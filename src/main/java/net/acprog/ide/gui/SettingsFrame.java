package net.acprog.ide.gui;

import net.acprog.ide.configurations.IdeSettings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SettingsFrame extends JDialog {

    private JPanel panel;
    private JPanel actionsPanel;

    public SettingsFrame() {
        setLayout(new BorderLayout());
        setTitle("Preferencies");
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        InitializeComponets();
    }

    private void InitializeComponets() {
        panel = new JPanel(new MigLayout());

        formRow("Arduino CLI",
                "Select Arduino CLI executable (ex. arduino_debug.exe)",
                JFileChooser.FILES_ONLY,
                IdeSettings.getInstance().getArduinoCli(),
                fc -> {
                    IdeSettings.getInstance().setArduinoCli(fc.getSelectedFile().toString());
                });

        formRow("Arduino Library Folder",
                "Select Arduino Library Folder executable (ex. arduino_debug.exe)",
                JFileChooser.DIRECTORIES_ONLY,
                IdeSettings.getInstance().getArduinoLibraryFolder(),
                fc -> {
                    IdeSettings.getInstance().setArduinoLibraryFolder(fc.getSelectedFile().toString());
                });

        formRow("Acprog Modules Folder",
                "Select Acprog Modules Folder executable (ex. arduino_debug.exe)",
                JFileChooser.DIRECTORIES_ONLY,
                IdeSettings.getInstance().getAcprogModulesFolder(),
                fc -> {
                    IdeSettings.getInstance().setAcprogModulesFolder(fc.getSelectedFile().toString());
                });


        actionsPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            IdeSettings.getInstance().saveSettingsToFile();
            setVisible(false);
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        actionsPanel.add(cancelButton);
        actionsPanel.add(saveButton);

        add(panel, BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.PAGE_END);
    }

    interface EventListener {
        void emit(JFileChooser fc);
    }

    private void formRow(String label, String fileChooserTitle, int mode, String fileName, EventListener callback) {
        JTextField textField;
        JButton button;

        JLabel jLabel = new JLabel(label);
        textField = new JTextField(fileName);
        textField.setEditable(false);
        button = new JButton("...");
        button.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(mode);
            fc.setDialogTitle(fileChooserTitle);
            fc.setSelectedFile(new File(fileName));
            fc.addActionListener(e1 -> callback.emit(fc));
            fc.showOpenDialog(this);
        });
        panel.add(jLabel);
        panel.add(textField, "grow");
        panel.add(button, "wrap");
    }

}
