package net.acprog.ide.gui.components;

import net.acprog.builder.modules.Module;
import net.acprog.ide.utils.event.EventType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBoxComponent extends JButton implements ActionListener {
    private final ToolBoxIdeComponent toolBoxIdeComponent;
    private final Module module;

    public ToolBoxComponent(ToolBoxIdeComponent toolBoxIdeComponent, Module module) {
        this.toolBoxIdeComponent = toolBoxIdeComponent;
        this.module = module;
        setText(module.getName());
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ToolBoxComponent source = (ToolBoxComponent) e.getSource();
        Module module = source.getModule();
        toolBoxIdeComponent.getMainFrame().getEventManager().callEvent(EventType.COMPONENT_CREATE, module);
    }

    public Module getModule() {
        return module;
    }
}
