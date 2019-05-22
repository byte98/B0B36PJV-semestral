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
package cz.cvut.fel.skodaj.b0b36pjv.netinspector.ui;

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.net.*;

/**
 * User interface informing user about topology
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public interface UserInterface
{
    /**
     * Actual progress of making topology
     */
    enum Action
    {
        /**
         * Preparing for making topology
         */
        PREPARING,
        /**
         * Searching network for devices
         */
        SEARCHING_NETWORK,
        /**
         * Loading MAC addresses of devices
         */
        LOADING_MAC,
        /**
         * Getting hostnames of devices
         */
        GETTING_NAMES,        
        /**
         * Displaying topology via user interface
         */
        DISPLAYING,
        /**
         * Everything has been done
         */
        FINISHED
    }
    /**
     * Sets icon of actual action
     * @param action Actual action
     */
    public void setIcon(Action action);
    
    /**
     * Sets text of actual step in process
     * @param text Text to be displayed
     */
    public void setText(String text);
    
    /**
     * Sets percentual progress
     * @param value Percent of progress
     */
    public void setProgress(int value);
    
    /**
     * Shows progress
     */
    public void showProgress();
    
    /**
     * Hides progress
     */
    public void hideProgress();
    
    /**
     * Sets topology to User interface
     * @param t Topology to be set
     */
    public void setTopology(Topology t);
    
    /**
     * Displays topology to user
     */
    public void viewTopology();
    
    
}
