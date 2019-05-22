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

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.ArrayListNotBinaryException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.ArrayNotBinaryException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.ArrayTooSmallForBinaryException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.BinaryException;
import java.util.ArrayList;

/**
 * Class to implements integers binary numbers
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class binary
{
    /**
     * Decimal value of number
     */
    private int value;
    
    /**
     * Binary representation (stored as {@code ArrayList} of 1 and 0)
     */
    private ArrayList<Integer> representation;
    
    /**
     * Creates binary number from decimal {@code Integer} number
     * @param value Decimal number
     */
    public binary(int value)
    {
        this.value = value;
        this.representation = new ArrayList<Integer>();
        this.convertToBinary();
    }
    
    /**
     * Creates binary number from list of 0 and 1
     * @param value List of 0 and 1
     */
    public binary(ArrayList<Integer> value) throws ArrayListNotBinaryException
    {
        for (int v : value)
        {
            if (v != 0 && v != 1)
            {
                throw new ArrayListNotBinaryException("value");
            }
        }
        this.representation = value;
        this.value = 0;
        this.convertToDecimal();
    }
    
    /**
     * Creates binary number from array of 0 and 1
     * @param value Array of 0 and 1
     */
    public binary(int[] value) throws ArrayNotBinaryException
    {
        this.representation = new ArrayList<Integer>();
        for (int v : value)
        {
            if (v != 0 && v != 1)
            {
                throw new ArrayNotBinaryException("value");
            }
            this.representation.add(v);
        }
        this.value = 0;
        this.convertToDecimal();
    }
    
    /**
     * Internal conversion of decimal value to binary representation
     */
    private void convertToBinary()
    {
        ArrayList<Integer> temp = new ArrayList<>();
        int remain = this.value;
        while (remain > 0)
        {
            temp.add((remain % 2));
            remain = remain / 2;
        }
        for (int i = temp.size() - 1; i >= 0; i--)
        {
            this.representation.add(temp.get(i));
        }
    }
    
    /**
     * Computes decimal value of binary number
     */
    private void convertToDecimal()
    {
        this.value = 0;
        int exponent = 0;
        for (int i = this.representation.size() - 1; i >= 0; i--)
        {
            this.value += this.representation.get(i) * Math.pow(2, exponent);
            exponent++;
        }
    }
    
    
    /**
     * Gets decimal value of {@code binary } number
     * @return Decimal value
     */
    public int toDecimal()
    {
        return this.value;
    }
    
    /**
     * Gets array of 0 and 1 representing binary number
     * @return Array containing representation of binary number
     */
    public int[] toArray()
    {
        int[] reti = new int[this.representation.size()];
        for (int i = 0; i < this.representation.size(); i++)
        {
            reti[i] = this.representation.get(i);
        }
        return reti;
    }
    
    /**
     * Gets specified long array of 0 and 1 representing binary number
     * @param length Length of result array
     * @return Specified long array representing binary number
     * @throws ArrayTooSmallForBinaryException Exception, when binary number cannot fit into specified long array
     */
    public int[] toArray(int length) throws ArrayTooSmallForBinaryException
    {
        if (length < this.representation.size())
        {
            throw new ArrayTooSmallForBinaryException(length, this);
        }
        int[] reti = new int[length];
        int diff = length - this.representation.size();
        for (int i = 0; i < length; i++)
        {
            if ((i - diff) >= 0)
            {
                reti[i] = this.representation.get(i - diff);
            }
            else
            {
                reti[i] = 0;
            }
        }
        
        return reti;
    }
    
    /**
     * Gets representation of binary number
     * @return {@code ArrayList} containing 0 and 1 which represents binary number
     */
    public ArrayList<Integer> getRepresentation()
    {
        return this.representation;
    }
    
    /**
     * Gets length of binary number
     * @return Length of representation of binary number
     */
    public int length()
    {
        return this.representation.size();
    }
    
    /**
     * Provides operation logical AND for two binary numbers
     * @param a First {@code binary} number
     * @param b Second {@code binary} number
     * @return Result of logical AND done on a and b
     * @throws BinaryException Exception when something gone wrong
     */
    public static binary b_and(binary a, binary b) throws BinaryException
    {
        binary larger = a;
        binary smaller = b;
        if (smaller.length() > larger.length())
        {
            binary tmp = smaller;
            smaller = larger;
            larger = tmp;
        }
        if (smaller.length() != larger.length())
        {
            int[] smaller_repr = smaller.toArray(larger.length());
            smaller = new binary(smaller_repr);
        }
        int[] retiVal = new int[larger.representation.size()];
        for (int i = 0; i < larger.representation.size(); i++)
        {
            retiVal[i] = larger.representation.get(i) * smaller.representation.get(i);
        }
        return new binary(retiVal);
    }
    
    
   /**
     * Provides operation logical OR for two binary numbers
     * @param a First {@code binary} number
     * @param b Second {@code binary} number
     * @return Result of logical AND done on a and b
     * @throws BinaryException Exception when something gone wrong
     */
    public static binary b_or (binary a, binary b) throws BinaryException
    {
        binary larger = a;
        binary smaller = b;
        if (smaller.length() > larger.length())
        {
            binary tmp = smaller;
            smaller = larger;
            larger = tmp;
        }
        if (smaller.length() != larger.length())
        {
            int[] smaller_repr = smaller.toArray(larger.length());
            smaller = new binary(smaller_repr);
        }
        int[] retiVal = new int[larger.representation.size()];
        for (int i = 0; i < larger.representation.size(); i++)
        {
            retiVal[i] = ((larger.representation.get(i) == 1 ||  smaller.representation.get(i)== 1) ? 1 : 0);
        }
        return new binary(retiVal);
    }
    
}
