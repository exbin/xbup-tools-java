/*
 * Copyright (C) ExBin Project, https://exbin.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.jaguif.component.page;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import org.exbin.jaguif.tabpages.api.TabPagesComponent;

/**
 * Xbup pages panel.
 */
@NullMarked
public class XbupPagesPanel extends javax.swing.JPanel {

    private final List<PageRecord> pageRecords = new ArrayList<>();
    private int activePage = 0;
    private JComponent borderComponent = null;
    private String preferredPageId = null;
    private boolean updateMode = false;

    public XbupPagesPanel() {
        initComponents();
        init();
    }

    private void init() {
        modeComboBox.addItemListener((ItemEvent e) -> {
            int selectedIndex = modeComboBox.getSelectedIndex();
            if (selectedIndex >= 0) {
                switchTo(selectedIndex);
                if (!updateMode) {
                    preferredPageId = (String) getPageId(pageRecords.get(selectedIndex).page);
                }
            }
        });
    }

    private void switchTo(int index) {
        if (activePage >= 0 && activePage < pageRecords.size()) {
            PageRecord prevRecord = pageRecords.get(activePage);
            prevRecord.button.setSelected(false);
            remove(prevRecord.page.getComponent());
        }
        PageRecord record = pageRecords.get(index);
        record.button.setSelected(true);
        add(record.page.getComponent(), BorderLayout.CENTER);
        activePage = index;
        revalidate();
        repaint();
    }

    public void addPage(XbupComponentPage page) {
        if (pageRecords.isEmpty()) {
            add(page.getComponent(), BorderLayout.CENTER);

            if (borderComponent != null) {
                remove(borderComponent);
            }
        }

        PageRecord record = new PageRecord(page);
        final int index = pageRecords.size();
        pageRecords.add(record);
        String pageName = getPageName(page);
        record.button = new JToggleButton(pageName);
        record.button.setSelected(index == 0);
        record.button.addActionListener((e) -> {
            switchTo(index);
            preferredPageId = getPageId(pageRecords.get(index).page);
        });
        modeComboBox.addItem(pageName);
        selectionPanel.add(record.button);
    }

    public void addPage(String name, JComponent component) {
        addPage(new BasicXbupComponentPage(name, component));
    }

    public void finishPages() {
        int pagesCount = pageRecords.size();
        if (preferredPageId != null) {
            for (int i = 0; i < pagesCount; i++) {
                PageRecord pageRecord = pageRecords.get(i);
                if (getPageId(pageRecord.page).equals(preferredPageId)) {
                    switchTo(i);
                }
            }
        }

        if (pagesCount > 0) {
            modeComboBox.setEnabled(true);
        }

        updateMode = false;
    }

    public void removeAllPages() {
        if (!pageRecords.isEmpty()) {
            if (activePage >= 0 && activePage < pageRecords.size()) {
                PageRecord prevRecord = pageRecords.get(activePage);
                remove(prevRecord.page.getComponent());
            }
            modeComboBox.removeAllItems();
            modeComboBox.setEnabled(false);
            selectionPanel.removeAll();
            pageRecords.clear();

            if (borderComponent != null) {
                add(borderComponent, BorderLayout.CENTER);
            }

            revalidate();
            repaint();
        }

        updateMode = true;
    }

    public void setMainComponent(JComponent component) {
        if (borderComponent != null) {
            remove(borderComponent);
        }
        add(component, BorderLayout.CENTER);
        borderComponent = component;
    }

    public List<XbupComponentPage> getPages() {
        List<XbupComponentPage> result = new ArrayList<>();
        for (PageRecord pageRecord : pageRecords) {
            result.add(pageRecord.page);
        }
        return result;
    }

    private static String getPageId(XbupComponentPage page) {
        return (String) page.getValue(TabPagesComponent.KEY_ID);
    }

    private static String getPageName(XbupComponentPage page) {
        return (String) page.getValue(TabPagesComponent.KEY_NAME);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        selectionPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        modeComboBox = new javax.swing.JComboBox<>();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.shadow")));
        headerPanel.setLayout(new java.awt.BorderLayout());

        selectionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        headerPanel.add(selectionPanel, java.awt.BorderLayout.CENTER);

        controlPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        modeComboBox.setEnabled(false);
        modeComboBox.setFocusable(false);
        controlPanel.add(modeComboBox);

        headerPanel.add(controlPanel, java.awt.BorderLayout.EAST);

        add(headerPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JComboBox<String> modeComboBox;
    private javax.swing.JPanel selectionPanel;
    // End of variables declaration//GEN-END:variables

    @NullMarked
    private static final class PageRecord {

        XbupComponentPage page;
        JToggleButton button;

        public PageRecord(XbupComponentPage page) {
            this.page = page;
        }
    }
}
