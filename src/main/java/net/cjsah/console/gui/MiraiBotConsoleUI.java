package net.cjsah.console.gui;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Objects;

public class MiraiBotConsoleUI {

    private JFrame frameBot;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MiraiBotConsoleUI windows = new MiraiBotConsoleUI();
                windows.frameBot.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MiraiBotConsoleUI() {
        initialize();
    }

    private void initialize() {

        CardLayout cl = new CardLayout(0,0);
        frameBot = new JFrame();
        frameBot.setIconImage(Toolkit.getDefaultToolkit().getImage(MiraiBotConsoleUI.class.getResource("/assets/Icon.png")));
        frameBot.setTitle("MiraiBotConsole");

        //frameBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frameBot.setBounds(100, 100, 900, 600);
        //frameBot.getContentPane().setLayout(new BorderLayout(0, 0));

        //frameBot.setUndecorated(true);		//窗口去边框
        //frameBot.setAlwaysOnTop(true);		//设置窗口总在最前
        //frameBot.setBackground(new Color(0,0,0,0));		//设置窗口背景为透明色

        frameBot.getContentPane().setLayout(null);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            //ex.printStackTrace();
        }

        /*
         *卡片总容器
         */
        JPanel cardPanel = new JPanel();
        frameBot.getContentPane().add(cardPanel, BorderLayout.CENTER);
        cardPanel.setLayout(cl);
        cardPanel.setBounds(48, 0, 836, 560);

        /*
         *数据概览卡片
         */
        Panel data = new Panel();
        cardPanel.add(data, "friend");
        data.setLayout(null);
        cardPanel.add(data, "data");

        JPanel friends = new JPanel();
        friends.setBounds(0,0,cardPanel.getWidth()/3,cardPanel.getHeight());
        friends.setBorder(new TitledBorder(null, "好友", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        friends.setLayout(new BorderLayout(0, 0));
        ScrollPane friendList = new ScrollPane();
        friends.add(friendList, BorderLayout.CENTER);
        data.add(friends);

        JPanel groups = new JPanel();
        groups.setBounds(cardPanel.getWidth()/3,0,cardPanel.getWidth()/3,cardPanel.getHeight());
        groups.setBorder(new TitledBorder(null, "群组", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        groups.setLayout(new BorderLayout(0, 0));
        ScrollPane groupList = new ScrollPane();
        groups.add(groupList, BorderLayout.CENTER);
        data.add(groups);

        JPanel about = new JPanel();
        about.setBounds((cardPanel.getWidth()/3)*2,0,cardPanel.getWidth()/3,cardPanel.getHeight());
        about.setBorder(new TitledBorder(null, "关于", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        about.setLayout(new BorderLayout(0, 0));
        ScrollPane aboutSP = new ScrollPane();
        about.add(aboutSP, BorderLayout.CENTER);
        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);
        aboutText.setText("MiraiBotConsole By Cjsah\n版本: Dev 1.0.11\nGUI版本: 0.1.3\n内核版本: Releases 2.7.1\n");
        aboutSP.add(aboutText);
        data.add(about);

        /*
         *控制台卡片
         */
        Panel console = new Panel();
        console.setForeground(Color.WHITE);
        console.setBackground(Color.LIGHT_GRAY);
        cardPanel.add(console, "console");
        console.setLayout(new BorderLayout(0, 0));

        JTextField command = new JTextField();
        command.setColumns(255);
        console.add(command, BorderLayout.SOUTH);

        JPanel state = new JPanel();
        state.setBackground(SystemColor.textHighlightText);
        state.setBorder(new TitledBorder(null, "\u5728\u7EBF\u72B6\u6001", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        console.add(state, BorderLayout.NORTH);
        state.setLayout(new BorderLayout(0, 0));

        JLabel state_Label = new JLabel("在线");      //记得改
        state.add(state_Label, BorderLayout.EAST);

        JPanel log = new JPanel();
        log.setBackground(new Color(255, 255, 255));
        log.setBorder(new TitledBorder(null, "\u65E5\u5FD7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        console.add(log, BorderLayout.CENTER);
        log.setLayout(new BorderLayout(0, 0));

        TextArea log_textArea = new TextArea("GUI部分组件功能尚未实现");
        log_textArea.setBackground(new Color(245, 245, 245));
        log_textArea.setEditable(false);
        log_textArea.setForeground(Color.BLACK);
        log.add(log_textArea, BorderLayout.CENTER);

        /*
         *插件卡片
         */
        Panel plugin = new Panel();
        cardPanel.add(plugin, "plugin");
        plugin.setLayout(null);

        JPanel plugin_list_panel = new JPanel();
        plugin_list_panel.setBounds(0, 32, 836, 528);
        plugin_list_panel.setBorder(new TitledBorder(null, "插件列表", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        plugin.add(plugin_list_panel);
        plugin_list_panel.setLayout(new BorderLayout(0, 0));

        JScrollPane plugin_scrollPane = new JScrollPane();
        plugin_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        plugin_list_panel.add(plugin_scrollPane, BorderLayout.CENTER);

        JPanel pluginPanel = new JPanel();
        pluginPanel.setLayout(null);
        pluginPanel.setBounds(0,0,836,1000);
        pluginPanel.setBackground(new Color(245,245,245));
        plugin_scrollPane.setViewportView(pluginPanel);

        JPanel ctrl_panel = new JPanel();
        ctrl_panel.setBounds(0, 0, 836, 32);
        plugin.add(ctrl_panel);
        ctrl_panel.setLayout(null);

        JButton reloadButton = new JButton();//刷新按钮
        reloadButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/refresh.png"))));
        reloadButton.setBounds(10, 0, 32, 32);
        reloadButton.setContentAreaFilled(false);		//设置按钮背景透明
        reloadButton.setBorderPainted(false);		//去掉按钮边框
        ctrl_panel.add(reloadButton);

        JButton addButton = new JButton();//添加按钮
        addButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/add.png"))));
        addButton.setBounds(52, 0, 32, 32);
        addButton.setContentAreaFilled(false);		//设置按钮背景透明
        addButton.setBorderPainted(false);		//去掉按钮边框
        ctrl_panel.add(addButton);

        JButton delButton = new JButton();//删除按钮
        delButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/delete.png"))));
        delButton.setBounds(94, 0, 32, 32);
        delButton.setContentAreaFilled(false);		//设置按钮背景透明
        delButton.setBorderPainted(false);		//去掉按钮边框
        ctrl_panel.add(delButton);

        /*
         *设置卡片
         */
        Panel config = new Panel();
        cardPanel.add(config, "config");
        config.setLayout(new BorderLayout(0, 0));

        JPanel account = new JPanel();
        account.setBorder(new TitledBorder(null, "\u8D26\u6237", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        config.add(account, BorderLayout.NORTH);
        account.setLayout(new BorderLayout(0, 0));

        JPanel accountInputPanel = new JPanel();
        accountInputPanel.setLayout(new BorderLayout(0, 0));

        JLabel account_label = new JLabel("账户：");
        accountInputPanel.add(account_label, BorderLayout.WEST);

        JTextField accountTextField = new JTextField();
        accountTextField.setColumns(10);
        accountInputPanel.add(accountTextField, BorderLayout.CENTER);
        account.add(accountInputPanel, BorderLayout.NORTH);

        JPanel passwordInputPanel = new JPanel();
        account.add(passwordInputPanel, BorderLayout.CENTER);
        passwordInputPanel.setLayout(new BorderLayout(0, 0));

        JLabel password_label = new JLabel("密码：");
        passwordInputPanel.add(password_label, BorderLayout.WEST);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setToolTipText("");
        passwordInputPanel.add(passwordField, BorderLayout.CENTER);

        JCheckBox password_vis = new JCheckBox("显示密码");
        passwordInputPanel.add(password_vis, BorderLayout.SOUTH);
        password_vis.addActionListener(e -> passwordField.setVisible(true));

        JPanel ctrlButtonPanel = new JPanel();
        account.add(ctrlButtonPanel, BorderLayout.SOUTH);
        ctrlButtonPanel.setLayout(new GridLayout(0, 2, 0, 0));

        JButton edit = new JButton("编辑");
        ctrlButtonPanel.add(edit);

        JButton exit = new JButton("退出");
        exit.addActionListener(e -> System.exit(0));
        ctrlButtonPanel.add(exit);

        /*
         *卡片切换
         */
        JPanel cardButtonPanel = new JPanel();
        cardButtonPanel.setLocation(0, 0);
        cardButtonPanel.setBackground(new Color(49, 191, 225));
        cardButtonPanel.setSize(48, 560);
        frameBot.getContentPane().add(cardButtonPanel, BorderLayout.WEST);
        cardButtonPanel.setLayout(null);

        JButton dataPanelButton=new JButton();
        dataPanelButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/dataIcon.png"))));
        dataPanelButton.setContentAreaFilled(false);		//设置按钮背景透明
        dataPanelButton.setBorderPainted(false);		//去掉按钮边框
        dataPanelButton.setBounds(0, 0, 48, 48);		//设置按钮大小及位置

        JButton consolePanelButton=new JButton();
        consolePanelButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/consoleIcon.png"))));
        consolePanelButton.setContentAreaFilled(false);		//设置按钮背景透明
        consolePanelButton.setBorderPainted(false);		//去掉按钮边框
        consolePanelButton.setBounds(0, 48, 48, 48);		//设置按钮大小及位置

        JButton pluginPanelButton=new JButton();
        pluginPanelButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/pluginIcon.png"))));
        pluginPanelButton.setContentAreaFilled(false);		//设置按钮背景透明
        pluginPanelButton.setBorderPainted(false);		//去掉按钮边框
        pluginPanelButton.setBounds(0, 96, 48, 48);		//设置按钮大小及位置

        JButton configPanelButton=new JButton();
        configPanelButton.setIcon(new ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/button/configIcon.png"))));
        configPanelButton.setContentAreaFilled(false);		//设置按钮背景透明
        configPanelButton.setBorderPainted(false);		//去掉按钮边框
        configPanelButton.setBounds(0, 512, 48, 48);		//设置按钮大小及位置

        cardButtonPanel.add(consolePanelButton);
        cardButtonPanel.add(pluginPanelButton);
        cardButtonPanel.add(dataPanelButton);
        cardButtonPanel.add(configPanelButton);

        dataPanelButton.addActionListener(e -> cl.show(cardPanel, "data"));
        consolePanelButton.addActionListener(e -> cl.show(cardPanel, "console"));
        pluginPanelButton.addActionListener(e -> cl.show(cardPanel, "plugin"));
        configPanelButton.addActionListener(e -> cl.show(cardPanel, "config"));

        /*
         *创建系统托盘
         */
		if (SystemTray.isSupported()) {
			PopupMenu popupMenu = new PopupMenu();

			MenuItem itemDisplay = new MenuItem("显示主界面");
            itemDisplay.addActionListener(e -> frameBot.setVisible(true));
            popupMenu.add(itemDisplay);

			MenuItem itemExit = new MenuItem("退出");
            itemExit.addActionListener(e -> System.exit(0));
			popupMenu.add(itemExit);

			/*
			 *设置托盘图标
			 */
			final ImageIcon ico = new ImageIcon(
					Objects.requireNonNull(MiraiBotConsoleUI.class.getResource("/assets/Icon16.png")));
			TrayIcon trayIcon = new TrayIcon(ico.getImage(), "MiraiBotConsole", popupMenu);
            trayIcon.addActionListener(e -> frameBot.setVisible(true));

            /*
			 *创建系统托盘
			 */
			try {
				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e) {
				//e.printStackTrace();
			}
		}


    }
}

