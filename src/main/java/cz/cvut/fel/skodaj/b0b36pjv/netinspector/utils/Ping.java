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

import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.Callable;

/**
 *
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class Ping implements Callable<PingResult>
{
    private IPAddress host;
    private int timeout;
    private static final Logger LOG = Logger.getLogger(Ping.class.getName(), Ping.class);
    
    
    public Ping(IPAddress host, int timeout)
    {
        this.host = host;
        this.timeout = timeout;
    }

    public PingResult execute() throws IOException, InterruptedException {
        long reti = -1;

        String command = "ping ";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win"))
        {
            command += "-n";
        }
        else if (os.toLowerCase().contains("linux"))
        {
            command += "-c";
        }
        command += " 1 ";
        if (os.toLowerCase().contains("win"))
        {
            command += "-w " + (this.timeout * 1000) + " ";
        }
        else if (os.toLowerCase().contains("linux"))
        {
            command += "-W " + this.timeout + " ";
        }
        
        command += this.host.toIPString();
        
        long start = System.currentTimeMillis();
        Process pingProc = java.lang.Runtime.getRuntime().exec(command);
        
        int returnVal = pingProc.waitFor();
        if (returnVal == 0)
        {
            long stop = System.currentTimeMillis();
            reti = new Long(stop - start);
        }
        LOG.fine(command);
        return new PingResult(this.host, reti);
    }

    @Override
    public PingResult call() throws Exception
    {
        return this.execute();
    }

    
}
