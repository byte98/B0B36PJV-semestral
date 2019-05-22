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
 * Stores information about ARP
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class ARPResult
{
    /**
     * IP address of ARP
     */
    private IPAddress ip;
    
    /**
     * Returned MAC address
     */
    private MACAddress mac;
    
    /**
     * Constructor of ARP
     * @param ip IP address of host
     * @param mac MAC address of host
     */
    public ARPResult (IPAddress ip, MACAddress mac)
    {
        this.ip = ip;
        this.mac = mac;
    }
    
    /**
     * Gets IP
     * @return IP address of host 
     */
    public IPAddress getIp()
    {
        return this.ip;
    }
    
    /**
     * Gets MAC
     * @return Mac address of host
     */
    public MACAddress getMac()
    {
        return this.mac;
    }
    
}
