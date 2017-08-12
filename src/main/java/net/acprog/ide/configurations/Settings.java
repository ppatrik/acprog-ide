package net.acprog.ide.configurations;

import java.io.File;

public class Settings {
    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }


    private File acprogModulesFolder = new File("c:\\_data\\skola\\diplomovka\\_projekt\\acprog-modules\\acp\\");

    private Settings() {
    }

    public File getAcprogModulesFolder() {
        return acprogModulesFolder;
    }

    public void setAcprogModulesFolder(File acprogModulesFolder) {
        this.acprogModulesFolder = acprogModulesFolder;
    }
}
