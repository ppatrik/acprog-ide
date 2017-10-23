package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;
import net.acprog.ide.gui.MainFrame;

import javax.swing.*;

public interface IdeComponent {
    public JComponent render();

    public SingleCDockable dockable();
}
