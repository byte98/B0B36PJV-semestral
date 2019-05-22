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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class IPAddressTest {
    
    public IPAddressTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getNetworkAddress method, of class IPAddress.
     */
    @Test
    public void testGetNetworkAddress() throws Exception {
        System.out.println("getNetworkAddress");
        IPAddress instance = new IPAddress(192,168,0,100,24);
        IPAddress expResult = new IPAddress(192,168,0,0,24);
        IPAddress result = instance.getNetworkAddress();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getBroadcastAddress method, of class IPAddress.
     */
    @Test
    public void testGetBroadcastAddress() throws Exception {
        System.out.println("getBroadcastAddress");
        IPAddress instance = new IPAddress(192,168,0,100,24);
        IPAddress expResult = new IPAddress(192,168,0,255,24);
        IPAddress result = instance.getBroadcastAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of hasNext method, of class IPAddress.
     */
    @Test
    public void testHasNextTrue() {
        System.out.println("hasNext");
        IPAddress instance = new IPAddress(192, 168, 0 ,1, 24);
        boolean expResult = true;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hasNext method, of class IPAddress.
     */
    @Test
    public void testHasNextFalse() {
        System.out.println("hasNext");
        IPAddress instance = new IPAddress(192, 168, 0 ,255, 24);
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
    }


    /**
     * Test of toString method, of class IPAddress.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        IPAddress instance = new IPAddress(192, 168, 0, 100, 24);
        String expResult = "192.168.0.100/24";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class IPAddress.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        IPAddress ip = new IPAddress(192, 168, 0, 100, 24);
        IPAddress instance = new IPAddress(192, 168, 0, 200, 24);
        int expResult = 1;
        int result = instance.compareTo(ip);
        assertEquals(expResult, result);
    }

    /**
     * Test of next method, of class IPAddress.
     */
    @Test
    public void testNext() {
        System.out.println("next");
        IPAddress instance = new IPAddress(192, 168, 0, 100, 24);
        IPAddress expResult = new IPAddress(192, 168, 0, 101, 24);
        IPAddress result = instance.next();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class IPAddress.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        IPAddress ip = new IPAddress(192, 168, 0, 100, 24);
        IPAddress instance = new IPAddress(192, 168, 0, 100, 24);
        boolean expResult = true;
        boolean result = instance.equals(ip);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRangeAddress method, of class IPAddress.
     */
    @Test
    public void testGetRangeAddress() {
        System.out.println("getRangeAddress");
        IPAddress instance = new IPAddress(192, 168, 0, 100, 24);
        int expResult = (int) (Math.pow(2, 24) - 2);
        int result = instance.getRangeAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of toIPString method, of class IPAddress.
     */
    @Test
    public void testToIPString() {
        System.out.println("toIPString");
        IPAddress instance = new IPAddress(192, 168, 0, 100, 24);
        String expResult = "192.168.0.100.24";
        String result = instance.toIPString();
        assertEquals(expResult, result);
    }
    
}
