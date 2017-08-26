package net.acprog.ide.configurations;

import net.acprog.builder.project.Project;
import net.acprog.builder.utils.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

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

    private void openProjectDefinition() {
        project = Project.loadFromFile(new File(projectFolder.getPath() + "\\" + projectFolder.getName() + ".xml"));
    }

    private void openProjectSource() throws FileNotFoundException {
        source = new File(projectFolder.getPath() + "\\" + projectFolder.getName() + ".ino");
        sourceString = FileUtils.readFile(source);
    }

    public boolean save() {
        try (Writer fw = new BufferedWriter(new FileWriter(source))) {
            fw.write(sourceString);
            fw.close();
            return true;
        } catch (Exception e) {
            return false;
        }
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
        System.out.println(sourceString);
        System.out.println("Ukladam novy text");
    }
}
