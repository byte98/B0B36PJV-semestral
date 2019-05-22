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
package cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary;

/**
 * Exception which says, that in set {@code ArrayList<Integer>} are not binary (0, 1) numbers.
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class ArrayListNotBinaryException extends Exception
{
    /**
     * Creates new exception warnings, that {@code ArrayList<Integer>} is not binary
     * @param arrayListName Name of {@code ArrayList<Integer>}
     */
    public ArrayListNotBinaryException(String arrayListName)
    {
        super("ArrayList(" + arrayListName + ") contains not binary values!");
    }
}
