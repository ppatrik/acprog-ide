package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.gui.EditorFrame;
import net.acprog.ide.gui.groupview.GroupView;
import net.acprog.ide.project.ComponentInterface;
import net.acprog.ide.project.ProjectProxy;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class VisualGroupEditorIdeComponent implements IdeComponent {
    private final EditorFrame editorFrame;

    protected JComponent renderView;

    public VisualGroupEditorIdeComponent(EditorFrame editorFrame) {
        this.editorFrame = editorFrame;

        InitializeComponents();
    }

    private void InitializeComponents() {
        Map<ProjectProxy.Group, List<ComponentInterface>> groups = IdeProject.getInstance().getProject().getComponents();

        GroupView<ProjectProxy.Group, ComponentInterface> groupView = new GroupView<>();
        groupView.setModel(groups);

        renderView = new JScrollPane(groupView);
        ((JScrollPane) renderView).getVerticalScrollBar().setUnitIncrement(100);
        renderView.setBackground(Color.WHITE);
    }

    public JComponent render() {
        return renderView;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Project components editor", render());
    }

    public EditorFrame getEditorFrame() {
        return editorFrame;
    }


}
