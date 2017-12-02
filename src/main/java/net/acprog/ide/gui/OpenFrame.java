package net.acprog.ide.gui;

import net.acprog.ide.App;
import net.acprog.ide.configurations.IdeSettingsProject;

import javax.swing.*;
import java.awt.*;

public class OpenFrame extends JDialog {

    public OpenFrame() {
        setLayout(new BorderLayout());
        setTitle("Open project");
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        InitializeComponets();
    }

    private void InitializeComponets() {

        JButton button = new JButton("...");
        button.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setDialogTitle("Select acprog project folder");
            fc.addActionListener(e1 -> {
                App.openProject(IdeSettingsProject.fromFile(fc.getSelectedFile()));
                OpenFrame.this.dispose();
            });
            fc.showOpenDialog(this);
        });

        add(button, BorderLayout.CENTER);
    }

}
