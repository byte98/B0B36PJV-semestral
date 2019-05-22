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

import java.util.ArrayList;
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
public class binaryTest {
    
    public binaryTest() {
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
     * Test of toDecimal method, of class binary.
     */
    @Test
    public void testToDecimal() {
        System.out.println("toDecimal");
        binary instance = new binary(5);
        int expResult = 5;
        int result = instance.toDecimal();
        assertEquals(expResult, result);
    }

    /**
     * Test of toArray method, of class binary.
     */
    @Test
    public void testToArray_0args() {
        System.out.println("toArray");
        binary instance = new binary(5);
        int[] expResult = {1, 0, 1};
        int[] result = instance.toArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toArray method, of class binary.
     */
    @Test
    public void testToArray_int() throws Exception {
        System.out.println("toArray");
        binary instance = new binary(5);
        int[] expResult = {0, 0, 1, 0, 1};
        int[] result = instance.toArray(5);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of length method, of class binary.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        binary instance = new binary(5);
        int expResult = 3;
        int result = instance.length();
        assertEquals(expResult, result);
    }

    /**
     * Test of b_and method, of class binary.
     */
    @Test
    public void testB_and() throws Exception {
        System.out.println("b_and");
        binary a = new binary(1);
        binary b = new binary(0);
        binary expResult = new binary(0);
        binary result = binary.b_and(a, b);
        assertEquals(expResult, result);
    }

    /**
     * Test of b_or method, of class binary.
     */
    @Test
    public void testB_or() throws Exception {
        System.out.println("b_or");
        binary a = new binary(1);
        binary b = new binary(1);
        binary expResult = new binary(1);
        binary result = binary.b_or(a, b);
        assertEquals(expResult, result);
    }
    
}
