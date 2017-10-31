package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.builder.components.Event;
import net.acprog.builder.components.PropertyType;
import net.acprog.builder.modules.ComponentType;
import net.acprog.builder.modules.Module;
import net.acprog.ide.configurations.Component;
import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.utils.ACPModules;
import net.acprog.ide.utils.event.EventType;
import sk.gbox.swing.propertiespanel.*;
import sk.gbox.swing.propertiespanel.types.StringType;

import javax.swing.*;
import java.util.Map;

public class PropertyEditorIdeComponent implements IdeComponent {
    private final MainFrame mainFrame;

    protected Component projectComponent;

    protected PropertiesPanel propertiesPanel;

    public PropertyEditorIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        InitializeComponents();

        addEventListeners();
    }

    public void onComponentSelected(EventType eventType, Object o) {
        projectComponent = (Component) o;
        setModelProperties();
    }

    private void addEventListeners() {
        mainFrame.getEventManager().registerObserver(EventType.COMPONENT_SELECTED, this::onComponentSelected);
    }

    private void InitializeComponents() {
        propertiesPanel = new PropertiesPanel();
    }

    private void setModelProperties() {
        propertiesPanel.setModel(null);
        Module module = ACPModules.getModule(projectComponent.getParentComponent());
        if (!(module instanceof ComponentType)) {
            return;
        }
        ComponentType componentType = (ComponentType) module;

        ComposedProperty mainProperty = new ComposedProperty();
        mainProperty.setLabel(module.getName());
        mainProperty.setHint(module.getDescription());
        ComposedProperty.PropertyList mainSubproperties = mainProperty.getSubproperties();

        SimpleProperty propertyName = new SimpleProperty(new StringType(), projectComponent.getName());
        propertyName.addPropertyListener(new PropertyListener() {
            @Override
            public void propertyChanged(Property property) {

            }

            @Override
            public void propertyValueChanged(Property property) {
                projectComponent.setName((String) property.getValue());
                mainFrame.getEventManager().callEvent(EventType.VISUAL_EDITOR_UPDATEUI);
            }

            @Override
            public void subpropertyListChanged(ComposedProperty composedProperty) {

            }
        });
        propertyName.setName("Názov komponentu");
        propertyName.setLabel("Názov komponentu");
        propertyName.setHint("Pod týmto názvom budete môcť pristupovať ku komponentu pri programovaní.");


        SimpleProperty propertyType = new SimpleProperty(new StringType(), projectComponent.getType());
        propertyType.setReadOnly(true);
        propertyType.setName("Typ komponentu");
        propertyType.setLabel("Typ komponentu");
        propertyType.setHint("Tu by mohol prist popis tohto typu komponentu.");


        ComposedProperty propertiesProperty = new ComposedProperty();
        propertiesProperty.setName("properties");
        propertiesProperty.setLabel("Properties");
        propertiesProperty.setHint(module.getName());
        ComposedProperty.PropertyList propertiesSubproperties = propertiesProperty.getSubproperties();
        initializeProperties(propertiesSubproperties, componentType);

        ComposedProperty eventsProperty = new ComposedProperty();
        eventsProperty.setName("events");
        eventsProperty.setLabel("Events");
        eventsProperty.setHint(module.getName());
        ComposedProperty.PropertyList eventsSubroperties = eventsProperty.getSubproperties();
        initializeEvents(eventsSubroperties, componentType);

        mainSubproperties.add(propertyName);
        mainSubproperties.add(propertyType);
        mainSubproperties.add(propertiesProperty);
        mainSubproperties.add(eventsProperty);

        // refresh property panela
        setPropertiesForEditor(mainProperty);
        propertiesPanel.setModel(mainProperty);
    }

    private void initializeProperties(ComposedProperty.PropertyList subproperties, ComponentType componentType) {
        Map<String, PropertyType> properties = componentType.getProperties();
        properties.forEach((name, property) -> {
            Property prop = new SimpleProperty(new StringType(), "");
            prop.setName(name);
            prop.setLabel(name);
            prop.setValue(projectComponent.getProperties().get(name));
            prop.addPropertyListener(new PropertyListener() {
                @Override
                public void propertyChanged(Property property) {

                }

                @Override
                public void propertyValueChanged(Property property) {
                    projectComponent.getProperties().put(name, (String) property.getValue());
                }

                @Override
                public void subpropertyListChanged(ComposedProperty composedProperty) {

                }
            });
            subproperties.add(prop);
        });
    }

    private void initializeEvents(ComposedProperty.PropertyList subproperties, ComponentType componentType) {
        Map<String, Event> events = componentType.getEvents();
        events.forEach((name, property) -> {
            Property prop = new SimpleProperty(new StringType(), "");
            prop.setName(name);
            prop.setLabel(name);
            prop.setHint(property.getDescription());
            prop.setValue(projectComponent.getEvents().get(name));
            prop.addPropertyListener(new PropertyListener() {
                @Override
                public void propertyChanged(Property property) {

                }

                @Override
                public void propertyValueChanged(Property property) {
                    projectComponent.getEvents().put(name, (String) property.getValue());
                }

                @Override
                public void subpropertyListChanged(ComposedProperty composedProperty) {

                }
            });
            subproperties.add(prop);
        });
    }

    public JComponent render() {
        return propertiesPanel;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Property editor", render());
    }

    public void setPropertiesForEditor(Property parentProperty) {

        if (parentProperty instanceof ComposedProperty) {
            for (Property prop : ((ComposedProperty) parentProperty).getSubproperties()) {
                setPropertiesForEditor(prop);
            }
        } else {
            switch (parentProperty.getName()) {
                case "name":
                    parentProperty.setValue((Object) projectComponent.getName());
                    break;
                case "type":
                    parentProperty.setValue((Object) projectComponent.getType());
                    break;
            }
        }
    }
}
