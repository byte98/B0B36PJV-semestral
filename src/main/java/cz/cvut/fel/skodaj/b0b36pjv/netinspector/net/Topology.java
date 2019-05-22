/*
 * Copyright 2019 Jiří Škoda <skodaji4@fel.cvut.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.cvut.fel.skodaj.b0b36pjv.netinspector.net;

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.BinaryException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.ARP;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.IPAddress;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.MACAddress;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Ping;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.PingResult;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.ui.*;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.ARPResult;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.NSLookup;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.NSLookupResult;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Utils;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.net.Inet6Address;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class holding information about topology of current network
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class Topology implements Runnable
{
    /**
     * Devices in network
     */
    private ArrayList<Device> devices;
    
    /**
     * Addresses used in network
     */
    ArrayList<IPAddress> addresses;
    
    /**
     * Interfaces of local host
     */
    private ArrayList<NetworkInterface> myInterfaces;
    
    /**
     * Database of vendors of network cards
     */
    private Map<String, String> vendors;
    
    /**
     * Maximal allowed used threads
     */
    private int poolSize;
    
    /**
     * Path to file containing database of vendors
     */
    private URL vendorsFile;
    
    /**
     * User interface providing information about progress of getting topology
     */
    private UserInterface UI;
    
    /**
     * Logger of class
     */
    private static Logger LOG = Logger.getLogger(Topology.class.getName());
    
    /**
     * Constructor of Topology class
     * @param vendors Path to database containing vendors
     * @param poolSize Maximal allowed threads
     * @param ui User interface interface providing information about progress of getting topology
     */
    public Topology(URL vendors, int poolSize, UserInterface ui)
    {
        this.UI = ui;
        this.UI.hideProgress();
        this.myInterfaces = new ArrayList<NetworkInterface>();
        this.poolSize = poolSize;
        this.vendorsFile = vendors;
    }
    
    /**
     * Creates topology with informing user via User Interface
     * @throws IOException Error when reading database
     * @throws BinaryException Error when computing address
     * @throws InterruptedException Thread was interrupted
     * @throws ExecutionException Execution failed
     */
    public void create() throws IOException, BinaryException, InterruptedException, ExecutionException
    {
        long start = System.currentTimeMillis();
        this.UI.setIcon(UserInterface.Action.PREPARING);
        this.UI.setText("Preparing (loading vendors database)");
        LOG.fine("Loading database " + vendorsFile.getPath());
        
        long tmp1 = System.currentTimeMillis();
        this.vendors = Utils.loadVendors(vendorsFile.getPath());
        LOG.log(Level.INFO, "Vendors loaded in {0} s", (System.currentTimeMillis() - tmp1) / 1000);
        
        this.UI.setText("Preparing (loading local interfaces)");
        tmp1 = System.currentTimeMillis();
        this.loadLocalInterfaces();
        LOG.log(Level.INFO, "Interfaces loaded in {0} s", (System.currentTimeMillis() - tmp1) / 1000);
        tmp1 = System.currentTimeMillis();
        this.scanNetwork();
        LOG.log(Level.INFO, "Network scanned in {0} s", (System.currentTimeMillis() - tmp1) / 1000);
        this.UI.setIcon(UserInterface.Action.LOADING_MAC);
        this.UI.setProgress(0);
        this.UI.setText("Getting further information");
        tmp1 = System.currentTimeMillis();
        this.getMACAddresses();
        LOG.log(Level.INFO, "MAC addresses loaded in {0} s", (System.currentTimeMillis() - tmp1) / 1000);
        long stop = System.currentTimeMillis();
        this.UI.setIcon(UserInterface.Action.DISPLAYING);
        this.UI.setProgress(0);
        this.UI.setText("Getting hostnames");
        this.UI.setIcon(UserInterface.Action.GETTING_NAMES);
        tmp1 = System.currentTimeMillis();
        this.getHostnames();
        LOG.log(Level.INFO, "Hostnames loaded in {0} s", (System.currentTimeMillis() - tmp1) / 1000);
        
        this.UI.setText("Displaying devices");
        this.UI.viewTopology();
        long interval = stop - start;
        String text = Math.ceil((interval / 1000)*10)/10 + " s";
        if (interval < 1000)
        {
            text = "< 1 s";
        }
        this.UI.setText("Finished in " + text + ", found devices: " + this.devices.size());
        this.UI.setIcon(UserInterface.Action.FINISHED);
        this.UI.hideProgress();
        
    }
    
    /**
     * Gets interfaces of local host
     * @throws SocketException Socket failed
     */
    public void loadLocalInterfaces() throws SocketException
    {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();
            LOG.log(Level.FINER, "Found interface: {0}", networkInterface.getDisplayName());
            if (networkInterface.isLoopback() == false && networkInterface.isVirtual() == false && networkInterface.isUp())
            {
                this.myInterfaces.add(networkInterface);
                
            }

        }
    }
    
    /**
     * Gets all addresses of interface
     * @param iface Interface to recognize addresses from
     * @return List of addresses
     */
    private ArrayList<IPAddress> getAddressFromInterface(NetworkInterface iface)
    {
        ArrayList<IPAddress> reti = new ArrayList<IPAddress>();
        for (InterfaceAddress address : iface.getInterfaceAddresses())
        {
            LOG.log(Level.FINER, "Found address of interface: {0}", address.toString());
            if (address.getAddress() instanceof Inet6Address)
            {
                continue;
            }
            else
            {
                String addr = address.getAddress().getHostAddress();
                String[] vals = addr.split("\\.");
                int[] vals_v = new int[4];
                for (int i = 0; i < 4; i++)
                {
                    vals_v[i] = Integer.parseInt(vals[i]);
                }
               IPAddress ip = new IPAddress(vals_v[0], vals_v[1], vals_v[2], vals_v[3], address.getNetworkPrefixLength());
               reti.add(ip);
                
            }
        }
        return reti;
    }
    
    /**
     * Scans network
     * @return Count of found devices
     * @throws IOException Reading file failed
     * @throws BinaryException Binary computing failed
     * @throws InterruptedException Thread was interrupted
     * @throws ExecutionException Execution failed
     */
    public int scanNetwork() throws IOException, BinaryException, InterruptedException, ExecutionException
    {
        int reti = 0;
        ArrayList<Callable> tasks = new ArrayList<Callable>();
        ArrayList<Future> results = new ArrayList<Future>();
        int maximal = 1;
        this.addresses = new ArrayList<IPAddress>();
        for (NetworkInterface iface : this.myInterfaces)
        {
           ArrayList<IPAddress> default_addr = this.getAddressFromInterface(iface);
           for (IPAddress ip : default_addr)
           {
               IPAddress actual = ip.getNetworkAddress();
               actual = actual.next();
                while (actual.hasNext())
                {
                    int act_addr[] = actual.toArray();
                    int prefix = actual.getPrefix();
                    Ping p = new Ping(new IPAddress(act_addr[0], act_addr[1], act_addr[2], act_addr[3], prefix), 3);
                    tasks.add(p);
                    actual = actual.next();
                    reti++;
                    this.UI.setText("Preparing (loading available address " + (maximal + 1) + ")");
                    maximal++;
                }
           }
        }
        this.UI.setIcon(UserInterface.Action.SEARCHING_NETWORK);
        this.UI.setText("Searching network");
        this.UI.setProgress(0);
        this.UI.showProgress();
        int current = 0;
        ExecutorService eS = Executors.newFixedThreadPool(this.poolSize);
        for (Callable task : tasks)
        {
            results.add(eS.submit(task));
        }
        for (Future result : results)
        {
            int pct = (int)Math.ceil(((double)current / (double)maximal) * 100);
            this.UI.setText("Searching network (" + pct + " %)");
            this.UI.setProgress(pct);
            PingResult res = (PingResult) result.get();
            current++;
            if (res.isReachable())
            {
                this.addresses.add(res.getAddress());
            }
        }
        eS.shutdown();
        return this.addresses.size();
    }
    
    /**
     * Gets MAC addresses of found devices
     * @throws IOException File read failed
     * @throws InterruptedException Thread was interrupted
     * @throws ExecutionException Execution failed
     */
    public void getMACAddresses() throws IOException, InterruptedException, ExecutionException
    {
        this.devices = new ArrayList<Device>();
        ExecutorService eS = Executors.newFixedThreadPool(this.poolSize);
        ArrayList<Callable> tasks = new ArrayList<Callable>();
        ArrayList<Future> results = new ArrayList<Future>();
        int current = 0;
        for (IPAddress ip : this.addresses)
        {
            ARP a = new ARP(ip, this.vendors);
            tasks.add(a);
        }
        
        for (Callable task : tasks)
        {
            results.add(eS.submit(task));
        }
        
        int maximal = results.size();
        
        for (Future result : results)
        {
            int pct = (int)Math.ceil(((double)current / (double)maximal) * 100);
            this.UI.setText("Getting further information (" + pct + " %)");
            this.UI.setProgress(pct);
            ARPResult arp = (ARPResult) result.get();
            this.devices.add(new Device(arp.getIp(), arp.getMac()));
            current++;
        }
        eS.shutdown();
    }

    /**
     * Gets hostnames of devices
     * @throws InterruptedException Thread was interrupted
     * @throws ExecutionException Execution failed
     */
    public void getHostnames() throws InterruptedException, ExecutionException
    {
        ExecutorService eS = Executors.newFixedThreadPool(this.poolSize);
        ArrayList<Callable> tasks = new ArrayList<Callable>();
        ArrayList<Future> results = new ArrayList<Future>();
        int current = 0;
        for (IPAddress ip : this.addresses)
        {
            NSLookup ns = new NSLookup(ip);
            tasks.add(ns);
        }
        
        for (Callable task : tasks)
        {
            results.add(eS.submit(task));
        }
        
        int maximal = results.size();
        
        for (Future result : results)
        {
            int pct = (int)Math.ceil(((double)current / (double)maximal) * 100);
            this.UI.setText("Getting hostnames (" + pct + " %)");
            this.UI.setProgress(pct);
            NSLookupResult res = (NSLookupResult) result.get();
            this.setDeviceHostname(res.getIp(), res.getName());
            current++;
        }
        eS.shutdown();
    }
    
    /**
     * Sets hostname to device
     * @param addr IP address of device to set hostname to
     * @param name Hostname to be set
     */
    private void setDeviceHostname(IPAddress addr, String name)
    {
        for (Device d : this.devices)
        {
            if (d.getIP().equals(addr.toIPString()))
            {
                d.setHostname(name);
                break;
            }
        }
    }
    
    @Override
    public void run() {
        try {
            this.create();
        } catch (IOException ex) {
            Logger.getLogger(Topology.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BinaryException ex) {
            Logger.getLogger(Topology.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Topology.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Topology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Views found devices via user interface
     */
    public void viewDevices()
    {
        this.UI.viewTopology();
    }
    
    /**
     * Gets found devices
     * @return found devices
     */
    public ArrayList<Device> getDevices()
    {
        return this.devices;
    }
}
