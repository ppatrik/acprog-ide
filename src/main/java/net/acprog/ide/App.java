package net.acprog.ide;

import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.configurations.IdeSettings;
import net.acprog.ide.configurations.IdeSettingsProject;
import net.acprog.ide.gui.EditorFrame;
import net.acprog.ide.gui.OpenFrame;
import net.acprog.ide.gui.SettingsFrame;
import net.acprog.ide.platform.Platform;
import net.acprog.ide.utils.DiscoveryManager;
import net.acprog.ide.utils.OSUtils;

import javax.swing.*;
import java.io.File;

public class App {

    public static void main(String[] args) {
        initPlatform();
        try {
            getPlatform().setLookAndFeel();

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
            IdeSettingsProject latestProject = settings.getRecentProject();
            if (latestProject != null) {
                App.openProject(latestProject);
            } else {
                App.openProjectChooser();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openProjectChooser() {
        JDialog frame = new OpenFrame();
        frame.pack();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public static void openSettings() {
        JDialog frame = new SettingsFrame();
        frame.pack();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public static void openProject(IdeSettingsProject ideSettingsProject) {
        try {
            IdeSettings.getInstance().addRecentProject(ideSettingsProject);
            IdeProject ideProject = null;
            ideProject = ideSettingsProject.getIdeProject();
            EditorFrame frame = new EditorFrame(ideProject);
            frame.setLocationRelativeTo(null);
            SwingUtilities.invokeLater(() -> frame.setVisible(true));
        } catch (IdeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Acprog error", JOptionPane.ERROR_MESSAGE);
            App.openProjectChooser();
        }
    }

    private static DiscoveryManager discoveryManager;
    private static Platform platform;
    private static String currentDirectory = System.getProperty("user.dir");

    public static DiscoveryManager getDiscoveryManager() {
        if (discoveryManager == null) {
            discoveryManager = new DiscoveryManager();
        }
        return discoveryManager;
    }

    public static Platform getPlatform() {
        return platform;
    }

    protected static void initPlatform() {
        try {
            Class<?> platformClass = Class.forName("net.acprog.ide.platform.Platform");
            if (OSUtils.isMacOS()) {
                platformClass = Class.forName("net.acprog.ide.platform.macosx.Platform");
            } else if (OSUtils.isWindows()) {
                platformClass = Class.forName("net.acprog.ide.platform.windows.Platform");
            } else if (OSUtils.isLinux()) {
                platformClass = Class.forName("net.acprog.ide.platform.linux.Platform");
            }
            platform = (Platform) platformClass.newInstance();
        } catch (Exception e) {
            /*showError(tr("Problem Setting the Platform"),
                    tr("An unknown error occurred while trying to load\n" +
                            "platform-specific code for your machine."), e);*/
            System.exit(-1);
        }
    }

    public static File getContentFile(String name) {
        String appDir = System.getProperty("APP_DIR");
        if (appDir == null || appDir.length() == 0) {
            appDir = currentDirectory;
        }
        File installationFolder = new File(appDir);
        return new File(installationFolder, name);
    }
}
