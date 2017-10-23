package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;
import net.acprog.builder.modules.Module;
import net.acprog.ide.configurations.IdeSettings;
import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.utils.ACPModules;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.File;
import java.util.Collection;

public class ToolBoxIdeComponent implements IdeComponent {
    private final MainFrame mainFrame;

    protected JScrollPane scrollPane;

    public ToolBoxIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        InitializeComponents();
    }

    private void InitializeComponents() {
        File acpModulesDirectory = IdeSettings.getInstance().getAcprogModulesFolder();
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
    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Toolbox", render());
    }
}
