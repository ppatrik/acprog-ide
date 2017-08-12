package net.acprog.ide.gui.components;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

public class EditorComponent implements Component {
    protected RSyntaxTextArea textArea;
    protected RTextScrollPane sp;

    public EditorComponent() {
        InitializeComponents();

        textArea.setText("//----------------------------------------------------------------------\n" +
                "// Includes required to build the sketch (including ext. dependencies)\n" +
                "#include <GEPEcho.h>\n" +
                "//----------------------------------------------------------------------\n" +
                "\n" +
                "//----------------------------------------------------------------------\n" +
                "// Summary of available objects:\n" +
                "// messenger (acp.messenger.gep_stream_messenger)\n" +
                "//----------------------------------------------------------------------\n" +
                "\n" +
                "//----------------------------------------------------------------------\n" +
                "// Event callback for Program.OnStart\n" +
                "void onStart() {\n" +
                "  Serial.begin(9600);\n" +
                "  messenger.setStream(Serial);\n" +
                "}\n" +
                "\n" +
                "//----------------------------------------------------------------------\n" +
                "// Event callback for messenger.OnMessageReceived\n" +
                "void onMessageReceived(const char* message, int messageLength, long messageTag) {\n" +
                "  char reply[30];\n" +
                "  \n" +
                "  // Consider at most 15 byte from beginning of the received message\n" +
                "  if (messageLength > 15) {\n" +
                "    messageLength = 15;\n" +
                "  }\n" +
                "  \n" +
                "  // Duplicate chars (bytes)\n" +
                "  for (int i=0; i<messageLength; i++) {\n" +
                "    reply[2*i] = message[i];\n" +
                "    reply[2*i+1] = message[i];\n" +
                "  }\n" +
                "   \n" +
                "  // Reply with a broadcasted message (destinationId is set to 0) \n" +
                "  if (messageTag >= 0) { \n" +
                "    messenger.sendMessage(0, reply, messageLength*2, messageTag);\n" +
                "  } else {\n" +
                "    messenger.sendMessage(0, reply, messageLength*2);  \n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "\n");

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
