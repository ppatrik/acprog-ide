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

public class Project extends net.acprog.builder.project.Project {


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
        if(getEepromItems().size() == 0) {
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

    public List<Component> getIdeComponents() {
        List<Component> list = new ArrayList<>(super.getComponents().size());
        for (net.acprog.builder.project.Component component : super.getComponents()) {
            list.add(new Component(component));
        }
        return list;
    }

    private Element writeComponents(Document doc, Element xmlComponents) {
        if (xmlComponents != null) {
            for (Component component : getIdeComponents()) {
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

        } catch (Exception e) {
            throw new ConfigurationException("Loading of project configuration failed.", e);
        }
        return true;
    }

    public static Project loadFromFile(File xmlFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            Project result = new Project();
            Element xmlRoot = doc.getDocumentElement();
            if (!"project".equals(xmlRoot.getNodeName())) {
                throw new ConfigurationException("Root element of a project configuration must be an element with name 'project'.");
            } else {
                result.readConfiguration(xmlRoot);
                return result;
            }
        } catch (Exception var6) {
            throw new ConfigurationException("Loading of project configuration failed.", var6);
        }
    }
}
