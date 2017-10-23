package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.builder.modules.Module;
import net.acprog.ide.configurations.IdeSettings;
import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.utils.ACPModules;
import net.acprog.ide.utils.event.EventType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        ToolBoxComponent moduleButton;
        MigLayout migLayout = new MigLayout();
        JPanel buttonGroup = new JPanel(migLayout);
        for (Module module : allModules) {
            moduleButton = new ToolBoxComponent(this, module);
            buttonGroup.add(moduleButton, "wrap");
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

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
