package net.acprog.ide.configurations;

import net.acprog.builder.utils.XmlUtils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class IdeSettingsProject {
    private String projectDirectory;
    private String projektName;

    public IdeSettingsProject() {

    }

    public IdeSettingsProject(String projectDirectory, String projektName) {
        super();
        this.projectDirectory = projectDirectory;
        this.projektName = projektName;
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public String getProjektName() {
        return projektName;
    }

    @Override
    public int hashCode() {
        return projectDirectory.hashCode();
    }

    @Override
    public String toString() {
        return projectDirectory + " " + projektName;
    }

    public static IdeSettingsProject loadSettingsFromXml(Element element) {
        IdeSettingsProject result = new IdeSettingsProject();
        result.projektName = XmlUtils.getSimplePropertyValue(element, "name", null);
        result.projectDirectory = XmlUtils.getSimplePropertyValue(element, "directory", null);
        return result;
    }

    public IdeProject getIdeProject() {
        return IdeProject.openProject(new File(projectDirectory));
    }

    public static IdeSettingsProject fromFile(File fc) {
        IdeSettingsProject result = new IdeSettingsProject();
        result.projectDirectory = fc.getAbsolutePath();
        result.projektName = fc.getName();
        return result;
    }

    private static final String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<project platform=\"ArduinoUno\">\n" +
            "</project>\n";

    private static final String inoContent = "//----------------------------------------------------------------------\n" +
            "// Includes required to build the sketch (including ext. dependencies)\n" +
            "#include <{projectName}.h>\n" +
            "//----------------------------------------------------------------------\n" +
            "\n" +
            "//----------------------------------------------------------------------\n" +
            "// Summary of available objects:\n" +
            "//----------------------------------------------------------------------\n";

    private static void createFile(File file, String content) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.print(content);
        writer.close();
    }

    public static IdeSettingsProject createNewProject(File inThisDirectory, String newProjectName) throws Exception {
        if (!inThisDirectory.isDirectory()) {
            throw new Exception("inThisDirectory must be directory!");
        }
        File projectDirectory = new File(inThisDirectory.getAbsolutePath() + File.separator + newProjectName + File.separator);
        if (projectDirectory.exists()) {
            throw new Exception(projectDirectory.toString() + " must not exists!");
        }
        if (!projectDirectory.mkdir()) {
            throw new Exception("Cannot create project folder " + projectDirectory.toString() + "!");
        }

        createFile(new File(projectDirectory.getAbsoluteFile() + File.separator + newProjectName + ".xml"), xmlContent.replace("{projectName}", newProjectName));
        createFile(new File(projectDirectory.getAbsoluteFile() + File.separator + newProjectName + ".ino"), inoContent.replace("{projectName}", newProjectName));
        return new IdeSettingsProject(projectDirectory.getAbsolutePath(), newProjectName);
    }
}
