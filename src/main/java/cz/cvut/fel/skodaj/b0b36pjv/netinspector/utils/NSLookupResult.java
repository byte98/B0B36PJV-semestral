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

/**
 * Stores result of nslookup command
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class NSLookupResult
{
    /**
     * IP address of host
     */
    private IPAddress address;
    
    /**
     * Resolved hostname
     */
    private String hostname;
    
    /**
     * Creates result of nslookup
     * @param address Address which was resolved
     * @param hostname Resolved hostname
     */
    public NSLookupResult(IPAddress address, String hostname)
    {
        this.hostname = hostname;
        this.address = address;
    }
    
    /**
     * Gets IP address which was resolved
     * @return Host IP address
     */
    public IPAddress getIp()
    {
        return this.address;
    }
    
    /**
     * Gets resolved hostname
     * @return Resolved hostname
     */
    public String getName()
    {
        return this.hostname;
    }
}
