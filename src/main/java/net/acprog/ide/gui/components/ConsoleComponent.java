package net.acprog.ide.gui.components;

import javax.swing.*;

public class ConsoleComponent implements Component {
    protected JTextField panel;

    public ConsoleComponent() {
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
