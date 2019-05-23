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
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class DetailWindow extends JFrame implements ActionListener
{
    private Device device;
    
    private JPanel infoPanel;
    private JPanel chartPanel;
    private ResponseMonitoring rm;
    
    public DetailWindow(Device d)
    {
        this.device = d;
    }
    
    public void view() throws IOException
    {
        this.setTitle("Detail");
        
        this.setPreferredSize(new Dimension(300, 500));
        
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.infoPanel = new JPanel();
        this.infoPanel.setLayout(new BoxLayout(this.infoPanel, BoxLayout.Y_AXIS));
        Box b = Box.createHorizontalBox();
        b.add(new JLabel(Utils.getIcon("search_24.png")));
        JLabel header = new JLabel(" Detail of device");
        header.setFont(new Font("sans-serif", 10,20));
        header.setForeground(new Color(66, 134, 244));
        b.add(header);
        this.infoPanel.add(b);
        
        Box b2 = Box.createHorizontalBox();
        b2.add(new JLabel("IP address:   "));
        b2.add(new JLabel(this.device.getIP()));
        this.infoPanel.add(b2);
        
        Box b3 = Box.createHorizontalBox();
        b3.add(new JLabel("MAC address:   "));
        b3.add(new JLabel(this.device.getMAC()));
        this.infoPanel.add(b3);
        
        Box b4 = Box.createHorizontalBox();
        b4.add(new JLabel("Vendor:   "));
        b4.add(new JLabel(this.device.getVendor()));
        this.infoPanel.add(b4);
        
        Box b5 = Box.createHorizontalBox();
        b5.add(new JLabel("Hostname:   "));
        b5.add(new JLabel(this.device.getHostname()));
        this.infoPanel.add(b5);
        
        Box b6 = Box.createHorizontalBox();
        JButton responseMonitorBtn = new JButton("Start response monitor", Utils.getIcon("play_32.png"));
        responseMonitorBtn.addActionListener(this);
        b6.add(responseMonitorBtn);
        this.infoPanel.add(b6);
        
        this.getContentPane().add(this.infoPanel);
        
        this.chartPanel = new JPanel();
        this.getContentPane().add(this.chartPanel);
        
        this.pack();
        this.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Start response monitor")
        {
            //ResponseMonitoring rm = new ResponseMonitor(this.chartPanel, this.device);
            this.rm = null;
            try {
                rm = new SimpleMonitor(this.chartPanel, this.device);
            } catch (IOException ex) {
                Logger.getLogger(DetailWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread monitorThread = new Thread(rm);
            monitorThread.start();
        }
    }
    
    public void stopMonitor()
    {
        this.rm.stop();
    }
}
