package net.acprog.ide.configurations;

import net.acprog.builder.components.ConfigurationException;
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
import java.util.List;
import java.util.Map;

public class Project {

    protected net.acprog.builder.project.Project parentProject;

    protected List<Component> components = new ArrayList<>();

    public Project(net.acprog.builder.project.Project parentProject) {
        this.parentProject = parentProject;
        for (net.acprog.builder.project.Component component: parentProject.getComponents()) {
            components.add(new Component(component));
        }
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public void addComponent(Component component)
    {
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

    public static Project loadFromFile(File xmlFile) {
        net.acprog.builder.project.Project parentProject = net.acprog.builder.project.Project.loadFromFile(xmlFile);
        return new Project(parentProject);
    }


    private Element writeProgramConfiguration(Document doc, Element xmlProgram) throws ConfigurationException {
        boolean empty = true;
        if (xmlProgram != null) {
            if (this.getWatchdogLevel() != -1) { // todo magicka konstanta
                xmlProgram.setAttribute("watchdog-level", Integer.toString(getWatchdogLevel()));
                empty = false;
            }
            Element xmlEvents = writeEventProgramConfiguration(doc, doc.createElement("events"));
            if (xmlEvents != null) {
                xmlProgram.appendChild(xmlEvents);
                empty = false;
            }
            Element xmlImports = writeImportsProgramConfiguration(doc, doc.createElement("imports"));
            if (xmlImports != null) {
                xmlProgram.appendChild(xmlImports);
                empty = false;
            }
        }
        if (empty) {
            return null;
        }
        return xmlProgram;
    }

    private Element writeImportsProgramConfiguration(Document doc, Element xmlImports) {
        if (getLibraryImports().size() == 0) {
            return null;
        }
        for (String libraryImport : getLibraryImports()) {
            Element xmlImport = doc.createElement("library");
            xmlImport.setTextContent(libraryImport);
            xmlImports.appendChild(xmlImport);
        }
        return xmlImports;
    }

    private Element writeEventProgramConfiguration(Document doc, Element xmlEvents) {
        if (getProgramEvents().size() == 0) {
            return null;
        }
        for (Map.Entry<String, String> entry : getProgramEvents().entrySet()) {
            Element xmlEvent = doc.createElement("event");
            xmlEvent.setAttribute("name", entry.getKey());
            xmlEvent.setTextContent(entry.getValue());
            xmlEvents.appendChild(xmlEvent);
        }
        return xmlEvents;
    }

    private Element writeEepromConfiguration(Document doc, Element xmlEeproms) throws ConfigurationException {
        if (getEepromItems().size() == 0) {
            return null;
        }
        if (xmlEeproms != null) {
            for (net.acprog.builder.project.EepromItem eepromItem : getEepromItems()) {
                EepromItem eepromItemLocal = new EepromItem(eepromItem);
                Element xmlEeprom = eepromItemLocal.writeToXml(doc);
                xmlEeproms.appendChild(xmlEeprom);
            }
        }
        return xmlEeproms;
    }

    private Element writeComponents(Document doc, Element xmlComponents) {
        if (xmlComponents != null) {
            for (Component component : getComponents()) {
                Element xmlComponent = doc.createElement("component");
                component.saveToXml(doc, xmlComponent);
                xmlComponents.appendChild(xmlComponent);
            }
        }
        return xmlComponents;
    }

    public Element writeConfiguration(Document doc, Element xmlProject) throws ConfigurationException {
        xmlProject.setAttribute("platform", this.getPlatformName());

        Element xmlProgram = writeProgramConfiguration(doc, doc.createElement("program"));
        if (xmlProgram != null) {
            xmlProject.appendChild(xmlProgram);
        }
        Element xmlEeprom = writeEepromConfiguration(doc, doc.createElement("eeprom"));
        if (xmlEeprom != null) {
            xmlProject.appendChild(xmlEeprom);
        }
        xmlProject.appendChild(writeComponents(doc, doc.createElement("components")));
        return xmlProject;
    }

    public boolean saveToFile(File xmlFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // Write actual configuration to root node
            Element xmlRoot = writeConfiguration(doc, doc.createElement("project"));
            doc.appendChild(xmlRoot);

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
}
