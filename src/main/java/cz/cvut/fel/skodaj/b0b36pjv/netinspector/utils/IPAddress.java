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

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.ip.IPException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.ip.AddressOutOfCurrentRangeException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.BinaryException;
import static cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.binary.*;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class to handle IPv4 address manipulation
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class IPAddress implements Iterator<IPAddress>, Comparable<IPAddress>
{
    enum addressClass
    {
        UNKNOWN,
        A,
        B,
        C,
        D,
        E, 
        OTHER;
    }
    
    /**
     * Data of address stored as array of octets
     */
    private int [] address;
    
    /**
     * Network prefix
     */
    private int prefix;
    
    /**
     * Value of address stored as array of binary numbers
     */
    private ArrayList<binary> bin_address;
    
    private addressClass cls = addressClass.UNKNOWN;
    
    
    public IPAddress(int a, int b, int c, int d, int prefix)
    {
        this.address = new int[4];
        this.address[0] = a;
        this.address[1] = b;
        this.address[2] = c;
        this.address[3] = d;
        this.prefix = prefix;
        this.bin_address = new ArrayList<binary>();
        this.toBinary();
        this.resolveAddressClass();
    }
    
    /**
     * Converts IP address to binary
     */
    private void toBinary()
    {
        for (int val : this.address)
        {
            this.bin_address.add(new binary(val));
        }
    }

    /**
     * Gets network address
     * @return Network address
     * @throws BinaryException 
     */
    public IPAddress getNetworkAddress() throws BinaryException
    {
        IPAddress reti = null;
       
       //Convert whole address to binary representation
       int[] bin = new int[32];
       int idx = 0;
       for (int i = 0; i < 4; i++)
       {
           int[] arr = this.bin_address.get(i).toArray(8);
           for (int j = 0; j < 8; j++)
           {
               bin[idx] = arr[j];
               idx++;
           }
       }
       
       //Make mask
       int[] mask = new int[32];
       for(int i = 0; i < 32; i++)
       {
           mask[i] = (i < this.prefix ? 1 : 0);
       }
       
       binary addr = new binary(bin);
       binary netmask = new binary(mask);
       
       //Compute net address
       binary net_addr = b_and(addr, netmask);
       
       //Cast result to ip address
       int[] net_addr_arr = net_addr.toArray(32);
       int[][] result = new int[4][];
       idx = 0;
       for (int i = 0; i < 4; i++)
       {
           result[i] = new int[8];
           for (int j = 0; j < 8; j++)
           {
               result[i][j] = net_addr_arr[idx];
               idx++;
           }
       }
       binary a_b, b_b, c_b, d_b;
       a_b = new binary(result[0]);
       b_b = new binary(result[1]);
       c_b = new binary(result[2]);
       d_b = new binary(result[3]);
       reti = new IPAddress(a_b.toDecimal(), b_b.toDecimal(), c_b.toDecimal(), d_b.toDecimal(), this.prefix);
       
       
       return reti;
    }
    
    /**
     * Gets Broadcast address
     * @return Broadcast address
     * @throws BinaryException 
     */
    public IPAddress getBroadcastAddress() throws BinaryException
    {
        IPAddress reti = null;
               //Convert whole address to binary representation
       int[] bin = new int[32];
       int idx = 0;
       for (int i = 0; i < 4; i++)
       {
           int[] arr = this.bin_address.get(i).toArray(8);
           for (int j = 0; j < 8; j++)
           {
               bin[idx] = arr[j];
               idx++;
           }
       }
       
       //Make mask
       int[] mask = new int[32];
       for(int i = 0; i < 32; i++)
       {
           mask[i] = (i < this.prefix ? 0 : 1);
       }
       
       binary addr = new binary(bin);
       binary netmask = new binary(mask);
       
       //Compute broadcast address
       binary bc_addr = b_or(addr, netmask);
       
       //Cast result to ip address
       int[] bc_addr_arr = bc_addr.toArray(32);
       int[][] result = new int[4][];
       idx = 0;
       for (int i = 0; i < 4; i++)
       {
           result[i] = new int[8];
           for (int j = 0; j < 8; j++)
           {
               result[i][j] = bc_addr_arr[idx];
               idx++;
           }
       }
       binary a_b, b_b, c_b, d_b;
       a_b = new binary(result[0]);
       b_b = new binary(result[1]);
       c_b = new binary(result[2]);
       d_b = new binary(result[3]);
       reti = new IPAddress(a_b.toDecimal(), b_b.toDecimal(), c_b.toDecimal(), d_b.toDecimal(), this.prefix);
       
       
       return reti;
    }
   
    
    @Override
    public boolean hasNext()
    {
        boolean reti = true;
        IPAddress wrong = null;
        IPAddress next = new IPAddress(this.address[0], this.address [1], this.address[2], this.address[3], this.prefix);
        next.increment();
        try
        {
            wrong = this.getBroadcastAddress();
            wrong.increment();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        if(next.compareTo(wrong) >= 0)
        {
            reti = false;
        }

        return reti;
    }

    /**
     * Increments address
     */
    private void increment()
    {
        boolean overflow = true;
        for (int i = 3; i >=0; i--)
        {
            if (overflow)
            {
                this.address[i]++;
                overflow = false;
            }
            if (this.address[i] > 255)
            {
                this.address[i] = 0;
                overflow = true;
            }
        }
    }
    
    @Override
    public String toString()
    {
        String reti = "";
        for (int i = 0; i < 4; i++)
        {
            reti += this.address[i];
            if (i < 3)
            {
                reti += ".";
            }
        }
        reti += "/";
        reti += this.prefix;
        
        return reti;
    }

    @Override
    public int compareTo(IPAddress ip) {
        int reti = 0;
        
        for (int i = 0; i < 4; i++)
        {
            if (this.address[i] > ip.address[i])
            {
                reti = 1;
                break;
            }
            else if (this.address[i] < ip.address[i])
            {
                reti = -1;
                break;
            }
        }
        
        return reti;
    }

    @Override
    public IPAddress next()
    {
        IPAddress reti = null;
        reti = this;
        reti.increment();
        IPAddress wrong = null;
        try
        {
            wrong = this.getBroadcastAddress();
            wrong.increment();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        if(reti.equals(wrong))
        {
            try {
                IPException ex = new AddressOutOfCurrentRangeException(reti);
                System.err.println("Error! " + ex.getMessage());
            } catch (BinaryException ex1) {
                System.err.println(ex1.getMessage());
            }
        }
        return reti;
    }
    
    /**
     * Check if addresses equals
     * @param ip address to be compared with
     * @return  True if yes, false if not
     */
    public boolean equals(IPAddress ip)
    {
        boolean reti = true;
        for (int i = 0; i < 4; i++)
        {
            if (this.address[i] != ip.address[i])
            {
                reti = false;
                break;
            }
        }
        if (ip.prefix != this.prefix)
        {
            reti = false;
        }
        return reti;
    }

    /**
     * Gets count of address in network space
     * @return Count of addresses
     */
    public int getRangeAddress()
    {
        return (int) (Math.pow(2, this.prefix) - 2);
    }
    
    /**
     * Resolves address class
     */
    private void resolveAddressClass()
    {
        if (this.address[1] <= 126)
        {
            this.cls = addressClass.A;
        }
        else if (this.address[1] >= 128 && this.address[1] <= 191)
        {
            this.cls = addressClass.B;
        }
        else if (this.address[1] >= 192 && this.address[1] <= 223)
        {
            this.cls = addressClass.C;
        }
        else if (this.address[1] >= 224 && this.address[1] <= 239)
        {
            this.cls = addressClass.D;
        }
        else if (this.address[1] >= 240 && this.address[1] <= 254)
        {
            this.cls = addressClass.E;
        }
        else
        {
            this.cls = addressClass.OTHER;
        }
    }
    
    /**
     * Gets string without prefix
     * @return IP address in string
     */
    public String toIPString()
    {
        return this.address[0] + "." + this.address[1] + "." + this.address[2] + "." + this.address[3];
    }
    
    /**
     * Transforms IP address to byte array
     * @return IP address in bytes
     */
    public byte[] toByteArray()
    {
        byte[] reti = {(byte)this.address[0],(byte) this.address[1], (byte)this.address[2],(byte) this.address[3]};
        return reti;
    }

    /**
     * Gets address data in array
     * @return array with address data
     */
    public int[] toArray()
    {
        return this.address;
    }
    
    /**
     * Gets prefix of address
     * @return Prefix of address
     */
    public int getPrefix()
    {
        return this.prefix;
    }
}
