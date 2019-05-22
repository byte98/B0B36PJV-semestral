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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Class holding all data to MAC addresses
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class MACAddress
{
    private int[] data;
    String vendor;
    
    /**
     * Constructor of MAC address (format a:b:c:d:e:f)
     * @param a <b>a</b>:b:c:d:e:f in MAC address
     * @param b a:<b>b</b>:c:d:e:f in MAC address
     * @param c a:b:<b>c</b>:d:e:f in MAC address
     * @param d a:b:c:<b>d</b>:e:f in MAC address
     * @param e a:b:c:d:<b>e</b>:f in MAC address
     * @param f a:b:c:d:e:<b>f</b> in MAC address
     * @param vendor Vendor of network card
     */
    public MACAddress(int a, int b, int c, int d, int e, int f, String vendor)
    {
        this.data = new int[6];
        this.data[0] = a;
        this.data[1] = b;
        this.data[2] = c;
        this.data[3] = d;
        this.data[4] = e;
        this.data[5] = f;
        this.vendor = vendor;
    }
    
    /**
     * Constructor of mac address which gets mac address from a string
     * @param address String containing MAC address
     * @param delimiter Delimiter in MAC address
     * @param vendor Vendor of network card
     */
    public MACAddress(String address, String delimiter, String vendor)
    {
        String vals[] = address.split(delimiter);
        int idx = 0;
        for (String val: vals)
        {
            this.data[idx] = Integer.parseInt(val, 16);
            idx++;
        }
        this.vendor = vendor;
    }
    
    /**
     * Gets vendor of network card
     * @return 
     */
    public String getVendor()
    {
        return this.vendor;
    }
    
    @Override
    public String toString()
    {
        String reti = "";
        for (int i = 0; i < 6; i++)
        {
            String add = Integer.toHexString(this.data[i]).toUpperCase();
            if (add.length() < 2)
            {
                add = "0" + add;
            }
            reti += add;
            if (i < 5)
            {
                reti += ":";
            }
        }
        if (reti.equals(new String("00:00:00:00:00:00")))
        {
            reti = "(unknown)";
        }
        return reti;
    }
}
