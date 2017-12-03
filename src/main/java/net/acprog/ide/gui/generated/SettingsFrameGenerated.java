/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acprog.ide.gui.generated;

/**
 * @author patrik
 */
abstract public class SettingsFrameGenerated extends javax.swing.JDialog {

    protected void InitializeComponets() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        compilerPropertiesPanel = new javax.swing.JPanel();
        arduinoCli = new javax.swing.JTextField();
        arduinoCliSelection = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        arduinoLibraryFolder = new javax.swing.JTextField();
        arduinoLibraryFolderSelection = new javax.swing.JButton();
        acprogModulesFolder = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        acprogModulesFolderSelection = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 0));

        compilerPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Compiler properties"));
        compilerPropertiesPanel.setPreferredSize(new java.awt.Dimension(400, 126));

        arduinoCli.setEditable(false);

        arduinoCliSelection.setText("...");

        jLabel1.setText("Arduino CLI");

        jLabel2.setText("Arduino Library Folder");

        arduinoLibraryFolder.setEditable(false);

        arduinoLibraryFolderSelection.setText("...");

        acprogModulesFolder.setEditable(false);

        jLabel3.setText("Acprog Modules Folder");

        acprogModulesFolderSelection.setText("...");

        javax.swing.GroupLayout compilerPropertiesPanelLayout = new javax.swing.GroupLayout(compilerPropertiesPanel);
        compilerPropertiesPanel.setLayout(compilerPropertiesPanelLayout);
        compilerPropertiesPanelLayout.setHorizontalGroup(
                compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(compilerPropertiesPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(acprogModulesFolder, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(arduinoCli, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(arduinoLibraryFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(arduinoLibraryFolderSelection)
                                        .addComponent(arduinoCliSelection)
                                        .addComponent(acprogModulesFolderSelection))
                                .addContainerGap())
        );
        compilerPropertiesPanelLayout.setVerticalGroup(
                compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(compilerPropertiesPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(arduinoCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(arduinoCliSelection)
                                        .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(arduinoLibraryFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(arduinoLibraryFolderSelection)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(compilerPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(acprogModulesFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(acprogModulesFolderSelection)
                                        .addComponent(jLabel3))
                                .addContainerGap())
        );

        saveButton.setText("Save");

        cancelButton.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(cancelButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveButton))
                                        .addComponent(compilerPropertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(compilerPropertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveButton)
                                        .addComponent(cancelButton))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    protected javax.swing.JTextField acprogModulesFolder;
    protected javax.swing.JButton acprogModulesFolderSelection;
    protected javax.swing.JTextField arduinoCli;
    protected javax.swing.JButton arduinoCliSelection;
    protected javax.swing.JTextField arduinoLibraryFolder;
    protected javax.swing.JButton arduinoLibraryFolderSelection;
    protected javax.swing.JButton cancelButton;
    private javax.swing.JPanel compilerPropertiesPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    protected javax.swing.JButton saveButton;
    // End of variables declaration                   
}
