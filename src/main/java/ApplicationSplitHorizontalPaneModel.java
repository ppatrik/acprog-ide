import org.jdesktop.swingx.MultiSplitLayout;

public class ApplicationSplitHorizontalPaneModel extends MultiSplitLayout.Split {
    //4 possible positions
    public static final String P1 = "1";
    public static final String P2 = "2";
    public static final String P3 = "3";
    public static final String P4 = "4";

    MultiSplitLayout.Leaf p1, p2, p3, p4;
    ;

    public ApplicationSplitHorizontalPaneModel() {
        setRowLayout(true);
        p1 = new MultiSplitLayout.Leaf(P1);
        p2 = new MultiSplitLayout.Leaf(P2);
        p3 = new MultiSplitLayout.Leaf(P3);
        p4 = new MultiSplitLayout.Leaf(P4);
        setChildren(p1, new MultiSplitLayout.Divider(), p2, new MultiSplitLayout.Divider(),
                p3, new MultiSplitLayout.Divider(), p4);
    }

    public void setDefaultWeights() {
        p1.setWeight(0.25);
        p2.setWeight(0.25);
        p3.setWeight(0.25);
        p4.setWeight(0.25);
    }
}
