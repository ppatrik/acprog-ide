package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;
import net.acprog.ide.gui.MainFrame;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class ConsoleIdeComponent implements IdeComponent, Runnable {
    private final MainFrame mainFrame;

    private JScrollPane panel;
    private JTextArea textArea;
    //private Thread reader;
    //private Thread reader2;
    private boolean quit;

    //private final PipedInputStream pin = new PipedInputStream();
    //private final PipedInputStream pin2 = new PipedInputStream();
    //Thread errorThrower;


    public ConsoleIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        InitializeComponents();
    }

    private void InitializeComponents() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel = new JScrollPane(textArea);
        redirect();
    }

    public JComponent render() {
        return panel;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Console", render());
    }


    private void redirect() {
        /*try {
            PipedOutputStream pout = new PipedOutputStream(pin);
            System.setOut(new PrintStream(pout, true));
        } catch (java.io.IOException io) {
            textArea.append("Couldn't redirect STDOUT to this console\n" + io.getMessage());
        } catch (SecurityException se) {
            textArea.append("Couldn't redirect STDOUT to this console\n" + se.getMessage());
        }

        try {
            PipedOutputStream pout2 = new PipedOutputStream(pin2);
            System.setErr(new PrintStream(pout2, true));
        } catch (java.io.IOException io) {
            textArea.append("Couldn't redirect STDERR to this console\n" + io.getMessage());
        } catch (SecurityException se) {
            textArea.append("Couldn't redirect STDERR to this console\n" + se.getMessage());
        }*/

        quit = false; // signals the Threads that they should exit

        // Starting two seperate threads to read from the PipedInputStreams
        //

        /*reader = new Thread(this);
        reader.setDaemon(true);
        reader.start();
        reader2 = new Thread(this);
        reader2.setDaemon(true);
        reader2.start();
        errorThrower = new Thread(this);
        errorThrower.setDaemon(true);
        errorThrower.start();*/
    }

    public synchronized void appendRow(String string)
    {
        textArea.append(string);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public synchronized void run() {
        /*try {
            while (Thread.currentThread() == reader) {
                try {
                    this.wait(100);
                } catch (InterruptedException ie) {
                }
                if (pin.available() != 0) {
                    appendRow(this.readLine(pin));
                }
                if (quit) return;
            }

            while (Thread.currentThread() == reader2) {
                try {
                    this.wait(100);
                } catch (InterruptedException ie) {
                }
                if (pin2.available() != 0) {
                    appendRow(this.readLine(pin2));
                }
                if (quit) return;
            }
        } catch (Exception e) {
            appendRow("\nConsole reports an Internal error.");
            appendRow("The error is: " + e);
        }*/
    }


    protected synchronized String readLine(PipedInputStream in) throws IOException {
        String input = "";
        do {
            int available = in.available();
            if (available == 0) break;
            byte b[] = new byte[available];
            in.read(b);
            input = input + new String(b, 0, b.length);
        } while (!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
        return input;
    }

    @Override
    protected void finalize() throws Throwable {
        quit = true;
        /*this.notifyAll(); // stop all threads
        try {
            reader.join(1000);
            pin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader2.join(1000);
            pin2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.finalize();
    }

}
