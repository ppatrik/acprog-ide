import javax.swing.*;

public class VisualEditorComponent implements Component {
    protected JTextField panel;

    public VisualEditorComponent() {
        InitializeComponents();
    }

    private void InitializeComponents() {
        panel = new JTextField();
        panel.setText("Ahoj svet, ja som vizualny editor");
    }

    public JComponent render() {
        return panel;
    }
}
