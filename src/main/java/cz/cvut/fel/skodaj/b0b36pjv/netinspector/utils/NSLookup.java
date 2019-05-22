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
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Provides nslookup command
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class NSLookup implements Callable<NSLookupResult>
{
    /**
     * Hostname (result of NSLookup)
     */
    private String hostname;
    
    /**
     * IP addres of host, which hostname will be resolved
     */
    private IPAddress host;
    
    /**
     * Class logger
     */
    private static final Logger LOG = Logger.getLogger(NSLookup.class.getName());
    
    /**
     * Prepares NSLookup command
     * @param address Address to be resolved
     */
    public NSLookup(IPAddress address)
    {
        this.host = address;
        this.hostname = " ";
    }
    
    /**
     * Executes nsLookup command
     * @return Result of nslookup command
     * @throws IOException IO failed
     */
    public NSLookupResult execute() throws IOException
    {
        String os = System.getProperty("os.name");
        
        if (os.toLowerCase().contains("win"))
        {
            this.hostname = this.getHostnameWindows();
        }
        return new NSLookupResult(this.host, this.hostname);
    }
    
    /**
     * Resolves hostname on OS Microsoft Windows
     * @return String containing hostname
     * @throws IOException IO failed
     */
    private String getHostnameWindows() throws IOException
    {
        String reti = " ";
        String command = "nslookup " + this.host.toIPString();
        LOG.fine(command);
        Process nslookup = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new 
        InputStreamReader(nslookup.getInputStream()));
        String s = null;
        String output = null;
        while ((s = stdInput.readLine()) != null)
        {
            output += s;
        }
        String[] values = output.split(" ");
        ArrayList<String> usefull = new ArrayList<String>();
        for (String val : values)
        {
            if (val.contains("Address:"))
            {
                usefull.add(val);
            }
        }
        if (usefull.size() > 0)
        {
            reti = usefull.get((usefull.size() - 1));
            reti = reti.replace("Address:", "");
        }

        return reti;
    }

    @Override
    public NSLookupResult call() throws Exception
    {
        return this.execute();
    }
    
}
