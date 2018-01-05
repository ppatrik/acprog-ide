package net.acprog.ide.gui.property;

import net.acprog.ide.gui.EditorFrame;
import sk.gbox.swing.propertiespanel.PropertiesPanel;
import sk.gbox.swing.propertiespanel.types.StringType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.EventObject;

public class AstType extends StringType {

    /**
     * Cell editor for strings.
     */
    private static class CellEditor extends AbstractCellEditor
            implements TableCellEditor, TreeCellEditor {

        /**
         * The Swing component being edited.
         */
        protected JTextField editorTextField;
        protected JButton editorButton;
        protected JPanel editorPanel;

        /**
         * The delegate class which handles all methods sent from the
         * <code>CellEditor</code>.
         */
        protected CellEditor.EditorDelegate delegate;

        /**
         * An integer specifying the number of clicks needed to start editing.
         * Even if <code>clickCountToStart</code> is defined as zero, it
         * will not initiate until a click occurs.
         */
        protected int clickCountToStart = 1;

        //
        //  Constructors
        //

        /**
         * Constructs a <code>DefaultCellEditor</code> that uses a text field.
         */
        @ConstructorProperties({"component"})
        public CellEditor() {
            editorPanel = new JPanel(new BorderLayout());
            editorButton = new JButton("+");
            editorTextField = new JTextField();
            editorPanel.add(editorTextField, BorderLayout.CENTER);
            editorPanel.add(editorButton, BorderLayout.EAST);

            editorButton.setMargin(new Insets(5, 5, 5, 5));
            editorPanel.setBackground(Color.WHITE);
            editorTextField.setHorizontalAlignment(10);
            editorTextField.setBorder(null);

            this.clickCountToStart = 2;
            delegate = new CellEditor.EditorDelegate() {
                public void setValue(Object value) {
                    editorTextField.setText((value != null) ? value.toString() : "");
                }

                public Object getCellEditorValue() {
                    return editorTextField.getText();
                }
            };
            editorTextField.addActionListener(delegate);

            editorButton.addActionListener(e -> {
                EditorFrame.instance.code.createOrFindMethod("componentOnAction");
            });
        }

        /**
         * Returns a reference to the editor component.
         *
         * @return the editor <code>Component</code>
         */
        public Component getComponent() {
            return editorPanel;
        }

        //
        //  Modifying
        //

        /**
         * Specifies the number of clicks needed to start editing.
         *
         * @param count an int specifying the number of clicks needed to start editing
         * @see #getClickCountToStart
         */
        public void setClickCountToStart(int count) {
            clickCountToStart = count;
        }

        /**
         * Returns the number of clicks needed to start editing.
         *
         * @return the number of clicks needed to start editing
         */
        public int getClickCountToStart() {
            return clickCountToStart;
        }

        //
        //  Override the implementations of the superclass, forwarding all methods
        //  from the CellEditor interface to our delegate.
        //

        /**
         * Forwards the message from the <code>CellEditor</code> to
         * the <code>delegate</code>.
         *
         * @see DefaultCellEditor.EditorDelegate#getCellEditorValue
         */
        public Object getCellEditorValue() {
            return delegate.getCellEditorValue();
        }

        /**
         * Forwards the message from the <code>CellEditor</code> to
         * the <code>delegate</code>.
         *
         * @see DefaultCellEditor.EditorDelegate#isCellEditable(EventObject)
         */
        public boolean isCellEditable(EventObject anEvent) {
            return delegate.isCellEditable(anEvent);
        }

        /**
         * Forwards the message from the <code>CellEditor</code> to
         * the <code>delegate</code>.
         *
         * @see DefaultCellEditor.EditorDelegate#shouldSelectCell(EventObject)
         */
        public boolean shouldSelectCell(EventObject anEvent) {
            return delegate.shouldSelectCell(anEvent);
        }

        /**
         * Forwards the message from the <code>CellEditor</code> to
         * the <code>delegate</code>.
         *
         * @see DefaultCellEditor.EditorDelegate#stopCellEditing
         */
        public boolean stopCellEditing() {
            return delegate.stopCellEditing();
        }

        /**
         * Forwards the message from the <code>CellEditor</code> to
         * the <code>delegate</code>.
         *
         * @see DefaultCellEditor.EditorDelegate#cancelCellEditing
         */
        public void cancelCellEditing() {
            delegate.cancelCellEditing();
        }

        //
        //  Implementing the TreeCellEditor Interface
        //

        /**
         * Implements the <code>TreeCellEditor</code> interface.
         */
        public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                    boolean isSelected,
                                                    boolean expanded,
                                                    boolean leaf, int row) {
            String stringValue = tree.convertValueToText(value, isSelected,
                    expanded, leaf, row, false);

            delegate.setValue(stringValue);
            return getComponent();
        }

        //
        //  Implementing the CellEditor Interface
        //

        /**
         * Implements the <code>TableCellEditor</code> interface.
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column) {
            delegate.setValue(value);
            return getComponent();
        }


//
//  Protected EditorDelegate class
//

        /**
         * The protected <code>EditorDelegate</code> class.
         */
        protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

            /**
             * The value of this cell.
             */
            protected Object value;

            /**
             * Returns the value of this cell.
             *
             * @return the value of this cell
             */
            public Object getCellEditorValue() {
                return value;
            }

            /**
             * Sets the value of this cell.
             *
             * @param value the new value of this cell
             */
            public void setValue(Object value) {
                this.value = value;
            }

            /**
             * Returns true if <code>anEvent</code> is <b>not</b> a
             * <code>MouseEvent</code>.  Otherwise, it returns true
             * if the necessary number of clicks have occurred, and
             * returns false otherwise.
             *
             * @param anEvent the event
             * @return true  if cell is ready for editing, false otherwise
             * @see #setClickCountToStart
             * @see #shouldSelectCell
             */
            public boolean isCellEditable(EventObject anEvent) {
                if (anEvent instanceof MouseEvent) {
                    return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
                }
                return true;
            }

            /**
             * Returns true to indicate that the editing cell may
             * be selected.
             *
             * @param anEvent the event
             * @return true
             * @see #isCellEditable
             */
            public boolean shouldSelectCell(EventObject anEvent) {
                return true;
            }

            /**
             * Returns true to indicate that editing has begun.
             *
             * @param anEvent the event
             */
            public boolean startCellEditing(EventObject anEvent) {
                return true;
            }

            /**
             * Stops editing and
             * returns true to indicate that editing has stopped.
             * This method calls <code>fireEditingStopped</code>.
             *
             * @return true
             */
            public boolean stopCellEditing() {
                fireEditingStopped();
                return true;
            }

            /**
             * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
             */
            public void cancelCellEditing() {
                fireEditingCanceled();
            }

            /**
             * When an action is performed, editing is ended.
             *
             * @param e the action event
             * @see #stopCellEditing
             */
            public void actionPerformed(ActionEvent e) {
                CellEditor.this.stopCellEditing();
            }

            /**
             * When an item's state changes, editing is ended.
             *
             * @param e the action event
             * @see #stopCellEditing
             */
            public void itemStateChanged(ItemEvent e) {
                CellEditor.this.stopCellEditing();
            }
        }

    }


    private static class CellRenderer extends DefaultTableCellRenderer {
        private CellRenderer() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        }
    }

    /**
     * Renderer for boolean values.
     */
    private static final AstType.CellRenderer renderer = new AstType.CellRenderer();

    /**
     * Editor for boolean values.
     */
    private static final AstType.CellEditor editor = new AstType.CellEditor();

    @Override
    public TableCellRenderer getValueRenderer(PropertiesPanel propertiesPanel) {
        return renderer;
    }

    @Override
    public TableCellEditor getValueEditor(PropertiesPanel propertiesPanel) {
        return editor;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isAssignableValue(Object value) {
        try {
            if (value == null) {
                return true;
            }

            if (value instanceof String) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object convertAssignableToValidValue(Object value) {
        try {
            if (value == null) {
                return null;
            }

            if (value instanceof String) {
                if (((String) value).isEmpty()) {
                    return null;
                }
                return value.toString();
            }

            throw new RuntimeException("Invalid value.");
        } catch (Exception e) {
            throw new RuntimeException("Invalid value.");
        }
    }
}
