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
 * Stores result of ping command
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class PingResult
{
    /**
     * Is host reachable?
     */
    private boolean isReachable;
    /**
     * IP address of host
     */
    private IPAddress address;
    /**
     * Mow long did it takes to command
     */
    private Long delay;
    
    /**
     * Creates result of ping command
     * @param address Address which was pinged
     * @param delay Measured delay of ping
     */
    public PingResult(IPAddress address, Long delay)
    {
        this.address = address;
        this.delay = delay;
        this.isReachable = (this.delay.compareTo(new Long(0)) < 0);                
    }
    
    /**
     * Gets pinged IP address
     * @return pinged IP address
     */
    public IPAddress getAddress()
    {
        return this.address;
    }
    
    /**
     * Check, if IP address is reachable
     * @return Is IP address reachable?
     */
    public boolean isReachable()
    {
        return this.isReachable;
    }
    
    /**
     * Gets measured delay of ping command
     * @return measured delay
     */
    public Long getDelay()
    {
        return this.delay;
    }
}
