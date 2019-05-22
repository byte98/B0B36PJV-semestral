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
package cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Provides ARP command
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class ARP implements Callable<ARPResult>
{
    /**
     * IP address to be searched
     */
    private IPAddress ipAddr;
    
    /**
     * Database of vendors
     */
    private Map<String, String> vendors;
    
    /**
     * Logger of class
     */
    private static final Logger LOG = Logger.getLogger(ARP.class.getName());
    
    /**
     * Creates ARP
     * @param host Address of host
     * @param vendors Database of vendors 
     */
    public ARP(IPAddress host, Map<String, String> vendors)
    {
        this.ipAddr = host;
        this.vendors = vendors;
    }
    
    /**
     * Executes ARP
     * @return Result of ARP command
     * @throws IOException IO went bad
     */
    public ARPResult execute() throws IOException
    {
        String command = "arp -a " + this.ipAddr.toIPString();
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new 
     InputStreamReader(p.getInputStream()));
        String s = null;
        String output = null;
        while ((s = stdInput.readLine()) != null)
        {
            output += s;
        }
        LOG.fine(command);
        String[] arpRes = new String[0];
        String parsedOutput = this.parseOutput(output);
        String os = System.getProperty("os.name");
        boolean unknown = false;
        
        if (os.toLowerCase().contains("win"))
        {
            arpRes = parsedOutput.split("-");
        }
        else if (os.toLowerCase().contains("nix"))
        {
             arpRes = parsedOutput.split(":");
        }
        if (arpRes.length < 6)
        {
            unknown = true;
            String tmp[] = new String[6];
            for (int i = 0; i < 6; i++)
            {
                if (i < arpRes.length)
                {
                    tmp[i] = arpRes[i];
                }
                else
                {
                    tmp[i] = "0";
                }
            }
            arpRes = tmp;
        }
        for (int i = 0; i < 6; i++)
        {
            if (arpRes[i].equals(new String("")))
            {
                arpRes[i] = "0";
            }
        }
        String tmp = new String();
        for (int i = 0; i < 6; i++)
        {
            if (arpRes[i].length() < 2)
            {
                tmp += "0";
            }
            tmp += arpRes[i];
            if (i < 5)
            {
                tmp += ":";
            }
        }
        String vendor = (unknown == true ? "(unknown)" : Utils.findVendor(tmp, vendors));
        
        return new ARPResult(
                this.ipAddr,
                new MACAddress(
                Integer.parseInt(arpRes[0], 16), 
                Integer.parseInt(arpRes[1], 16),
                Integer.parseInt(arpRes[2], 16),
                Integer.parseInt(arpRes[4], 16),
                Integer.parseInt(arpRes[4], 16),
                Integer.parseInt(arpRes[5], 16),
                vendor));

    }
    /**
     * Parses output of ARP
     * @param input Output
     * @return Parsed output
     */
    private String parseOutput(String input)
    {
        String reti = "";
        String os = System.getProperty("os.name");
        
        if (os.toLowerCase().contains("win"))
        {
            reti = this.parseWindowsOutput(input);
        }
        else
        {
            reti = "0:0:0:0:0:0";
        }
        return reti;
    }
    
    /**
     * Parses output of command in Windows OS
     * @param input Output of command
     * @return Parsed output
     */
    private String parseWindowsOutput(String input)
    {
        String reti = "";
        String vals[] = input.split(" ");
        for (String val : vals)
        {
            if (val.contains("-"))
            {
                if (val.length() == 17)
                {
                    reti = val;
                }
            }
        }

        return reti;
    }

    @Override
    public ARPResult call() throws Exception
    {
        return this.execute();
    }
    
}
