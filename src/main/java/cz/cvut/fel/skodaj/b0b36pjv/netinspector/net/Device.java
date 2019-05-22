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

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.IPAddress;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.MACAddress;

/**
 * Class holding all information about network device
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class Device {
    
    /**
     * IP address of device
     */
    private IPAddress ip;
    
    /**
     * MAC address of device
     */
    private MACAddress mac;
    
    /**
     * Hostname of device
     */
    private String hostname;
    
    /**
     * Constructor of device class
     * @param ip IP address of device
     * @param mac MAC address of device
     */
    public Device(IPAddress ip, MACAddress mac)
    {
        this.ip = ip;
        this.mac = mac;
    }
    
    /**
     * Sets hostname of device
     * @param name Hostname of device
     */
    public void setHostname(String name)
    {
        this.hostname = name;
    }
    
    /**
     * Gets IP address of device
     * @return IP address of device
     */
    public String getIP()
    {
        return this.ip.toIPString();
    }
    
    /**
     * Gets MAC address of device
     * @return MAC address of device
     */
    public String getMAC()
    {
        return this.mac.toString();
    }
    
    
    /**
     * Gets vendor of network card of device
     * @return Vendor of network card of device
     */
    public String getVendor()
    {
        return this.mac.getVendor();
    }
    
    /**
     * Gets hostname of device
     * @return Hostname of device
     */
    public String getHostname()
    {
        return this.hostname;
    }
}
