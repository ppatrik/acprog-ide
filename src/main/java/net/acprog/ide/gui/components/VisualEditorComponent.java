package net.acprog.ide.gui.components;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class VisualEditorComponent implements Component {

    class VisualEditorLayout implements LayoutManager {

        private int minWidth = 0, minHeight = 0;

        @Override
        public void addLayoutComponent(String name, java.awt.Component comp) {

        }

        @Override
        public void removeLayoutComponent(java.awt.Component comp) {

        }

        private void setSizes(Container parent) {
            int nComps = parent.getComponentCount();
            Rectangle d = null;

            minWidth = 0;
            minHeight = 0;

            for (int i = 0; i < nComps; i++) {
                java.awt.Component c = parent.getComponent(i);
                if (c.isVisible()) {
                    d = c.getBounds();

                    minWidth = Math.max(minWidth, d.width + d.x);
                    minHeight = Math.max(minHeight, d.height + d.y);
                }
            }
        }

        /* Required by LayoutManager. */
        public Dimension preferredLayoutSize(Container parent) {
            Dimension dim = new Dimension(0, 0);
            int nComps = parent.getComponentCount();

            setSizes(parent);

            //Always add the container's insets!
            Insets insets = parent.getInsets();
            dim.width = minWidth + insets.left + insets.right;
            dim.height = minHeight + insets.top + insets.bottom;

            return dim;
        }

        /* Required by LayoutManager. */
        public Dimension minimumLayoutSize(Container parent) {
            Dimension dim = new Dimension(0, 0);
            int nComps = parent.getComponentCount();

            //Always add the container's insets!
            Insets insets = parent.getInsets();
            dim.width = minWidth
                    + insets.left + insets.right;
            dim.height = minHeight
                    + insets.top + insets.bottom;

            return dim;
        }

        /* Required by LayoutManager. */
        /*
         * This is called when the panel is first displayed,
         * and every time its size changes.
         * Note: You CAN'T assume preferredLayoutSize or
         * minimumLayoutSize will be called -- in the case
         * of applets, at least, they probably won't be.
         */
        public void layoutContainer(Container parent) {
            parent.setSize(minimumLayoutSize(parent));
        }

        public String toString() {
            String str = "";
            return getClass().getName() + "[]";
        }
    }

    protected JScrollPane scrollPane;

    public VisualEditorComponent() {
        InitializeComponents();
    }

    private void InitializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new VisualEditorLayout());

        // TODO zobraz si projekt a nacitat z neho komponenty

        ProjectComponent pc;
        net.acprog.builder.project.Component c;

        c = new net.acprog.builder.project.Component();
        c.setName("acp.nieco.pekne");
        c.setType("utils");
        c.setDescription("dgdfgfdgfgfdgdfgdgdfgdgdfgdf");
        pc = new ProjectComponent(c);

        panel.add(pc);

        scrollPane = new JScrollPane(panel);

        Insets insets = panel.getInsets();
        panel.setSize(300 + insets.left + insets.right,
                125 + insets.top + insets.bottom);

    }

    public JComponent render() {
        return scrollPane;
    }
}
