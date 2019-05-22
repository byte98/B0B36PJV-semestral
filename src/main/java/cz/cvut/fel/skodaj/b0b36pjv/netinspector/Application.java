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
package cz.cvut.fel.skodaj.b0b36pjv.netinspector;

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.BinaryException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.net.Topology;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.ui.*;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.ui.gui.*;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Utils;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Entry point of applications
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class Application
{
    /**
     * Logger of class Application
     */
    public static final Logger LOG = Logger.getLogger(Application.class.getName());
    
    /**
     * Main program
     * @param args Main function arguments
     * @throws IOException Error on input/output
     * @throws BinaryException Error on computing with binary numbers
     * @throws InterruptedException Thread interrupted error
     * @throws ExecutionException Execution failed
     */
    public static void main(String[] args) throws IOException, BinaryException, InterruptedException, ExecutionException {
        UserInterface ui = new GUI();
        
        URL vendorsFile = Application.class.getClassLoader().getResource("vendors.json");
        Topology topo = new Topology(vendorsFile, 1000, ui);
        ui.setTopology(topo);
    }
}
