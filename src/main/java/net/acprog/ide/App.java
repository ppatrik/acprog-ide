package net.acprog.ide;

import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.configurations.IdeSettings;
import net.acprog.ide.configurations.IdeSettingsProject;
import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.gui.OpenFrame;
import net.acprog.ide.gui.SettingsFrame;

import javax.swing.*;
import java.io.File;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


                IdeSettings settings = IdeSettings.getInstance();
                if (!settings.isInitializedEmpty()) {
                    App.openSettings();
                }

                if (args.length >= 1) {
                    if ("open".equals(args[0])) {
                        if (args.length == 2) {
                            App.openProject(IdeSettingsProject.fromFile(new File(args[1])));
                        } else {
                            System.out.println("app.jar open project-directory");
                        }
                    }
                    if ("create".equals(args[0])) {
                        IdeSettingsProject.createNewProject(new File(args[1]), args[2]);
                    }
                }

                // Default otvorenie posledného projektu, ak taký nie je otvoríme okno na vytvorenie nového alebo výber projektu
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
        IdeSettings.getInstance().addLastProject(ideSettingsProject);
        IdeProject ideProject = ideSettingsProject.getIdeProject();
        MainFrame frame = new MainFrame(ideProject);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
