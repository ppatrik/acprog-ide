package net.acprog.ide.configurations;

import net.acprog.builder.components.ConfigurationException;
import net.acprog.builder.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {

    protected net.acprog.builder.project.Project parentProject;

    protected List<Component> components = new ArrayList<>();

    public Project(net.acprog.builder.project.Project parentProject) {
        this.parentProject = parentProject;
        for (net.acprog.builder.project.Component component : parentProject.getComponents()) {
            components.add(new Component(component));
        }
    }

    // --------------------------------------------------------------------------------------
    // GETTERS / SETTERS
    // --------------------------------------------------------------------------------------

    public List<Component> getComponents() {
        return this.components;
    }

    public Map<String, Component> getComponentsMap() {
        Map<String, Component> map = new HashMap<>();
        for (Component component : getComponents()) {
            map.put(component.getName(), component);
        }
        return map;
    }

    public void addComponent(Component component) {
        this.components.add(component);
        parentProject.getComponents().add(component.getParentComponent());
    }

    public Map<String, String> getProgramEvents() {
        return parentProject.getProgramEvents();
    }

    public List<String> getLibraryImports() {
        return parentProject.getLibraryImports();
    }

    public List<net.acprog.builder.project.EepromItem> getEepromItems() {
        return parentProject.getEepromItems();
    }

    public String getPlatformName() {
        return parentProject.getPlatformName();
    }

    public void setPlatformName(String platformName) {
        parentProject.setPlatformName(platformName);
    }

    public int getWatchdogLevel() {
        return parentProject.getWatchdogLevel();
    }

    public void setWatchdogLevel(int watchdogLevel) {
        parentProject.setWatchdogLevel(watchdogLevel);
    }

    public String getEepromLayoutVersion() {
        return parentProject.getEepromLayoutVersion();
    }

    public void setEepromLayoutVersion(String eepromLayoutVersion) {
        parentProject.setEepromLayoutVersion(eepromLayoutVersion);
    }

    // --------------------------------------------------------------------------------------
    // IDE CONFIGURATION READER
    // --------------------------------------------------------------------------------------

    private void readConfiguration(Element xmlRoot) {
        Map<String, Component> componentsMap = getComponentsMap();
        for (Element xmlGroup : XmlUtils.getChildElements(xmlRoot, "group")) {
            String type = XmlUtils.getSimpleAttributeValue(xmlGroup, "type", "component");
            String name = XmlUtils.getSimpleAttributeValue(xmlGroup, "name", null);
            switch (type) {
                case "component":
                    componentsMap.get(name).readFromXml(xmlGroup);
                    break;
                case "component-group":
                    break;
            }
        }
    }

    public static Project loadFromFile(File xmlFile) {
        net.acprog.builder.project.Project parentProject = net.acprog.builder.project.Project.loadFromFile(xmlFile);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            Project result = new Project(parentProject);
            Element xmlRoot = doc.getDocumentElement();
            Element ideXmlRoot = XmlUtils.getChildElement(xmlRoot, "ide");

            if (ideXmlRoot != null) {
                result.readConfiguration(ideXmlRoot);
            } else {
                // todo: urobit default konfiguraciu!
            }
            return result;
        } catch (Exception var6) {
            throw new ConfigurationException("Loading of project configuration failed.", var6);
        }
    }

    // --------------------------------------------------------------------------------------
    // IDE CONFIGURATION WRITER
    // --------------------------------------------------------------------------------------

    public boolean saveToFile(File xmlFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // Write actual configuration to root node
            Element xmlRoot = parentProject.writeConfiguration(doc.createElement("project"));
            doc.appendChild(xmlRoot);

            Element xmlIdeRoot = writeIdeConfiguration(doc.createElement("ide"));
            xmlRoot.appendChild(xmlIdeRoot);

            // Save configuration to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
            return true;
        } catch (Exception e) {
            throw new ConfigurationException("Loading of project configuration failed.", e);
        }
    }

    private Element writeIdeConfiguration(Element xmlIdeElement) {
        Document doc = xmlIdeElement.getOwnerDocument();
        int output = 0;
        for (Component component : getComponents()) {
            Element el = component.writeIdeConfiguration(doc.createElement("group"));
            xmlIdeElement.appendChild(el);
            output++;
        }
        return output == 0 ? null : xmlIdeElement;
    }

}
