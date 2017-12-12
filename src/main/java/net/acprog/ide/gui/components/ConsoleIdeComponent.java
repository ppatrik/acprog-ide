package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.gui.utils.ConsoleInterface;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class ConsoleIdeComponent implements IdeComponent, ConsoleInterface {
    private final MainFrame mainFrame;

    private JScrollPane panel;
    private JTextPane textPane;


    public ConsoleIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        InitializeComponents();
    }

    private void InitializeComponents() {

        DefaultStyledDocument document = new DefaultStyledDocument();
        textPane = new JTextPane(document);
        //textPane.setEditable(false);

        panel = new JScrollPane(textPane);
    }

    public JComponent render() {
        return panel;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Console", render());
    }

    private synchronized void appendToPane(JTextPane tp, String msg, Color c) {
        tp.setEditable(true);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset;

        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
        tp.setEditable(false);
    }

    public void print(String line) {
        appendToPane(textPane, line, Color.BLACK);
    }

    public void println(String line) {
        appendToPane(textPane, line + "\n", Color.BLACK);
    }

    public void err(String line) {
        appendToPane(textPane, line, Color.RED);
    }

    public void errln(String line) {
        appendToPane(textPane, line + "\n", Color.RED);
    }
}
