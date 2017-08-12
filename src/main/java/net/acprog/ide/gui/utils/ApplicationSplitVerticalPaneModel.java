package net.acprog.ide.gui.utils;

import org.jdesktop.swingx.MultiSplitLayout;

public class ApplicationSplitVerticalPaneModel extends MultiSplitLayout.Split {
    //4 possible positions
    public static final String P1 = "1";
    public static final String P2 = "2";


    MultiSplitLayout.Leaf p1, p2;

    public ApplicationSplitVerticalPaneModel() {
        setRowLayout(false);

        p1 = new MultiSplitLayout.Leaf(P1);
        p2 = new MultiSplitLayout.Leaf(P2);

        setChildren(p1, new MultiSplitLayout.Divider(), p2);
    }

    public void setDefaultWeights() {
        p1.setWeight(0.8);
        p2.setWeight(0.2);
    }
}
