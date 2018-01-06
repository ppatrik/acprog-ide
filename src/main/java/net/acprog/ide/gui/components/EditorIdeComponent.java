package net.acprog.ide.gui.components;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.gui.EditorFrame;
import net.acprog.ide.utils.event.EventType;
import net.acprog.ide.utils.event.Observer;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.Element;

public class EditorIdeComponent implements IdeComponent {
    private final EditorFrame editorFrame;

    protected RSyntaxTextArea textArea;
    protected RTextScrollPane sp;

    public EditorIdeComponent(EditorFrame editorFrame) {
        this.editorFrame = editorFrame;
        InitializeComponents();

        textArea.setText(editorFrame.getIdeProject().getSourceString());

        this.editorFrame.getEventManager().registerObserver(EventType.PROJECT_PRE_SAVE, new Observer() {
            @Override
            public void onEvent(EventType eventType, Object o) {
                editorFrame.getIdeProject().setSourceString(textArea.getText());
            }
        });
    }

    private class AcprogParser extends AbstractParser {
        private DefaultParseResult result;

        public AcprogParser() {
            this.result = new DefaultParseResult(this);
        }

        @Override
        public ParseResult parse(RSyntaxDocument doc, String s) {
            this.result.clearNotices();

            Element root = doc.getDefaultRootElement();
            this.result.setParsedLines(0, root.getElementCount() - 1);
            if (doc.getLength() != 0) {
                for (int line = 0; line < root.getElementCount(); line++) {
                    Token t = doc.getTokenListForLine(line);
                    //EditorFrame.instance.console.println(t.toString());
                    //EditorFrame.instance.console.errln("-----");

                    DefaultParserNotice pn = new DefaultParserNotice(this, "Skuska", line, 592, 8);
                    pn.setLevel(ParserNotice.Level.ERROR);
                    this.result.addNotice(pn);
                }
            }
            return this.result;
        }
    }

    private Parser parser = new AcprogParser();

    private void InitializeComponents() {
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        textArea.setCodeFoldingEnabled(true);
        textArea.addParser(parser);

        sp = new RTextScrollPane(textArea);
    }

    public JComponent render() {
        return sp;
    }

    @Override
    public SingleCDockable dockable() {
        return new DefaultSingleCDockable(getClass().toString(), "Editor", render());
    }

    public void createOrFindMethod(String methodName) {

    }

    /*private AbstractSourceTree tree;

    private void refreshSourceTree() {

        if (tree!=null) {
            tree.uninstall();
        }

        String language = textArea.getSyntaxEditingStyle();
        if (SyntaxConstants.SYNTAX_STYLE_JAVA.equals(language)) {
            tree = new JavaOutlineTree();
        }
        else if (SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT.equals(language)) {
            tree = new JavaScriptOutlineTree();
        }
        else if (SyntaxConstants.SYNTAX_STYLE_XML.equals(language)) {
            tree = new XmlOutlineTree();
        }
        else {
            tree = null;
        }

        if (tree!=null) {
            tree.listenTo(textArea);
            treeSP.setViewportView(tree);
        }
        else {
            JTree dummy = new JTree((TreeNode)null);
            treeSP.setViewportView(dummy);
        }
        treeSP.revalidate();

    }*/

}
