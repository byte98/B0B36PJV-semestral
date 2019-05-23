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

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.net.Device;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Ping;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.PingResult;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Utils;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.text.DecimalFormat;

/**
 * Simple response monitor
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class SimpleMonitor extends ResponseMonitoring
{
    /**
     * Device to be monitored
     */
    private Device device;
    
    /**
     * Container to display results
     */
    private Container container;
    
    /**
     * Run monitor?
     */
    private boolean run = true;
    
    /**
     * Icon of status
     */
    private JLabel icon;
    /**
     * Meassured delay
     */
    private JLabel time;
    
    /**
     * Good attemps
     */
    private int good = 0;
    
    /**
     * Reachable attemps
     */
    private int reachable = 0;
    
    /**
     * Unreachable attempts
     */
    private int unreachable = 0;
    
    /**
     * Count of attemps
     */
    private int attemps = 0;
    
    /**
     * Label for good attemps
     */
    private JLabel goodLabel;
    
    /**
     * Label for reachable attemps
     */
    private JLabel reachableLabel;
    
    /**
     * Label for unreachable attemps
     */
    private JLabel unreachableLabel;
    
    /**
     * Default font
     */
    private Font font = new Font("sans-serif", Font.PLAIN, 24);
    
    /**
     * Red colour definition
     */
    private Color red = new Color(200, 0, 0);
    
    /**
     * Yellow colour definition
     */
    private Color yellow = new Color(214, 168, 70);
    
    /**
     * Green colour definition
     */
    private Color green = new Color(0, 200, 0);
    
    /**
     * Constructor of simple monitor
     * @param container Container to view results
     * @param device Device which should be monitored
     * @throws IOException Image read failed
     */
    public SimpleMonitor(Container container, Device device) throws IOException
    {
        this.container = container;
        this.device = device;
        
        this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
        
        this.icon = new JLabel(Utils.getIcon("cross_48.png"));
        Box b1 = Box.createHorizontalBox();
        b1.add(this.icon);
        
        this.time = new JLabel("?");
        this.time.setForeground(red);
        Box b2 = Box.createHorizontalBox();
        b2.add(this.time);
        
        this.time.setFont(font);
                this.time.setText("?");
        this.time.setForeground(red);
                            try {
            this.icon.setIcon(Utils.getIcon("cross_48.png"));
        } catch (IOException ex) {
            Logger.getLogger(SimpleMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.container.add(b1);
        this.container.add(b2);
        
        
        
        JLabel stHeader = new JLabel ("Statistics");
        stHeader.setFont(new Font("sans-serif", Font.BOLD, 16));
        Box b3 = Box.createHorizontalBox();
        b3.add(stHeader);
        this.container.add(b3);
        
        JLabel gL = new JLabel("< 100 ms : ");
        gL.setForeground(green);
        this.goodLabel = new JLabel();
        this.goodLabel.setForeground(green);
        Box b4 = Box.createHorizontalBox();
        b4.add(gL);
        b4.add(this.goodLabel);
        this.container.add(b4);
        
        JLabel rL = new JLabel(">= 100 ms : ");
        rL.setForeground(yellow);
        this.reachableLabel = new JLabel();
        this.reachableLabel.setForeground(yellow);
        Box b5 = Box.createHorizontalBox();
        b5.add(rL);
        b5.add(this.reachableLabel);
        this.container.add(b5);
        
        JLabel uL = new JLabel("Unreachable : ");
        uL.setForeground(red);
        this.unreachableLabel = new JLabel();
        this.unreachableLabel.setForeground(red);
        Box b6 = Box.createHorizontalBox();
        b6.add(uL);
        b6.add(this.unreachableLabel);
        this.container.add(b6);
        
        
    }
    

@Override
    public void run()
    {

        while (this.run)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimpleMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            ExecutorService es = Executors.newSingleThreadExecutor();
            Ping p = new Ping(this.device.getIPAddress(), 5);
            this.attemps++;
            Future<PingResult> future = es.submit(p);
            PingResult res = null;
            try {
                res = (PingResult) future.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(ResponseMonitor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ResponseMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (res != null)
            {
                if (res.getDelay() > 0 && res.getDelay() < 100)
                {
                    this.good++;
                    this.time.setText(res.getDelay().toString() + "ms");
                    this.time.setForeground(green);
                    try {
                        this.icon.setIcon(Utils.getIcon("check_48.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(SimpleMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if (res.getDelay() > 0)
                {
                    this.reachable ++;
                    this.time.setText(res.getDelay().toString() + " ms");
                    this.time.setForeground(yellow);
                    try {
                        this.icon.setIcon(Utils.getIcon("exclamation_48.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(SimpleMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    this.unreachable++;
                    this.time.setText("?");
                    this.time.setForeground(red);
                                        try {
                        this.icon.setIcon(Utils.getIcon("cross_48.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(SimpleMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.displaySatistics();
            }
            es.shutdown();
        }

    }
    
    /**
     * Displays statistics of monitoring
     */
    private void displaySatistics()
    {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        double goodVal = ((double)this.good / (double)this.attemps) * 100;
        double reachVal = ((double)this.reachable  / (double)this.attemps) * 100;
        double unreachVal = ((double)this.unreachable / (double)this.attemps) * 100;
        
        this.goodLabel.setText(df.format(goodVal)+ " %");
        this.reachableLabel.setText(df.format(reachVal) + " %");
        this.unreachableLabel.setText(df.format(unreachVal) + " %");
        
    }
    
    /**
     * Stops monitoring
     */
    @Override
    public void stop()
    {
        this.run = false;
    }
    
}
