package net.acprog.ide;

import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.configurations.IdeSettings;
import net.acprog.ide.configurations.IdeSettingsProject;
import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.gui.OpenFrame;
import net.acprog.ide.gui.SettingsFrame;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


                IdeSettings settings = IdeSettings.getInstance();
                if (!settings.isInitializedEmpty()) {
                    App.openSettings();
                }

                // todo CLI parameter moze tiez otvorit projekt!

                IdeSettingsProject latestProject = settings.getLatestProject();
                if (latestProject != null) {
                    App.openProject(latestProject);
                } else {
                    App.openProjectChooser();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void openProjectChooser() {
        JDialog frame = new OpenFrame();
        frame.pack();
        frame.setVisible(true);
    }

    public static void openSettings() {
        JDialog frame = new SettingsFrame();
        frame.pack();
        frame.setVisible(true);
    }

    public static void openProject(IdeSettingsProject ideSettingsProject) {
        IdeProject ideProject = ideSettingsProject.getIdeProject();
        MainFrame frame = new MainFrame(ideProject);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
