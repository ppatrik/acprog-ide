package net.acprog.ide.gui.components;

import net.acprog.ide.utils.event.EventType;
import net.acprog.ide.configurations.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProjectComponent extends JButton {

    protected Component projectComponent;
    protected VisualEditorIdeComponent visualEditorIdeComponent;

    /**
     * If sets <b>TRUE</b> this component is draggable
     */
    private boolean draggable = true;

    /**
     * 2D Point representing the coordinate where mouse is, relative parent container
     */
    protected Point anchorPoint;

    /**
     * Default mouse cursor for dragging action
     */
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    /**
     * If sets <b>TRUE</b> when dragging component,
     * it will be painted over each other (z-Buffer change)
     */
    protected boolean overbearing = false;

    public ProjectComponent(VisualEditorIdeComponent visualEditorIdeComponent, Component projectComponent) {
        super();

        this.visualEditorIdeComponent = visualEditorIdeComponent;
        this.projectComponent = projectComponent;

        updateUI();

        addDragListeners();

        addClickListeners();
    }

    private void addClickListeners() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualEditorIdeComponent.getMainFrame().getEventManager().callEvent(EventType.EVENT_COMPONENT_SELECTED, (Object) projectComponent);
            }
        });
    }

    /**
     * Add Mouse Motion Listener with drag function
     */
    private void addDragListeners() {
        /** This handle is a reference to THIS because in next Mouse Adapter
         "this" is not allowed */
        final ProjectComponent handle = this;
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x -
                        anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                projectComponent.setLeft(mouseOnScreen.x - parentOnScreen.x - anchorX);
                projectComponent.setTop(mouseOnScreen.y - parentOnScreen.y - anchorY);
                setLocation(position);

                //Change Z-Buffer if it is "overbearing"
                if (overbearing) {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
            }
        });
    }

    @Override
    public void updateUI() {
        if (projectComponent != null) {
            setText(projectComponent.getName());
            setBounds(projectComponent.getLeft(), projectComponent.getTop(), projectComponent.getWidth(), projectComponent.getHeight());
        }
        super.updateUI();
    }
}
