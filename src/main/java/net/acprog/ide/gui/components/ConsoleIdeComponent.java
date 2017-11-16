package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.gui.MainFrame;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;

public class ConsoleIdeComponent implements IdeComponent {
    private final MainFrame mainFrame;

    private JScrollPane panel;
    private JTextArea textArea;


    public ConsoleIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        InitializeComponents();
    }

    private void InitializeComponents() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel = new JScrollPane(textArea);
    }

    public JComponent render() {
        return panel;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Console", render());
    }


    public synchronized void appendRow(String string) {
        textArea.append(string);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void print(String line) {
        appendRow(line);
    }

    public void println(String line) {
        appendRow(line + "\n");
    }
}
