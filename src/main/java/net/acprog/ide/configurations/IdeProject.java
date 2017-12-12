package net.acprog.ide.configurations;

import net.acprog.builder.compilation.ACPCompiler;
import net.acprog.builder.compilation.CompilationException;
import net.acprog.builder.compilation.CompilationSettings;
import net.acprog.builder.components.ConfigurationException;
import net.acprog.builder.utils.FileUtils;
import net.acprog.ide.IdeException;
import net.acprog.ide.gui.utils.ConsoleIde;
import net.acprog.ide.gui.utils.ConsoleInterface;
import net.acprog.ide.gui.utils.ProcessUtils;

import java.io.*;

public class IdeProject {
    private static IdeProject ourInstance = null;

    public static IdeProject getInstance() {
        return ourInstance;
    }

    private File projectFolder = null;
    private Project project = null;
    private File source = null;
    private String sourceString = null;
    private boolean opened = false;

    private IdeProject(File projectFolder) throws IdeException {
        try {
            this.projectFolder = projectFolder;
            openProjectDefinition();
            openProjectSource();
            opened = true;
        } catch (FileNotFoundException e) {
            throw new IdeException("Error when opening project. Did you selected acprog project folder?");
        }
    }

    private File getProjectXmlFile() {
        return new File(projectFolder.getPath() + "\\" + projectFolder.getName() + ".xml");
    }

    private File getProjectInoFile() {
        return new File(projectFolder.getPath() + "\\" + projectFolder.getName() + ".ino");
    }

    private String getLibraryName() {
        return projectFolder.getName();
    }

    private void openProjectDefinition() throws IdeException {
        try {
            project = Project.loadFromFile(getProjectXmlFile());
        } catch (ConfigurationException e) {
            throw new IdeException(e.getMessage());
        }
    }

    private void openProjectSource() throws FileNotFoundException {
        source = getProjectInoFile();
        sourceString = FileUtils.readFile(source);
    }

    public boolean save(ConsoleInterface console) {
        console.println("Saving ino file.........");
        try (Writer fw = new BufferedWriter(new FileWriter(getProjectInoFile()))) {
            fw.write(sourceString);
            fw.close();
        } catch (Exception e) {
            console.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            console.println(e.getMessage());
            return false;
        }
        console.println("Saving xml file.........");
        project.saveToFile(getProjectXmlFile());
        return true;
    }

    public void close() {
        if (opened) {
            save(ConsoleIde.instance);
        }
    }

    public static void closeProject() {
        ourInstance.close();
        ourInstance = null;
    }

    public static IdeProject openProject(File projectFolder) throws IdeException {
        if (ourInstance != null) {
            closeProject();
        }
        ourInstance = new IdeProject(projectFolder);
        return ourInstance;
    }

    public File getSource() {
        return source;
    }

    public String getSourceString() {
        return sourceString;
    }

    public Project getProject() {
        return project;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    public void build(ConsoleInterface console, boolean clean) {
        File acpModulesDirectory = new File(IdeSettings.getInstance().getAcprogModulesFolder());
        File arduinoLibraryDirectory = new File(IdeSettings.getInstance().getArduinoLibraryFolder());

        File projectFile = getProjectXmlFile();

        // Clean (if required)
        if (clean) {
            File libraryDir = new File(arduinoLibraryDirectory, getLibraryName());
            FileUtils.removeDirectory(libraryDir);
        }

        // Build
        try {
            console.println("Zaciatok kompilacie");
            ACPCompiler compiler = new ACPCompiler(acpModulesDirectory);
            CompilationSettings settings = new CompilationSettings();
            settings.setProjectConfigurationFile(projectFile);
            settings.setLibraryName(getLibraryName());
            settings.setOutputLibraryPath(arduinoLibraryDirectory);
            settings.setDebugMode(IdeSettings.getInstance().getDebugMode());
            console.println("Koniec nastaveni");
            compiler.compile(settings);
            console.println("Hotovo");
        } catch (CompilationException e) {
            console.errln(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verify(ConsoleInterface console) {
        String proccess = IdeSettings.getInstance().getArduinoCli() + " --pref build.path=" + getProjectInoFile().getParentFile().getPath() + "\\build" + " --verify " + getProjectInoFile();
        int ret = ProcessUtils.run(proccess, (process) -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    console.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if (ret == 0) {
            console.println("Hotovo");
        } else {
            console.println("Chyba, skontrolujte konzolu pre viac informácií.");
        }
    }
}
