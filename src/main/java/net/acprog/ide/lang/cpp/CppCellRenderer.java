/*
 * 03/21/2010
 *
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSTALanguageSupport.License.txt file for details.
 */
package net.acprog.ide.lang.cpp;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.VariableCompletion;

import javax.swing.*;


/**
 * The cell renderer used for the C programming language.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CppCellRenderer extends CompletionCellRenderer {

    private Icon variableIcon;
    private Icon functionIcon;


    /**
     * Constructor.
     */
    public CppCellRenderer() {
        variableIcon = getIcon("var.png");
        functionIcon = getIcon("function.png");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareForOtherCompletion(JList list,
                                             Completion c, int index, boolean selected, boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIcon(getEmptyIcon());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareForVariableCompletion(JList list,
                                                VariableCompletion vc, int index, boolean selected,
                                                boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected,
                hasFocus);
        setIcon(variableIcon);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareForFunctionCompletion(JList list,
                                                FunctionCompletion fc, int index, boolean selected,
                                                boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected,
                hasFocus);
        setIcon(functionIcon);
    }


}