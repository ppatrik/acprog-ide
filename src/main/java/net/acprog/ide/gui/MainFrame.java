package net.acprog.ide.gui;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.App;
import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.gui.components.*;
import net.acprog.ide.gui.utils.ConsoleIde;
import net.acprog.ide.gui.utils.ConsoleInterface;
import net.acprog.ide.utils.event.EventManager;
import net.acprog.ide.utils.event.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class MainFrame extends JFrame {

    public static MainFrame instance;

    private final IdeProject ideProject;

    private final EventManager eventManager;

    protected JPanel panel;

    protected JMenuBar menuBar;

    protected JToolBar toolBar;

    protected CControl control;

    public ConsoleIdeComponent console;

    public MainFrame(IdeProject ideProject) {
        instance = this;

        this.ideProject = ideProject;

        this.eventManager = new EventManager();

        InitializeEvents();

        InitializeMenuBar();

        InitializeToolBar();

        InitializeLayout();

        setTitle("Text Editor Demo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setPreferredSize(new Dimension(1024, 800));
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void InitializeEvents() {
        eventManager.registerObserver(EventType.PROJECT_CREATE, this::openProject);
        eventManager.registerObserver(EventType.PROJECT_OPEN, this::openProject);
        eventManager.registerObserver(EventType.PROJECT_SAVE, this::saveProject);
        eventManager.registerObserver(EventType.QUIT, this::closeProject);

        eventManager.registerObserver(EventType.COMPILE, this::compileProject);
        eventManager.registerObserver(EventType.COMPILE_AND_RUN, this::compileProjectAndRun);
        eventManager.registerObserver(EventType.HELP_ABOUT, this::helpAbout);
        eventManager.registerObserver(EventType.HELP_SLACK, this::helpSlack);
        eventManager.registerObserver(EventType.HELP_UPDATE, this::helpUpdate);
        eventManager.registerObserver(EventType.PREFERENCES_OPEN, this::openPreferences);
    }

    private void openPreferences(EventType eventType, Object o) {
        App.openSettings();
    }

    private void openProject(EventType eventType, Object o) {
        eventManager.callEvent(EventType.PROJECT_PRE_SAVE);
        ideProject.save(ConsoleIde.instance);
        dispose();
        App.openProjectChooser();
    }

    private void saveProject(EventType eventType, Object o) {
        eventManager.callEvent(EventType.PROJECT_PRE_SAVE);
        ideProject.save(ConsoleIde.instance);
    }

    private void closeProject(EventType eventType, Object o) {
        eventManager.callEvent(EventType.PROJECT_PRE_SAVE);
        try {
            control.writeXML(new File("workspace.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();
    }

    private void helpUpdate(EventType eventType, Object o) {
        try {
            String s = "https://github.com/ppatrik/acprog-ide/releases";
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(URI.create(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void helpSlack(EventType eventType, Object o) {
        try {
            String s = "https://acprog.slack.com";
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(URI.create(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void helpAbout(EventType eventType, Object o) {
        try {
            String s = "https://github.com/ppatrik/acprog-ide/";
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(URI.create(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void compileProject(EventType eventType, Object o) {
        eventManager.callEvent(EventType.PROJECT_PRE_SAVE);
        new Thread(() -> {
            ConsoleInterface console = ConsoleIde.instance;
            ideProject.save(console);
            ideProject.build(console, false);
            ideProject.verify(console);
            console.println("All tasks finished successfully\n");
        }).start();
    }

    private void compileProjectAndRun(EventType eventType, Object o) {
        compileProject(eventType, o);
        // TODO: spustenie projektu na arduine
    }

    private void InitializeToolBar() {
        toolBar = new JToolBar();

        JButton button;

        //first button
        button = new JButton();
        button.setText("Compile");
        button.addActionListener(e -> eventManager.callEvent(EventType.COMPILE));
        toolBar.add(button);

        //second button
        button = new JButton();
        button.setText("Compile & run");
        button.addActionListener(e -> eventManager.callEvent(EventType.COMPILE_AND_RUN));
        toolBar.add(button);

    }

    public static SingleCDockable create(String title, Color color) {
        JPanel bg = new JPanel();
        bg.setOpaque(true);
        bg.setBackground(color);
        return new DefaultSingleCDockable(title, title, bg);
    }

    private void InitializeLayout() {
        panel = new JPanel(new BorderLayout());

        panel.add(toolBar, BorderLayout.PAGE_START);

        control = new CControl(this);
        panel.add(control.getContentArea(), BorderLayout.CENTER);

        IdeComponent c;
        SingleCDockable dockable;

        // components
        c = new ToolBoxIdeComponent(this);
        dockable = c.dockable();
        control.addDockable(dockable);
        dockable.setLocation(CLocation.base().normalWest(0.25));
        dockable.setVisible(true);

        c = new VisualEditorIdeComponent(this);
        dockable = c.dockable();
        control.addDockable(dockable);
        dockable.setLocation(CLocation.base().normalWest(0.25));
        dockable.setVisible(true);

        c = new EditorIdeComponent(this);
        dockable = c.dockable();
        control.addDockable(dockable);
        dockable.setLocation(CLocation.base().normalEast(0.25));
        dockable.setVisible(true);

        c = new PropertyEditorIdeComponent(this);
        dockable = c.dockable();
        control.addDockable(dockable);
        dockable.setLocation(CLocation.base().normalEast(0.25));
        dockable.setVisible(true);

        c = new ConsoleIdeComponent(this);
        dockable = c.dockable();
        control.addDockable(dockable);
        dockable.setLocation(CLocation.base().normalSouth(0.1));
        dockable.setVisible(true);
        console = (ConsoleIdeComponent) c;

        setContentPane(panel);

        try {
            control.readXML(new File("workspace.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void InitializeMenuBar() {
        menuBar = new JMenuBar();

        JMenu menu;
        JMenuItem menuItem;

        // Build IdeProject menu
        menu = new JMenu("IdeProject");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        // Build IdeProject submenu
        menuItem = new JMenuItem("New project", KeyEvent.VK_N);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.PROJECT_CREATE));
        menu.add(menuItem);

        menuItem = new JMenuItem("Open project", KeyEvent.VK_O);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.PROJECT_OPEN));
        menu.add(menuItem);

        menuItem = new JMenuItem("Save project", KeyEvent.VK_O);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.PROJECT_SAVE));
        menu.add(menuItem);

        menuItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.PREFERENCES_OPEN));
        menu.add(menuItem);

        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.QUIT));
        menu.add(menuItem);

        // Build Help menu
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        // Build Help submenu
        menuItem = new JMenuItem("About us", KeyEvent.VK_A);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.HELP_ABOUT));
        menu.add(menuItem);

        menuItem = new JMenuItem("Check for updates", KeyEvent.VK_U);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.HELP_UPDATE));
        menu.add(menuItem);

        menuItem = new JMenuItem("Slack comunity", KeyEvent.VK_S);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.HELP_SLACK));
        menu.add(menuItem);

        setJMenuBar(menuBar);
    }

    public IdeProject getIdeProject() {
        return ideProject;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
