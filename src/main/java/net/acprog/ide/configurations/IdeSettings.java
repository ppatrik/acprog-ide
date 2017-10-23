package net.acprog.ide.configurations;

import java.io.File;

public class IdeSettings {
    private static IdeSettings ourInstance = new IdeSettings();

    public static IdeSettings getInstance() {
        return ourInstance;
    }


    private boolean debugMode = true;
    private File arduinoLibraryFolder = new File("c:\\Users\\patrik\\Documents\\Arduino\\libraries\\");
    private File acprogModulesFolder = new File("c:\\_data\\skola\\diplomovka\\_projekt\\acprog-modules\\");
    private String arduinoCli = "c:\\Program Files (x86)\\Arduino\\arduino_debug.exe";

    private IdeSettings() {
        // TODO: nacitanie z konfiguracneho xml-ka
    }

    public File getAcprogModulesFolder() {
        return acprogModulesFolder;
    }

    public void setAcprogModulesFolder(File acprogModulesFolder) {
        this.acprogModulesFolder = acprogModulesFolder;
    }

    public File getArduinoLibraryFolder() {
        return arduinoLibraryFolder;
    }

    public void setArduinoLibraryFolder(File arduinoLibraryFolder) {
        this.arduinoLibraryFolder = arduinoLibraryFolder;
    }

    public boolean getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String getArduinoCli() {
        return arduinoCli;
    }

    public void setArduinoCli(String arduinoCli) {
        this.arduinoCli = arduinoCli;
    }
}
