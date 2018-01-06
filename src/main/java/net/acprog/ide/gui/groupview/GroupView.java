package net.acprog.ide.gui.groupview;

import net.acprog.ide.gui.EditorFrame;
import net.acprog.ide.utils.event.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupView<G, I> extends JPanel {

    Map<G, List<I>> model;

    private Map<G, GroupSection<G, I>> sections = new LinkedHashMap<>();

    public GroupView() {

        setBackground(Color.WHITE);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setFocusable(false);

        revalidate();

        EditorFrame.instance.getEventManager().registerObserver(EventType.PROJECT_CHANGED, (eventType, o) -> {
            populateModelData();
        });
    }

    public void addSection(GroupSection<G, I> newSection, boolean expanded) {
        if (sections.containsKey(newSection.getGroup())) {
            throw new IllegalArgumentException("Sekcia uz bola vlozená skôr.");
        }
        sections.put(newSection.getGroup(), newSection);
        add(newSection);
        newSection.setExpanded(expanded);
    }

    public void setModel(Map<G, List<I>> model) {
        this.model = model;
        populateModelData();
    }

    public void populateModelData() {
        for (Map.Entry<G, List<I>> entry : model.entrySet()) {
            if (!sections.containsKey(entry.getKey())) {
                GroupSection<G, I> section = new GroupSection<G, I>(this, entry.getKey(), entry.getValue());
                addSection(section, true);
            }
        }
        revalidate();
    }

    public void clearSelection(Component exceptSection) {
        for (Component components : getComponents()) {
            GroupSection<G, I> groupSection = (GroupSection<G, I>) components;
            if (groupSection != exceptSection) {
                groupSection.clearSelection();
            }
        }
    }


}
