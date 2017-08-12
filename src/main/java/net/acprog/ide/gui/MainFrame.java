package net.acprog.ide.gui;

import net.acprog.ide.gui.components.*;
import net.acprog.ide.gui.utils.*;
import org.jdesktop.swingx.JXMultiSplitPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    protected JPanel panel;

    protected JXMultiSplitPane verticalPanel;

    protected ApplicationSplitVerticalPaneModel verticalPanelModel;

    protected JXMultiSplitPane horizontalPanel;

    protected ApplicationSplitHorizontalPaneModel horizontalPanelModel;

    protected JMenuBar menuBar;

    protected JToolBar toolBar;

    public MainFrame() {

        InitializeMenuBar();

        InitializeToolBar();

        InitializeLayout();

        setTitle("Text Editor Demo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setPreferredSize(new Dimension(1024, 800));
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        verticalPanelModel.setDefaultWeights();
        horizontalPanelModel.setDefaultWeights();
    }

    private void InitializeToolBar() {
        toolBar = new JToolBar();

        JButton button;

        //first button
        button = new JButton();
        button.setText("Compile");
        toolBar.add(button);

        //second button
        button = new JButton();
        button.setText("Compile & run");
        toolBar.add(button);

    }

    private void InitializeLayout() {
        panel = new JPanel(new BorderLayout());

        panel.add(toolBar, BorderLayout.PAGE_START);

        net.acprog.ide.gui.components.Component c;

        verticalPanel = new JXMultiSplitPane();
        verticalPanelModel = new ApplicationSplitVerticalPaneModel();
        verticalPanel.setModel(verticalPanelModel);

        horizontalPanel = new JXMultiSplitPane();
        horizontalPanelModel = new ApplicationSplitHorizontalPaneModel();
        horizontalPanel.setModel(horizontalPanelModel);

        // componenty
        c = new ToolBoxComponent();
        horizontalPanel.add(c.render(), ApplicationSplitHorizontalPaneModel.P1);

        c = new VisualEditorComponent();
        horizontalPanel.add(c.render(), ApplicationSplitHorizontalPaneModel.P2);

        c = new EditorComponent();
        horizontalPanel.add(c.render(), ApplicationSplitHorizontalPaneModel.P3);

        c = new PropertyEditorComponent();
        horizontalPanel.add(c.render(), ApplicationSplitHorizontalPaneModel.P4);

        verticalPanel.add(horizontalPanel, ApplicationSplitVerticalPaneModel.P1);

        // konzola
        c = new ConsoleComponent();
        verticalPanel.add(c.render(), ApplicationSplitVerticalPaneModel.P2);

        panel.add(verticalPanel, BorderLayout.CENTER);

        setContentPane(panel);
    }

    private void InitializeMenuBar() {
        menuBar = new JMenuBar();

        JMenu menu;
        JMenuItem menuItem;

        // Build Project menu
        menu = new JMenu("Project");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        // Build Project submenu
        menuItem = new JMenuItem("New project", KeyEvent.VK_N);
        menu.add(menuItem);

        menuItem = new JMenuItem("Open project", KeyEvent.VK_O);
        menu.add(menuItem);

        menuItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        menu.add(menuItem);

        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menu.add(menuItem);

        // Build Help menu
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        // Build Help submenu
        menuItem = new JMenuItem("About us", KeyEvent.VK_A);
        menu.add(menuItem);

        menuItem = new JMenuItem("Check for updates", KeyEvent.VK_U);
        menu.add(menuItem);

        menuItem = new JMenuItem("Slack comunity", KeyEvent.VK_S);
        menu.add(menuItem);

        setJMenuBar(menuBar);
    }
}
