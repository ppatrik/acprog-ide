package net.acprog.ide.configurations;

import net.acprog.builder.compilation.ACPCompiler;
import net.acprog.builder.compilation.CompilationSettings;
import net.acprog.builder.utils.FileUtils;
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

    private IdeProject(File projectFolder) {
        try {
            this.projectFolder = projectFolder;
            openProjectDefinition();
            openProjectSource();
        } catch (FileNotFoundException e) {
            // todo: project not found exception
            e.printStackTrace();
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

    private void openProjectDefinition() {
        project = Project.loadFromFile(getProjectXmlFile());
    }

    private void openProjectSource() throws FileNotFoundException {
        source = getProjectInoFile();
        sourceString = FileUtils.readFile(source);
    }

    public boolean save() {
        try (Writer fw = new BufferedWriter(new FileWriter(getProjectInoFile()))) {
            fw.write(sourceString);
            fw.close();
        } catch (Exception e) {
            return false;
        }
        project.saveToFile(getProjectXmlFile());
        return true;
    }

    public void close() {
        save();
    }

    public static void closeProject() {
        ourInstance.close();
        ourInstance = null;
    }

    public static IdeProject openProject(File projectFolder) {
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

    public void build(boolean clean) {
        File acpModulesDirectory = IdeSettings.getInstance().getAcprogModulesFolder();
        File arduinoLibraryDirectory = IdeSettings.getInstance().getArduinoLibraryFolder();

        File projectFile = getProjectXmlFile();

        // Clean (if required)
        if (clean) {
            File libraryDir = new File(arduinoLibraryDirectory, getLibraryName());
            FileUtils.removeDirectory(libraryDir);
        }

        // Build
        try {
            System.out.println("Zaciatok kompilacie");
            ACPCompiler compiler = new ACPCompiler(acpModulesDirectory);
            CompilationSettings settings = new CompilationSettings();
            settings.setProjectConfigurationFile(projectFile);
            settings.setLibraryName(getLibraryName());
            settings.setOutputLibraryPath(arduinoLibraryDirectory);
            settings.setDebugMode(IdeSettings.getInstance().getDebugMode());
            System.out.println("Koniec nastaveni");
            compiler.compile(settings);
            System.out.println("Hotovo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verify() {
        String proccess = IdeSettings.getInstance().getArduinoCli() + " --pref build.path=" + getProjectInoFile().getParentFile().getPath() + "\\build" + " --verify " + getProjectInoFile();
        int ret = ProcessUtils.run(proccess, (process) -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if(ret == 0) {
            System.out.println("Hotovo");
        } else{
            System.out.println("Chyba, skontrolujte konzolu pre viac informácií.");
        }
    }
}
