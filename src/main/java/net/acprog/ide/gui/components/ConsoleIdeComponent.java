package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.gui.EditorFrame;
import net.acprog.ide.gui.utils.ConsoleInterface;
import net.acprog.ide.gui.utils.ProcessUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleIdeComponent implements IdeComponent, ConsoleInterface {
    private final EditorFrame editorFrame;

    private JScrollPane panel;
    private JTextPane textPane;


    public ConsoleIdeComponent(EditorFrame editorFrame) {
        this.editorFrame = editorFrame;
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

    @Override
    public int runProccess(String proccess) {
        appendToPane(textPane, "> " + proccess + "\n", Color.GRAY);
        return ProcessUtils.run(proccess, (process) -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
