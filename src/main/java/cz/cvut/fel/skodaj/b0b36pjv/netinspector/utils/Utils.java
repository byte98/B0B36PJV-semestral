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

import cz.cvut.fel.skodaj.b0b36pjv.netinspector.Application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.json.*;
import javax.swing.ImageIcon;

/**
 * Static class with some maybe useful utilities
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class Utils
{
    /**
     * Constructor of class, which is private to make it inaccessible
     */
    private Utils(){}
    
    /**
     * Function to revert array
     * @param array Array to be reverted
     * @return Reverted array
     */
    public static int[] revertArray(int[] array)
    {
        int[] reti = new int[array.length];
        for (int i = 0; i < array.length; i++)
        {
            reti[i] = array[array.length - i - 1];
        }
        return reti;
    }
    
    /**
     * Loads vendor from JSON file
     * @param filename Path to file containing database of vendors
     * @return Map containing vendors and mac addresses
     * @throws FileNotFoundException Database file wasn't found
     * @throws IOException File read failed
     */
    public static Map<String, String> loadVendors(String filename) throws FileNotFoundException, IOException
    {
        InputStream is = new FileInputStream(new File(filename));
        JsonReader reader =  Json.createReader(is);
        JsonArray arr = reader.readArray();
        Map<String, String> reti = new HashMap<String, String>();
        for (int i = 0; i < arr.size(); i++)
        {
            JsonObject item = arr.getJsonObject(i);
            JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
            item = jsonReader.readObject();
            String[] mac = item.getString("_mac_prefix").split(":");
            String macAdd = "";
            for (int m = 0; m < mac.length; m++)
            {
                macAdd += mac[m];
            }
            reti.put(macAdd, item.getString("_vendor_name"));
            jsonReader.close();
        }       
        return reti;
        
    }
    
    /**
     * Gets icon from project resources
     * @param name Name of file in resources
     * @return Loaded icon
     * @throws IOException File read failed
     */
    public static ImageIcon getIcon(String name) throws IOException
    {
        URL imgUrl = Application.class.getClassLoader().getResource(name);
        return new ImageIcon(ImageIO.read(imgUrl));
    }
    
    /**
     * Gets vendor from database according to mac address
     * @param mac Mac address to identify vendor
     * @param vendors Database of vendors
     * @return Found vendor or <i>(unknown)</i>
     */
    public static String findVendor(String mac, Map<String, String> vendors)
    {
        String reti = "(unknown)";
        Map<String, String> possible = new HashMap<String, String>();
        String[] macSplit = mac.split(":");
        String key = null;
        for (Entry<String, String> e : vendors.entrySet())
        {
            String tmp1 = e.getKey().toUpperCase();
            String tmp2 = (macSplit[0] + macSplit[1] + macSplit[2]).toUpperCase();
            if (tmp1.startsWith(tmp2))
            {
                possible.put(e.getKey(), e.getValue());
                key = e.getKey();
                
            }
            
        }
        if (possible.size() == 1)
        {
            reti = possible.get(key);
        }
        else if (possible.size() >= 2)
        {
            
        }
        return reti;
    }
}
