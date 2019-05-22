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
package cz.cvut.fel.skodaj.b0b36pjv.netinspector.ui.gui;

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.net.Topology;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.ui.UserInterface;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Implementation of USer interface (graphical one)
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class GUI implements UserInterface
{

    /**
     * Main window of application
     */
    private MainWindow main;
    
    /**
     * Devices found in network
     */
    private Topology topology;
    
    /**
     * Constructor of GUI
     * @throws IOException Icon read failed
     */
    public GUI() throws IOException
    {
         this.main = new MainWindow("NetInspector");
         this.main.view();
    }
    
    @Override
    public void setIcon(Action action)
    {
        switch (action)
        {
            case PREPARING:
                try {
                    this.main.setProgressIcon(Utils.getIcon("work_24.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case SEARCHING_NETWORK:
            {
                try {
                    this.main.setProgressIcon(Utils.getIcon("search-net_24.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case LOADING_MAC:
            {
                try {
                    this.main.setProgressIcon(Utils.getIcon("remote_24.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case GETTING_NAMES:
                try {
                    this.main.setProgressIcon(Utils.getIcon("remote_24.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case DISPLAYING:
            {
                try {
                    this.main.setProgressIcon(Utils.getIcon("eye_24.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case FINISHED:
            {
                try {
                    this.main.setProgressIcon(Utils.getIcon("finish_24.png"));
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
                

        }
    }

    @Override
    public void setText(String text)
    {
        this.main.setProgressText(text);
    }

    @Override
    public void showProgress()
    {
        this.main.visibleProgress(true);
    }

    @Override
    public void hideProgress()
    {
       this.main.visibleProgress(false);
    }

    @Override
    public void setProgress(int value)
    {
        this.main.setProgressPercentage(value);
    }

    @Override
    public void setTopology(Topology t)
    {
        this.topology = t;
        this.main.setTopology(this.topology);
    }

    @Override
    public void viewTopology()
    {
        this.main.viewTopology();
    }


    
}
