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
import java.util.List;

public class IdeSettings {
    private static final String SETTINGS_FILENAME = "acp_settings.xml";
    private static final String XML_ROOT_ELEMENT = "ide-settings";
    private static final int MAX_LAST_PROJECTS = 10;

    private static IdeSettings ourInstance = new IdeSettings();

    public static IdeSettings getInstance() {
        return ourInstance;
    }


    private boolean initializedEmpty = true;

    private boolean debugMode;
    private String serialPort = null;
    private String arduinoLibraryFolder;
    private String acprogModulesFolder;
    private String arduinoCli;
    private List<IdeSettingsProject> lastProjects;

    private IdeSettings() {
        loadSettingsFromFile();
    }

    private File getSettingsXmlFile() {
        File f = new File(SETTINGS_FILENAME);
        if (!f.exists()) {
            initializedEmpty = false;
            saveSettingsToFile(f);
        }
        return f;
    }

    private void loadSettingsFromFile() {
        File xmlFile = getSettingsXmlFile();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            Element xmlRoot = doc.getDocumentElement();
            Element ideXmlRoot = XmlUtils.getChildElement(xmlRoot, XML_ROOT_ELEMENT);
            acprogModulesFolder = XmlUtils.getChildElement(xmlRoot, "acprog-modules-folder").getTextContent();
            arduinoCli = XmlUtils.getChildElement(xmlRoot, "arduino-cli").getTextContent();
            arduinoLibraryFolder = XmlUtils.getChildElement(xmlRoot, "arduino-library-folder").getTextContent();
            try {
                serialPort = XmlUtils.getChildElement(xmlRoot, "serial-port").getTextContent();
            } catch (Exception e) {
                serialPort = null;
            }
            debugMode = "true".equals(XmlUtils.getChildElement(xmlRoot, "debug-mode").getTextContent().toLowerCase());
            lastProjects = new ArrayList<>();
            Element lastProjectElementRoot = XmlUtils.getChildElement(xmlRoot, "last-projects");
            if (lastProjectElementRoot != null) {
                List<Element> elements = XmlUtils.getChildElements(lastProjectElementRoot, "project");
                if (elements != null) {
                    for (Element element : elements) {
                        lastProjects.add(IdeSettingsProject.loadSettingsFromXml(element));
                    }
                }
            }

        } catch (Exception e) {
            throw new ConfigurationException("Loading of project configuration failed.", e);
        }
    }

    public void saveSettingsToFile() {
        saveSettingsToFile(getSettingsXmlFile());
    }

    private void saveSettingsToFile(File xmlFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // Write actual configuration to root node
            Element xmlRoot = doc.createElement(XML_ROOT_ELEMENT);
            doc.appendChild(xmlRoot);

            Element el;
            el = doc.createElement("acprog-modules-folder");
            el.setTextContent(acprogModulesFolder);
            xmlRoot.appendChild(el);

            el = doc.createElement("arduino-cli");
            el.setTextContent(arduinoCli);
            xmlRoot.appendChild(el);

            el = doc.createElement("arduino-library-folder");
            el.setTextContent(arduinoLibraryFolder);
            xmlRoot.appendChild(el);

            el = doc.createElement("serial-port");
            el.setTextContent(serialPort);
            xmlRoot.appendChild(el);

            el = doc.createElement("debug-mode");
            el.setTextContent(debugMode ? "true" : "false");
            xmlRoot.appendChild(el);

            el = doc.createElement("last-projects");
            for (IdeSettingsProject project : lastProjects) {
                el.appendChild(project.saveToXml(doc));
            }
            xmlRoot.appendChild(el);

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
    }

    public String getAcprogModulesFolder() {
        return acprogModulesFolder;
    }

    public void setAcprogModulesFolder(String acprogModulesFolder) {
        this.acprogModulesFolder = acprogModulesFolder;
    }

    public String getArduinoLibraryFolder() {
        return arduinoLibraryFolder;
    }

    public void setArduinoLibraryFolder(String arduinoLibraryFolder) {
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

    public boolean isInitializedEmpty() {
        return initializedEmpty;
    }

    public IdeSettingsProject getLatestProject() {
        if (lastProjects.size() >= 1) {
            return lastProjects.get(lastProjects.size() - 1);
        }
        return null;
    }

    public void addLastProject(IdeSettingsProject ideSettingsProject) {
        lastProjects.remove(ideSettingsProject);
        lastProjects.add(ideSettingsProject);
        if (lastProjects.size() > MAX_LAST_PROJECTS) {
            for (int i = 0; i < lastProjects.size() - MAX_LAST_PROJECTS; i++) {
                lastProjects.remove(0);
            }
        }
        saveSettingsToFile();
    }

    public String getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }
}
