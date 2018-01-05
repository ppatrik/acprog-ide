package net.acprog.ide.gui;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import net.acprog.ide.App;
import net.acprog.ide.configurations.IdeProject;
import net.acprog.ide.configurations.IdeSettings;
import net.acprog.ide.gui.components.*;
import net.acprog.ide.gui.utils.ConsoleIde;
import net.acprog.ide.platform.Platform;
import net.acprog.ide.utils.BoardPort;
import net.acprog.ide.utils.event.EventManager;
import net.acprog.ide.utils.event.EventType;
import net.acprog.ide.utils.view.StubMenuListener;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EditorFrame extends JFrame {

    public static EditorFrame instance;

    private final IdeProject ideProject;

    private final EventManager eventManager;

    protected JPanel panel;

    protected JMenuBar menuBar;

    protected JToolBar toolBar;

    protected CControl control;

    public ConsoleIdeComponent console;

    public EditorIdeComponent code;

    public EditorFrame(IdeProject ideProject) {
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

        eventManager.registerObserver(EventType.BUILD, (e, o) -> {
            new Thread(this::compilerBuild).start();
        });
        eventManager.registerObserver(EventType.VERIFY, (e, o) -> {
            new Thread(this::compilerVerify).start();
        });
        eventManager.registerObserver(EventType.UPLOAD, (e, o) -> {
            new Thread(this::compilerUpload).start();
        });
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

    private boolean compilerBuild() {
        eventManager.callEvent(EventType.PROJECT_PRE_SAVE);

        if (ideProject.save(ConsoleIde.instance) &&
                ideProject.build(ConsoleIde.instance, false)) {
            ConsoleIde.instance.println("Build SUCCESS");
            return true;
        }
        return false;
    }

    private boolean compilerVerify() {
        if (compilerBuild()) {

            if (ideProject.verify(ConsoleIde.instance)) {
                ConsoleIde.instance.println("Verify SUCCESS");
                return true;
            }
        }
        return false;
    }

    private boolean compilerUpload() {
        if (compilerBuild()) {

            if (ideProject.upload(ConsoleIde.instance, IdeSettings.getInstance().getSerialPort())) {
                ConsoleIde.instance.println("Upload SUCCESS");
                return true;
            }
        }
        return false;
    }

    private void InitializeToolBar() {
        toolBar = new JToolBar();

        JButton button;

        button = new JButton();
        button.setText("Build");
        button.addActionListener(e -> eventManager.callEvent(EventType.BUILD));
        toolBar.add(button);

        button = new JButton();
        button.setText("Verify");
        button.addActionListener(e -> eventManager.callEvent(EventType.VERIFY));
        toolBar.add(button);

        button = new JButton();
        button.setText("Upload");
        button.addActionListener(e -> eventManager.callEvent(EventType.UPLOAD));
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
        code = (EditorIdeComponent) c;

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

    private JMenu portMenu;

    private void InitializeMenuBar() {
        menuBar = new JMenuBar();

        JMenu menu;
        JMenuItem menuItem;

        // Build IdeProject menu
        menu = new JMenu("IdeProject");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        // region Build IdeProject submenu
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
        // endregion

        // Build Tools menu
        menu = new JMenu("Tools");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.addMenuListener(new StubMenuListener() {
            public void menuSelected(MenuEvent e) {
                populatePortMenu();
            }
        });
        menuBar.add(menu);

        // region Build Tools submenu
        portMenu = new JMenu("Port");
        populatePortMenu();
        menu.add(portMenu);

        // endregion

        // Build Help menu
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        // region Build Help submenu
        menuItem = new JMenuItem("About us", KeyEvent.VK_A);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.HELP_ABOUT));
        menu.add(menuItem);

        menuItem = new JMenuItem("Check for updates", KeyEvent.VK_U);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.HELP_UPDATE));
        menu.add(menuItem);

        menuItem = new JMenuItem("Slack comunity", KeyEvent.VK_S);
        menuItem.addActionListener(e -> eventManager.callEvent(EventType.HELP_SLACK));
        menu.add(menuItem);
        // endregion

        setJMenuBar(menuBar);
    }

    public IdeProject getIdeProject() {
        return ideProject;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    private final static List<String> BOARD_PROTOCOLS_ORDER = Arrays.asList("serial", "network");
    private final static List<String> BOARD_PROTOCOLS_ORDER_TRANSLATIONS = Arrays.asList("Serial ports", "Network ports");

    private void populatePortMenu() {
        portMenu.removeAll();

        Platform platform = App.getPlatform();

        String selectedPort = IdeSettings.getInstance().getSerialPort();

        List<BoardPort> ports = App.getDiscoveryManager().discovery();

        ports = platform.filterPorts(ports, true);

        Collections.sort(ports, new Comparator<BoardPort>() {
            @Override
            public int compare(BoardPort o1, BoardPort o2) {
                return BOARD_PROTOCOLS_ORDER.indexOf(o1.getProtocol()) - BOARD_PROTOCOLS_ORDER.indexOf(o2.getProtocol());
            }
        });

        String lastProtocol = null;
        String lastProtocolTranslated;
        for (BoardPort port : ports) {
            if (lastProtocol == null || !port.getProtocol().equals(lastProtocol)) {
                if (lastProtocol != null) {
                    portMenu.addSeparator();
                }
                lastProtocol = port.getProtocol();

                if (BOARD_PROTOCOLS_ORDER.indexOf(port.getProtocol()) != -1) {
                    lastProtocolTranslated = BOARD_PROTOCOLS_ORDER_TRANSLATIONS.get(BOARD_PROTOCOLS_ORDER.indexOf(port.getProtocol()));
                } else {
                    lastProtocolTranslated = port.getProtocol();
                }
                JMenuItem lastProtocolMenuItem = new JMenuItem(lastProtocolTranslated);
                lastProtocolMenuItem.setEnabled(false);
                portMenu.add(lastProtocolMenuItem);
            }
            String address = port.getAddress();
            String label = port.getLabel();

            JCheckBoxMenuItem item = new JCheckBoxMenuItem(label, address.equals(selectedPort));
            item.addActionListener(new SerialMenuListener(address));
            portMenu.add(item);
        }

        portMenu.setEnabled(portMenu.getMenuComponentCount() > 0);
    }

    class SerialMenuListener implements ActionListener {

        private final String serialPort;

        public SerialMenuListener(String serialPort) {
            this.serialPort = serialPort;
        }

        public void actionPerformed(ActionEvent e) {
            IdeSettings.getInstance().setSerialPort(serialPort);
        }

    }
}
