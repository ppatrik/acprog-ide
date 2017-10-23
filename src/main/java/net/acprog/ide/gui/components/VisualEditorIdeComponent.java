package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.configurations.Component;
import net.acprog.ide.configurations.Project;
import net.acprog.ide.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class VisualEditorIdeComponent implements IdeComponent {
    private final MainFrame mainFrame;

    protected VisualEditorJPanel scrollPane;

    class VisualEditorJPanel extends JScrollPane {

        JPanel panel;

        public VisualEditorJPanel() {
            super();
            panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(Color.WHITE);
            setViewportView(panel);
        }

        void add(Component component) {
            ProjectComponent pc = new ProjectComponent(VisualEditorIdeComponent.this, component);
            panel.add(pc);
        }

    }

    public VisualEditorIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        InitializeComponents();

        // vlozenie komponentov do plochy
        Project project = mainFrame.getIdeProject().getProject();
        for (Component component : project.getComponents()) {
            scrollPane.add(component);
        }
    }

    private void InitializeComponents() {
        scrollPane = new VisualEditorJPanel();
    }

    public JComponent render() {
        return scrollPane;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Visual editor", render());
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
