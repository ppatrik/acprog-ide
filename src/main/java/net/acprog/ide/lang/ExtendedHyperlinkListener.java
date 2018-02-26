package net.acprog.ide.lang;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.event.HyperlinkEvent;
import java.util.EventListener;


/**
 * Listens for hyperlink events from
 * {@link org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip}s.  In addition
 * to the link event, the text area that the tip is for is also received, which
 * allows the listener to modify the displayed content, if desired.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ExtendedHyperlinkListener extends EventListener {


    /**
     * Called when a link in a
     * {@link org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip} is clicked.
     *
     * @param textArea The text area displaying the tip.
     * @param e        The event.
     */
    void linkClicked(RSyntaxTextArea textArea, HyperlinkEvent e);


}