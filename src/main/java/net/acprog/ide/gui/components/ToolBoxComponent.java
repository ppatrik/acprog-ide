package net.acprog.ide.gui.components;

import net.acprog.builder.modules.Module;
import net.acprog.ide.configurations.Settings;
import net.acprog.ide.utils.ACPModules;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.File;
import java.util.Collection;

public class ToolBoxComponent implements Component {
    protected JScrollPane scrollPane;

    public ToolBoxComponent() {
        InitializeComponents();
    }

    private void InitializeComponents() {
        File acpModulesDirectory = Settings.getInstance().getAcprogModulesFolder();
        ACPModules acpModules = new ACPModules(acpModulesDirectory);
        Collection<Module> allModules = acpModules.scanDirectory();

        JButton button;
        MigLayout migLayout = new MigLayout();
        JPanel buttonGroup = new JPanel(migLayout);
        for (Module module : allModules) {
            button = new JButton();
            button.setText(module.getName());
            buttonGroup.add(button, "wrap");
        }

        scrollPane = new JScrollPane(buttonGroup);
        scrollPane.getVerticalScrollBar().setUnitIncrement(100);
        scrollPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    }

    public JComponent render() {
        return scrollPane;
    }
}
