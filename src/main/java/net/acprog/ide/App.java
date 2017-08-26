package net.acprog.ide;

import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.gui.MainFrame;

import javax.swing.*;
import java.io.File;

public class App {

    public static void main(String[] args) {
        IdeProject ideProject = IdeProject.openProject(new File("c:\\_data\\skola\\diplomovka\\AcprogProjects\\Blink\\"));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    MainFrame frame = new MainFrame(ideProject);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
