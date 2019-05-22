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

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Class used to filter table
 * Source: https://www.logicbig.com/tutorials/java-swing/jtable-row-filter.html
 * @author Jiří Škoda <skodaji4@fel.cvut.cz>
 */
public class RowFilterUtil {
    /**
     * Creates textbox from which will be get filter text
     * @param table Table with data
     * @return Textbox of filter
     */
    public static JTextField createRowFilter(JTable table) {
        RowSorter<? extends TableModel> rs = table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }

        TableRowSorter<? extends TableModel> rowSorter =
                (rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;

        if (rowSorter == null) {
            throw new RuntimeException("Cannot find appropriate rowSorter: " + rs);
        }

        final JTextField tf = new JTextField();
        tf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }

            private void update(DocumentEvent e) {
                String text = tf.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        return tf;
    }
}