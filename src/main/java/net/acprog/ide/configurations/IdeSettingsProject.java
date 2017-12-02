package net.acprog.ide.configurations;

import net.acprog.builder.utils.XmlUtils;
import org.w3c.dom.Element;

import java.io.File;

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
}
