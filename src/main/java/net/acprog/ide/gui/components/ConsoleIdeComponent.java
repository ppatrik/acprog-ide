package net.acprog.ide.gui.components;

import net.acprog.ide.gui.MainFrame;

import javax.swing.*;

public class ConsoleIdeComponent implements IdeComponent {
    private final MainFrame mainFrame;

    protected JTextField panel;

    public ConsoleIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        InitializeComponents();
    }

    private void InitializeComponents() {
        panel = new JTextField();
        panel.setText("Ahoj svet, ja som konzola");
    }

    public JComponent render() {
        return panel;
    }
}
