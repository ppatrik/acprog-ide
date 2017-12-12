package net.acprog.ide.gui.utils;

import net.acprog.ide.gui.MainFrame;

public class ConsoleIde implements ConsoleInterface {

    public static final ConsoleIde instance = new ConsoleIde();

    private ConsoleIde() {
    }

    @Override
    public void print(String s) {
        MainFrame.instance.console.print(s);
    }

    @Override
    public void println(String s) {
        MainFrame.instance.console.println(s);
    }

    @Override
    public void err(String s) {
        MainFrame.instance.console.err(s);
    }

    @Override
    public void errln(String s) {
        MainFrame.instance.console.errln(s);
    }
}
