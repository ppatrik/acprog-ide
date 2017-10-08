package net.acprog.ide.gui.components;

import net.acprog.ide.gui.MainFrame;
import net.acprog.ide.utils.event.EventType;
import net.acprog.ide.utils.event.Observer;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

public class EditorIdeComponent implements IdeComponent {
    private final MainFrame mainFrame;

    protected RSyntaxTextArea textArea;
    protected RTextScrollPane sp;

    public EditorIdeComponent(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        InitializeComponents();

        textArea.setText(mainFrame.getIdeProject().getSourceString());

        this.mainFrame.getEventManager().registerObserver(EventType.EVENT_PROJECT_SAVE, new Observer() {
            @Override
            public void onEvent(EventType eventType, Object o) {
                mainFrame.getIdeProject().setSourceString(textArea.getText());
            }
        });
    }

    private void InitializeComponents() {
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        textArea.setCodeFoldingEnabled(true);

        sp = new RTextScrollPane(textArea);
    }

    public JComponent render() {
        return sp;
    }
}
