/*
 * Copyright (C) ExBin Project
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
package org.exbin.framework.viewer.xbup.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.exbin.auxiliary.dropdownbutton.DropDownButton;
import org.exbin.bined.CodeType;
import org.exbin.bined.highlight.swing.NonAsciiCodeAreaColorAssessor;
import org.exbin.bined.highlight.swing.NonprintablesCodeAreaAssessor;
import org.exbin.bined.swing.CodeAreaSwingUtils;
import org.exbin.bined.swing.capability.ColorAssessorPainterCapable;
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.framework.App;
import org.exbin.framework.bined.viewer.options.BinaryViewerOptions;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;

/**
 * Binary editor toolbar panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryToolbarPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BinaryToolbarPanel.class);

    private BinaryViewerOptions options;
    private SectCodeArea codeArea;

    private ActionListener goToPositionAction;

    private AbstractAction cycleCodeTypesAction;
    private JRadioButtonMenuItem binaryCodeTypeAction;
    private JRadioButtonMenuItem octalCodeTypeAction;
    private JRadioButtonMenuItem decimalCodeTypeAction;
    private JRadioButtonMenuItem hexadecimalCodeTypeAction;
    private ButtonGroup codeTypeButtonGroup;
    private DropDownButton codeTypeDropDown;

    public BinaryToolbarPanel() {
        initComponents();
        init();
    }

    private void init() {
        codeTypeButtonGroup = new ButtonGroup();
        binaryCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.binary")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.BINARY);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(binaryCodeTypeAction);
        octalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.octal")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.OCTAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(octalCodeTypeAction);
        decimalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.decimal")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.DECIMAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(decimalCodeTypeAction);
        hexadecimalCodeTypeAction = new JRadioButtonMenuItem(new AbstractAction(resourceBundle.getString("codeType.hexadecimal")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setCodeType(CodeType.HEXADECIMAL);
                updateCycleButtonState();
            }
        });
        codeTypeButtonGroup.add(hexadecimalCodeTypeAction);
        cycleCodeTypesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codeTypePos = codeArea.getCodeType().ordinal();
                CodeType[] values = CodeType.values();
                CodeType next = codeTypePos + 1 >= values.length ? values[0] : values[codeTypePos + 1];
                codeArea.setCodeType(next);
                updateCycleButtonState();
            }
        };

        cycleCodeTypesAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("cycleCodeTypesAction.text"));
        JPopupMenu cycleCodeTypesPopupMenu = new JPopupMenu();
        cycleCodeTypesPopupMenu.add(binaryCodeTypeAction);
        cycleCodeTypesPopupMenu.add(octalCodeTypeAction);
        cycleCodeTypesPopupMenu.add(decimalCodeTypeAction);
        cycleCodeTypesPopupMenu.add(hexadecimalCodeTypeAction);
        codeTypeDropDown = new DropDownButton(cycleCodeTypesAction, cycleCodeTypesPopupMenu);
        controlToolBar.add(codeTypeDropDown, 0);
    }
    
    public void setCodeArea(SectCodeArea codeArea) {
        this.codeArea = codeArea;
        NonAsciiCodeAreaColorAssessor nonAsciiColorAssessor = CodeAreaSwingUtils.findColorAssessor((ColorAssessorPainterCapable) codeArea.getPainter(), NonAsciiCodeAreaColorAssessor.class);
        if (nonAsciiColorAssessor != null) {
            codeColorizationToggleButton.setSelected(nonAsciiColorAssessor.isNonAsciiHighlightingEnabled());
        }
        updateCycleButtonState();
    }

    public void setOptions(BinaryViewerOptions options) {
        this.options = options;
    }

    public void setGoToPositionAction(ActionListener goToPositionAction) {
        this.goToPositionAction = goToPositionAction;
    }

    private void updateCycleButtonState() {
        CodeType codeType = codeArea.getCodeType();
        codeTypeDropDown.setActionText(codeType.name().substring(0, 3));
        switch (codeType) {
            case BINARY: {
                if (!binaryCodeTypeAction.isSelected()) {
                    binaryCodeTypeAction.setSelected(true);
                }
                break;
            }
            case OCTAL: {
                if (!octalCodeTypeAction.isSelected()) {
                    octalCodeTypeAction.setSelected(true);
                }
                break;
            }
            case DECIMAL: {
                if (!decimalCodeTypeAction.isSelected()) {
                    decimalCodeTypeAction.setSelected(true);
                }
                break;
            }
            case HEXADECIMAL: {
                if (!hexadecimalCodeTypeAction.isSelected()) {
                    hexadecimalCodeTypeAction.setSelected(true);
                }
                break;
            }
        }
    }

    public void applyFromCodeArea() {
        updateCycleButtonState();
        updateNonprintables();
    }

    public void loadFromPreferences() {
        codeArea.setCodeType(options.getCodeAreaOptions().getCodeType());
        updateCycleButtonState();
        updateNonprintables();
    }

    public void updateNonprintables() {
        NonprintablesCodeAreaAssessor nonprintablesCodeAreaAssessor = CodeAreaSwingUtils.findColorAssessor((ColorAssessorPainterCapable) codeArea.getPainter(), NonprintablesCodeAreaAssessor.class);
        if (nonprintablesCodeAreaAssessor != null) {
            showNonprintablesToggleButton.setSelected(nonprintablesCodeAreaAssessor.isShowNonprintables());
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (codeTypeDropDown != null) {
            codeTypeDropDown.updateUI();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlToolBar = new javax.swing.JToolBar();
        goToButton = new javax.swing.JButton();
        separator1 = new javax.swing.JToolBar.Separator();
        showNonprintablesToggleButton = new javax.swing.JToggleButton();
        codeColorizationToggleButton = new javax.swing.JToggleButton();
        separator2 = new javax.swing.JToolBar.Separator();

        controlToolBar.setBorder(null);
        controlToolBar.setRollover(true);

        goToButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/viewer/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/bullet_go.png"))); // NOI18N
        goToButton.setToolTipText(resourceBundle.getString("goToButton.toolTipText"));
        goToButton.setFocusable(false);
        goToButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goToButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        goToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(goToButton);
        controlToolBar.add(separator1);

        showNonprintablesToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/viewer/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/insert-pilcrow.png"))); // NOI18N
        showNonprintablesToggleButton.setToolTipText(resourceBundle.getString("showNonprintablesToggleButton.toolTipText")); // NOI18N
        showNonprintablesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNonprintablesToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(showNonprintablesToggleButton);

        codeColorizationToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/viewer/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/color_swatch.png"))); // NOI18N
        codeColorizationToggleButton.setToolTipText(resourceBundle.getString("codeColorizationToggleButton.toolTipText"));
        codeColorizationToggleButton.setFocusable(false);
        codeColorizationToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        codeColorizationToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        codeColorizationToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeColorizationToggleButtonActionPerformed(evt);
            }
        });
        controlToolBar.add(codeColorizationToggleButton);
        controlToolBar.add(separator2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 231, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controlToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void showNonprintablesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNonprintablesToggleButtonActionPerformed
        NonprintablesCodeAreaAssessor nonprintablesCodeAreaAssessor = CodeAreaSwingUtils.findColorAssessor((ColorAssessorPainterCapable) codeArea.getPainter(), NonprintablesCodeAreaAssessor.class);
        if (nonprintablesCodeAreaAssessor != null) {
            nonprintablesCodeAreaAssessor.setShowNonprintables(showNonprintablesToggleButton.isSelected());
        }
    }//GEN-LAST:event_showNonprintablesToggleButtonActionPerformed

    private void codeColorizationToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeColorizationToggleButtonActionPerformed
        NonAsciiCodeAreaColorAssessor nonAsciiColorAssessor = CodeAreaSwingUtils.findColorAssessor((ColorAssessorPainterCapable) codeArea.getPainter(), NonAsciiCodeAreaColorAssessor.class);
        if (nonAsciiColorAssessor != null) {
            nonAsciiColorAssessor.setNonAsciiHighlightingEnabled(codeColorizationToggleButton.isSelected());
        }
        codeArea.repaint();
    }//GEN-LAST:event_codeColorizationToggleButtonActionPerformed

    private void goToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToButtonActionPerformed
        goToPositionAction.actionPerformed(evt);
    }//GEN-LAST:event_goToButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new BinaryToolbarPanel());
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton codeColorizationToggleButton;
    private javax.swing.JToolBar controlToolBar;
    private javax.swing.JButton goToButton;
    private javax.swing.JToolBar.Separator separator1;
    private javax.swing.JToolBar.Separator separator2;
    private javax.swing.JToggleButton showNonprintablesToggleButton;
    // End of variables declaration//GEN-END:variables

}
