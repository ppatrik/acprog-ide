import javax.swing.*;

public class ToolBoxComponent implements Component {
    protected JTextField panel;

    public ToolBoxComponent() {
        InitializeComponents();
    }

    private void InitializeComponents() {
        panel = new JTextField();
        panel.setText("Ahoj svet, ja som toolbox");
    }

    public JComponent render() {
        return panel;
    }
}
