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

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.exceptions.binary.BinaryException;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.Utils;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.net.*;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.CustomTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Main window of application
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class MainWindow extends JFrame implements ActionListener
{
    private JPanel topPanel;
    private JButton startButton;
    private JButton closeButton;
    private JButton settingsButton;
    
    private JPanel statusPanel;
    private JLabel statusIcon;
    private JLabel statusText;
    private JProgressBar statusProgress;
    private JPanel statusTitle;
    
    private JDialog closeDialog;
    
    private JLabel initPanel;
    
    private JScrollPane dataPanel;

    
    private Topology topology;
    
    private static final Logger LOG = Logger.getLogger(MainWindow.class.getName());
    
    /**
     * Constructor of windows
     * @param title Text to be displayed in title of window
     * @throws IOException Image read failed
     */
    public MainWindow(String title) throws IOException
    {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    

    }
    
    /**
     * Views interface to user
     * @throws IOException Image read failed
     */
    public void view() throws IOException
    {
        
        LOG.finer("Starting GUI");
        
        this.closeDialog = new JDialog(this, "Close application", true);
        this.createCloseDialog();
        
        this.topPanel = new JPanel(new GridLayout());
        this.getContentPane().add(this.topPanel, BorderLayout.NORTH);
        
        this.statusPanel = new JPanel(new GridLayout());
        this.getContentPane().add(this.statusPanel, BorderLayout.SOUTH);
        this.statusPanel.setBackground(Color.white);
        
        this.statusTitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.statusPanel.add(this.statusTitle);
        this.statusTitle.setBackground(Color.white);
        
        this.startButton = new JButton("Start scan", Utils.getIcon("play_32.png"));
        this.startButton.addActionListener(this);
        this.topPanel.add(this.startButton);
        
        /*
        this.settingsButton = new JButton("Settings", Utils.getIcon("wrench_32.png"));
        this.settingsButton.addActionListener(this);
        this.topPanel.add(this.settingsButton);
        */
        
        this.closeButton = new JButton("Close", Utils.getIcon("close_32.png"));
        this.closeButton.addActionListener(this);
        this.topPanel.add(this.closeButton);
        
        this.statusIcon = new JLabel(Utils.getIcon("ready_24.png"));
        this.statusTitle.add(this.statusIcon);
        
        this.statusText = new JLabel("Ready");
        this.statusTitle.add(this.statusText);
        
        this.statusProgress = new JProgressBar();
        this.statusProgress.setBackground(new Color(240, 255, 240));
        this.statusProgress.setForeground(new Color(0, 230, 0));
        this.statusProgress.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 0)));
        this.statusProgress.setMinimum(0);
        this.statusProgress.setMaximum(100);
        this.statusProgress.setValue(0);
        this.statusProgress.setVisible(false);
        this.statusPanel.add(this.statusProgress);

        this.initPanel = new JLabel(Utils.getIcon("title.png"));
        this.getContentPane().add(this.initPanel, BorderLayout.CENTER);
        
        
        this.setSize(800, 600);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand())
        {
            case "Close":
                this.closeDialog.setVisible(true);
                break;
            case "Start scan":
            {
                try {

                    this.getContentPane().remove(this.initPanel);
                    if (this.dataPanel != null)
                    {
                        this.getContentPane().remove(this.dataPanel);
                    }
                     this.initPanel = new JLabel(Utils.getIcon("scan.png"));
                    this.getContentPane().add(this.initPanel);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                Thread scanThread = new Thread(this.topology);
                scanThread.start();
                break;



        }
    }
    
    /**
     * Creates closing dialog
     * @throws IOException Image read failed
     */
    private void createCloseDialog() throws IOException
    {
        LOG.finer("Creating close dialog");
        this.closeDialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.closeDialog.setResizable(false);
        
        JLabel questionImg = new JLabel(Utils.getIcon("question_96.png"));
        this.closeDialog.add(questionImg);
        
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        this.closeDialog.add(questionPanel);
        
        JLabel question = new JLabel("Are you sure you want close application?");   
        questionPanel.add(question);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        questionPanel.add(buttonPanel);
        
        JButton noButton = new JButton("NO", Utils.getIcon("close_24.png"));
        buttonPanel.add(noButton);
        noButton.addActionListener(new ActionListener(){
                     
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MainWindow.this.closeDialog.setVisible(false);
            }
        
        });
        
        JButton yesButton = new JButton("YES", Utils.getIcon("check_24.png"));
        buttonPanel.add(yesButton);
        yesButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        
        });
        
        this.closeDialog.pack();
    }
    
    /**
     * Sets icon of step in progress
     * @param icon 
     */
    public void setProgressIcon(ImageIcon icon)
    {
        this.statusIcon.setIcon(icon);
    }
    
    /**
     * Sets text of step in progress
     * @param text 
     */
    public void setProgressText(String text)
    {
        this.statusText.setText(text);
    }
    
    /**
     * Sets actual percentage of progress
     * @param percentage Progress to be set
     */
    public void setProgressPercentage(int percentage)
    {
        LOG.log(Level.FINEST, "Setting progress to GUI to {0}", percentage);
        this.statusProgress.setValue(percentage);
    }

    /**
     * Views/hides progress bar
     * @param visible 
     */
    public void visibleProgress(boolean visible)
    {
        this.statusProgress.setVisible(visible);
    }
    
    /**
     * Sets topology to window
     * @param t 
     */
    public void setTopology(Topology t)
    {
        this.topology = t;
    }

    /**
     * Displays topology to user in table
     */
    public void viewTopology()
    {
        String[] columns = new String[4];
        columns[0] = "IP address";
        columns[1] = "MAC address";
        columns[2] = "Vendor";
        columns[3] = "Hostname";
        JTable data = new JTable(new CustomTableModel(this.prepareData(), columns));
        this.dataPanel = new JScrollPane(data);
        this.getContentPane().remove(this.initPanel);
        this.getContentPane().add(this.dataPanel, BorderLayout.CENTER);
        LOG.finer("Displaying table");
        
    }
    
    /**
     * Prepares data from topology for table
     * @return Array of data
     */
    private Object[][] prepareData()
    {
        ArrayList<Device> devices = this.topology.getDevices();
        Object[][] reti = new Object[devices.size()][];
        int idx = 0;
        for (Device device : devices)
        {
            reti[idx] = new Object[4];
            reti[idx][0] = device.getIP().toString();
            reti[idx][1] = device.getMAC().toString();
            reti[idx][2] = device.getVendor().toString();
            reti[idx][3] = device.getHostname();
            idx++;
        }
        
        return reti;
    }
}
