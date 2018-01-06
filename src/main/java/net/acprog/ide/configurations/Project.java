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
import java.util.*;

public class Project {

    private final Group undefinedGroup = new Group("undefined group");

    public class Group {
        public String name;
        public boolean expanded;

        public Group() {
        }

        public Group(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    protected net.acprog.builder.project.Project parentProject;

    protected Map<Project.Group, List<Component>> components = new LinkedHashMap<>();

    public Project(net.acprog.builder.project.Project parentProject) {
        this.parentProject = parentProject;
    }

    // --------------------------------------------------------------------------------------
    // GETTERS / SETTERS
    // --------------------------------------------------------------------------------------

    public Map<Group, List<Component>> getComponents() {
        return components;
    }

    public Map<String, Component> getComponentsMap() {
        Map<String, Component> map = new HashMap<>();

        List<Component> components = new ArrayList<>();
        for (net.acprog.builder.project.Component component : parentProject.getComponents()) {
            components.add(new Component(component));
        }
        for (Component component : components) {
            map.put(component.getName(), component);
        }

        return map;
    }

    public void addComponent(Component component, Group group) {
        if (group == null) {
            group = undefinedGroup;
        }
        if (!components.containsKey(group)) {
            components.put(group, new ArrayList<>());
        }
        List<Component> groupComponents = components.get(group);
        groupComponents.add(component);
        parentProject.getComponents().add(component.getParentComponent());
    }

    public void moveComponent(Component component, Group newGroup) {
        removeComponent(component);
        addComponent(component, newGroup);
    }

    public void removeComponent(Component component) {
        for (Map.Entry<Group, List<Component>> entry : components.entrySet()) {
            entry.getValue().remove(component);
        }
        parentProject.getComponents().remove(component.getParentComponent());
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
        components = new LinkedHashMap<>();

        Map<String, Component> componentsMap = getComponentsMap();
        Map<String, Boolean> selectedComponents = new LinkedHashMap<>();

        Element groupsWrapper = XmlUtils.getChildElement(xmlRoot, "groups");
        for (Element xmlGroup : XmlUtils.getChildElements(groupsWrapper, "group")) {
            Group newGroup = new Group();
            newGroup.name = XmlUtils.getSimpleAttributeValue(xmlGroup, "name", null);
            newGroup.expanded = "true".equals(XmlUtils.getSimpleAttributeValue(xmlGroup, "expanded", "true"));
            List<Component> newGroupComponentsList = new ArrayList<>();
            for (Element groupComponent : XmlUtils.getChildElements(xmlGroup, "component")) {
                String componentName = XmlUtils.getElementValue(groupComponent, null);
                Component component = componentsMap.getOrDefault(componentName, null);
                if (component != null) {
                    newGroupComponentsList.add(component);
                    selectedComponents.put(componentName, true);
                }
            }
            components.put(newGroup, newGroupComponentsList);
        }

        List<Component> newGroupComponentsList = new ArrayList<>();
        for (Map.Entry<String, Component> entry : componentsMap.entrySet()) {
            if (!selectedComponents.containsKey(entry.getKey())) {
                newGroupComponentsList.add(entry.getValue());
            }
        }
        if (newGroupComponentsList.size() != 0) {
            components.put(undefinedGroup, newGroupComponentsList);
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
        Element groupsElement = doc.createElement("groups");
        for (Map.Entry<Group, List<Component>> entry : getComponents().entrySet()) {
            if (undefinedGroup.equals(entry.getKey())) {
                continue;
            }
            Element groupElement = doc.createElement("group");
            groupElement.setAttribute("name", entry.getKey().name);
            groupElement.setAttribute("expanded", Boolean.toString(entry.getKey().expanded));
            for (Component component : entry.getValue()) {
                Element componentElement = doc.createElement("component");
                componentElement.setTextContent(component.getName());
                groupElement.appendChild(componentElement);
            }
            groupsElement.appendChild(groupElement);
            output++;
        }
        xmlIdeElement.appendChild(groupsElement);
        return output == 0 ? null : xmlIdeElement;
    }

}
