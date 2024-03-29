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
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.IPAddress;
import cz.cvut.fel.skodaj.b0b36pjv.netinspector.utils.MACAddress;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.awt.event.MouseListener;
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
public class MainWindow extends JFrame implements ActionListener, MouseListener
{
    /**
     * Panel on upper side
     */
    private JPanel topPanel;
    
    /**
     * "Start scan" button
     */
    private JButton startButton;
    
    /**
     * "Close" button
     */
    private JButton closeButton;
    
    /**
     * "Settings" button
     * 
     * (not working anymore)
     */
    private JButton settingsButton;
    
    /**
     * Status bar
     */
    private JPanel statusPanel;
    
    /**
     * Icon of status
     */
    private JLabel statusIcon;
    
    /**
     * Text of status
     */
    private JLabel statusText;
    
    /**
     * Progress bar of status
     */
    private JProgressBar statusProgress;
    
    /**
     * Title of status
     */
    private JPanel statusTitle;
    
    
    /**
     * Confirming close dialog
     */
    private JDialog closeDialog;
    
    /**
     * First panel to be showed
     */
    private JLabel initPanel;
    
    /**
     * Scrollable panel containing table with data
     */
    private JScrollPane dataPanel;
    
    /**
     * Table containing data
     */
    private JTable dataTable;
    
    /**
     * Textbox used to search table
     */
    private JTextField searchBox;
    
    /**
     * Panel containing search textbox and icon
     */
    private JPanel searchPanel;
    

    /**
     * Topology to be viewed
     */
    private Topology topology;
    
    /**
     * Logger of class
     */
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

        this.initPanel = new JLabel(Utils.getIcon("title.png"));
        this.getContentPane().add(this.initPanel, BorderLayout.CENTER);
        
        this.searchPanel = new JPanel(new FlowLayout());
        

        

        this.searchPanel.add(new JLabel(Utils.getIcon("search_24.png")));
        this.searchPanel.add(new JLabel("Search "));     
        
        
        this.searchPanel.setBackground(Color.white);

        /////////////////////////////////////////////////////////////////////////////////////////////////
        /*this.dataTable = new JTable();
        String[] columns = new String[4];
        columns[0] = "IP address";
        columns[1] = "MAC address";
        columns[2] = "Vendor";
        columns[3] = "Hostname";
        CustomTableModel tableModel = new CustomTableModel();
        tableModel.setDataVector(this.prepareData(), columns);
        this.dataTable.setModel(tableModel);
        this.dataTable.setAutoCreateRowSorter(true);
        this.dataPanel = new JScrollPane(this.dataTable);
        this.getContentPane().remove(this.initPanel);
        this.getContentPane().add(this.dataPanel, BorderLayout.CENTER);
        LOG.finer("Displaying table");
        this.statusPanel.remove(this.statusProgress);
        this.statusPanel.add(this.searchPanel);
        this.searchBox = RowFilterUtil.createRowFilter(this.dataTable);
        this.searchPanel.add(this.searchBox);
        this.searchBox.setPreferredSize(new Dimension(128, 26));
        this.dataPanel.setVisible(true);
        this.dataTable.addMouseListener(this);*/
        ////////////////////////////////////////////////////////////////////////////////////////
        
        
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
                    this.statusPanel.remove(this.searchPanel);
                    this.statusPanel.add(this.statusProgress);
                    if (this.dataPanel != null)
                    {
                        this.getContentPane().remove(this.dataPanel);
                    }
                     this.initPanel = new JLabel(Utils.getIcon("scan.png"));
                    this.getContentPane().add(this.initPanel);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Thread topoThread = new Thread(this.topology);
                topoThread.start();
                break;
            }
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
        if (this.dataTable == null)
        {
            this.dataTable = new JTable();
        }


        String[] columns = new String[4];
        columns[0] = "IP address";
        columns[1] = "MAC address";
        columns[2] = "Vendor";
        columns[3] = "Hostname";
        CustomTableModel tableModel = new CustomTableModel();
        tableModel.setDataVector(this.prepareData(), columns);
        this.dataTable.setModel(tableModel);
        this.dataTable.setAutoCreateRowSorter(true);
        this.dataPanel = new JScrollPane(this.dataTable);
        this.getContentPane().remove(this.initPanel);
        this.getContentPane().add(this.dataPanel, BorderLayout.CENTER);
        LOG.finer("Displaying table");
        this.statusPanel.remove(this.statusProgress);
        this.statusPanel.add(this.searchPanel);
        this.searchBox = RowFilterUtil.createRowFilter(this.dataTable);
        this.searchPanel.add(this.searchBox);
        this.searchBox.setPreferredSize(new Dimension(128, 26));
        this.dataPanel.setVisible(true);
        this.dataTable.addMouseListener(this);
        
    }
    
    /**
     * Prepares data from topology for table
     * @return Array of data
     */
    private Object[][] prepareData()
    {
        Object[][] reti = new Object[this.topology.getDevices().size()][];
        int idx = 0;
        for (Device device : this.topology.getDevices())
        {
            reti[idx] = new Object[4];
            reti[idx][0] = device.getIP();
            reti[idx][1] = device.getMAC();
            reti[idx][2] = device.getVendor();
            reti[idx][3] = device.getHostname();
            idx++;
        }
        //Object[][] reti = {{"10.4.116.106", "2C:F0:EE:19:2A:10", "VendorA", "   "}, {"10.4.116.107", "2C:F0:EE:19:2A:11", "Vendor1", "Hostname"}};
        
        return reti;
    }

    
    /**
     * Shows detail of device
     * @param ipAddress IP address of device
     * @throws IOException Image read failed
     */
    private void showDetail(String ipAddress) throws IOException
    {
        Device d = this.topology.getDeviceByIp(ipAddress);
        DetailWindow dw = new DetailWindow(d);
        dw.view();
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
        {
            int row = this.dataTable.rowAtPoint(e.getPoint());
            String ip = (String) this.dataTable.getValueAt(row, 0);
            try {
                this.showDetail(ip);
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
