package net.acprog.ide.configurations;

import java.io.File;

public class IdeSettings {
    private static IdeSettings ourInstance = new IdeSettings();

    public static IdeSettings getInstance() {
        return ourInstance;
    }


    private File acprogModulesFolder = new File("c:\\_data\\skola\\diplomovka\\_projekt\\acprog-modules\\acp\\");

    private IdeSettings() {
    }

    public File getAcprogModulesFolder() {
        return acprogModulesFolder;
    }

    public void setAcprogModulesFolder(File acprogModulesFolder) {
        this.acprogModulesFolder = acprogModulesFolder;
    }
}
