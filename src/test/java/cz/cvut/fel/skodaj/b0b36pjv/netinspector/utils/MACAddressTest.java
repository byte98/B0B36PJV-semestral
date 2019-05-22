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
public class MACAddressTest {
    
    public MACAddressTest() {
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
     * Test of getVendor method, of class MACAddress.
     */
    @Test
    public void testGetVendor() {
        System.out.println("getVendor");
        MACAddress instance = new MACAddress(0xfc, 0xff, 0xaa, 0x00, 0x00, 0x01, "IEEE Registration Authority");
        String expResult = "IEEE Registration Authority";
        String result = instance.getVendor();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class MACAddress.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        MACAddress instance = new MACAddress(0xfc, 0xff, 0xaa, 0x00, 0x00, 0x01, "IEEE Registration Authority");
        String expResult = "FC:FF:FA:00:00:01";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
