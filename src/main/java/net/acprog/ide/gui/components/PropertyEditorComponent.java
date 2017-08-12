package net.acprog.ide.gui.components;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import sk.gbox.swing.propertiespanel.ComposedProperty;
import sk.gbox.swing.propertiespanel.PropertiesPanel;
import sk.gbox.swing.propertiespanel.Property;
import sk.gbox.swing.propertiespanel.XmlPropertyBuilder;
import sk.gbox.swing.propertiespanel.types.DefaultPropertyTypeResolver;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class PropertyEditorComponent implements Component {
    protected PropertiesPanel propertiesPanel;

    public PropertyEditorComponent() {
        InitializeComponents();
        setModelFromXml();
    }

    private void InitializeComponents() {
        propertiesPanel = new PropertiesPanel();

        XmlPropertyBuilder builder = new XmlPropertyBuilder();
        Property property = new ComposedProperty();
        propertiesPanel.setModel((ComposedProperty) property);
    }

    private void setModelFromXml() {
        try {
            DocumentBuilder domParser = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            String xml = "<?xml version=\"1.0\"?>\n" +
                    "<properties label=\"Demo properties\"\n" +
                    "\thint=\"Properties created according to xml configuration.\">\n" +
                    "\t<property name=\"properties\">\n" +
                    "\t\t<label>Properties</label>\n" +
                    "\t\t<hint>Basic configuration properties.</hint>\n" +
                    "\t\t<subproperties>\n" +
                    "\t\t\t<property name=\"name\" type=\"String\">\n" +
                    "\t\t\t\t<label>name</label>\n" +
                    "\t\t\t\t<hint title=\"properties.name\">Name of component.</hint>\n" +
                    "\t\t\t</property>\n" +
                    "\t\t\t<property name=\"enabled\" type=\"Boolean\">\n" +
                    "\t\t\t\t<label>enabled</label>\n" +
                    "\t\t\t\t<hint title=\"properties.enabled\">Indicates whether something is enabled.</hint>\n" +
                    "\t\t\t</property>\n" +
                    "\t\t\t<property name=\"count\">\n" +
                    "\t\t\t\t<label>count</label>\n" +
                    "\t\t\t\t<hint>Count of something</hint>\n" +
                    "\t\t\t\t<type name=\"Integer\">\n" +
                    "\t\t\t\t\t<parameter name=\"minValue\" value=\"0\" />\n" +
                    "\t\t\t\t\t<parameter name=\"maxValue\" value=\"10\" />\n" +
                    "\t\t\t\t\t<parameter name=\"nullable\" value=\"false\" />\n" +
                    "\t\t\t\t</type>\n" +
                    "\t\t\t</property>\n" +
                    "\t\t\t<property name=\"type\">\n" +
                    "\t\t\t\t<label>type</label>\n" +
                    "\t\t\t\t<hint>Type of something</hint>\n" +
                    "\t\t\t\t<type name=\"Enumeration\">\n" +
                    "\t\t\t\t\t<map name=\"items\">\n" +
                    "\t\t\t\t\t\t<item key=\"TypeA\">Type A</item>\n" +
                    "\t\t\t\t\t\t<item key=\"TypeB\">Type B</item>\n" +
                    "\t\t\t\t\t\t<item key=\"TypeC\">Type C</item>\n" +
                    "\t\t\t\t\t</map>\n" +
                    "\t\t\t\t</type>\n" +
                    "\t\t\t</property>\n" +
                    "\t\t</subproperties>\n" +
                    "\t</property>\n" +
                    "</properties>";
            Document doc = domParser
                    .parse(new InputSource(new StringReader(xml)));

            XmlPropertyBuilder builder = new XmlPropertyBuilder();
            builder.setDefaultPropertyTypeResolver(new DefaultPropertyTypeResolver());

            Property property = builder.createProperties(doc);
            if (property instanceof ComposedProperty) {
                propertiesPanel.setModel((ComposedProperty) property);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public JComponent render() {
        return propertiesPanel;
    }
}
