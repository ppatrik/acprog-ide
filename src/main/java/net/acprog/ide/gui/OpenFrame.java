package net.acprog.ide.gui;

import net.acprog.ide.App;
import net.acprog.ide.configurations.IdeSettingsProject;
import net.acprog.ide.gui.generated.OpenFrameGenerated;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OpenFrame extends OpenFrameGenerated {

    public OpenFrame() {
        setLayout(new BorderLayout());
        setTitle("Open project");
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        InitializeComponets();

        newProjectLocationSelection.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.addActionListener(e1 -> {
                File selection = fc.getSelectedFile();
                if (selection != null && selection.isDirectory()) {
                    newProjectLocation.setText(selection.getAbsolutePath());
                }
            });
            fc.setDialogTitle("Select where project folder should be located");
            fc.showOpenDialog(OpenFrame.this);
        });
        newProjectButton.addActionListener(e -> {
            try {
                IdeSettingsProject project = IdeSettingsProject.createNewProject(new File(newProjectLocation.getText()), newProjectName.getText());
                OpenFrame.this.dispose();
                App.openProject(project);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        openProjectLocationSelection.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.addActionListener(e1 -> {
                File selection = fc.getSelectedFile();
                if (selection != null && selection.isDirectory()) {
                    openProjectLocation.setText(selection.getAbsolutePath());
                }
            });
            fc.setDialogTitle("Select where your project folder is located");
            fc.showOpenDialog(OpenFrame.this);
        });
        openProjectButton.addActionListener(e -> {
            IdeSettingsProject project = IdeSettingsProject.fromFile(new File(openProjectLocation.getText()));
            OpenFrame.this.dispose();
            App.openProject(project);
        });
    }

}
