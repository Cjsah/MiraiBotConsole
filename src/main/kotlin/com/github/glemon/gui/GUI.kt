package com.github.glemon.gui

import java.awt.AWTException
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.MenuItem
import java.awt.Panel
import java.awt.PopupMenu
import java.awt.ScrollPane
import java.awt.SystemColor
import java.awt.SystemTray
import java.awt.TextArea
import java.awt.Toolkit
import java.awt.TrayIcon
import java.util.Objects
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.ScrollPaneConstants
import javax.swing.UIManager
import javax.swing.border.TitledBorder
import kotlin.system.exitProcess

class GUI : JFrame() {
    init {
        val cl = CardLayout(0, 0)
        this.iconImage = Toolkit.getDefaultToolkit().getImage(MiraiBotConsoleUI::class.java.getResource("/assets/Icon.png"))
        this.title = "MiraiBotConsole"

        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 900, 600)
        //frameBot.getContentPane().setLayout(new BorderLayout(0, 0));

        //this.setUndecorated(true);		//窗口去边框
        //this.setAlwaysOnTop(true);		//设置窗口总在最前
        //this.setBackground(new Color(0,0,0,0));		//设置窗口背景为透明色
        this.contentPane.layout = null
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
        } catch (ignore: Exception) { }

        // 卡片总容器
        val cardPanel = JPanel()
        this.contentPane.add(cardPanel, BorderLayout.CENTER)
        cardPanel.layout = cl
        cardPanel.setBounds(48, 0, 836, 560)

        // 数据概览卡片
        val data = Panel()
        cardPanel.add(data, "friend")
        data.layout = null
        cardPanel.add(data, "data")
        val friends = JPanel()
        friends.setBounds(0, 0, cardPanel.width / 3, cardPanel.height)
        friends.border = TitledBorder(null, "好友", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        friends.layout = BorderLayout(0, 0)
        val friendList = ScrollPane()
        friends.add(friendList, BorderLayout.CENTER)
        data.add(friends)
        val groups = JPanel()
        groups.setBounds(cardPanel.width / 3, 0, cardPanel.width / 3, cardPanel.height)
        groups.border = TitledBorder(null, "群组", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        groups.layout = BorderLayout(0, 0)
        val groupList = ScrollPane()
        groups.add(groupList, BorderLayout.CENTER)
        data.add(groups)
        val about = JPanel()
        about.setBounds(cardPanel.width / 3 * 2, 0, cardPanel.width / 3, cardPanel.height)
        about.border = TitledBorder(null, "关于", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        about.layout = BorderLayout(0, 0)
        val aboutSP = ScrollPane()
        about.add(aboutSP, BorderLayout.CENTER)
        val aboutText = JTextArea()
        aboutText.isEditable = false
        aboutText.text = "MiraiBotConsole By Cjsah\n版本: Dev 1.0.11\nGUI版本: 0.1.3\n内核版本: Releases 2.7.1\n"
        aboutSP.add(aboutText)
        data.add(about)

        // 控制台卡片
        val console = Panel()
        console.foreground = Color.WHITE
        console.background = Color.LIGHT_GRAY
        cardPanel.add(console, "console")
        console.layout = BorderLayout(0, 0)
        val command = JTextField()
        command.columns = 255
        console.add(command, BorderLayout.SOUTH)
        val state = JPanel()
        state.background = SystemColor.textHighlightText
        state.border = TitledBorder(null, "\u5728\u7EBF\u72B6\u6001", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        console.add(state, BorderLayout.NORTH)
        state.layout = BorderLayout(0, 0)
        val stateLabel = JLabel("在线") //记得改
        state.add(stateLabel, BorderLayout.EAST)
        val log = JPanel()
        log.background = Color(255, 255, 255)
        log.border = TitledBorder(null, "\u65E5\u5FD7", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        console.add(log, BorderLayout.CENTER)
        log.layout = BorderLayout(0, 0)
        val logTextArea = TextArea("GUI部分组件功能尚未实现")
        logTextArea.background = Color(245, 245, 245)
        logTextArea.isEditable = false
        logTextArea.foreground = Color.BLACK
        log.add(logTextArea, BorderLayout.CENTER)

        // 插件卡片
        val plugin = Panel()
        cardPanel.add(plugin, "plugin")
        plugin.layout = null
        val pluginListPanel = JPanel()
        pluginListPanel.setBounds(0, 32, 836, 528)
        pluginListPanel.border = TitledBorder(null, "插件列表", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        plugin.add(pluginListPanel)
        pluginListPanel.layout = BorderLayout(0, 0)
        val pluginScrollPane = JScrollPane()
        pluginScrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        pluginListPanel.add(pluginScrollPane, BorderLayout.CENTER)
        val pluginPanel = JPanel()
        pluginPanel.layout = null
        pluginPanel.setBounds(0, 0, 836, 1000)
        pluginPanel.background = Color(245, 245, 245)
        pluginScrollPane.setViewportView(pluginPanel)
        val ctrlPanel = JPanel()
        ctrlPanel.setBounds(0, 0, 836, 32)
        plugin.add(ctrlPanel)
        ctrlPanel.layout = null
        val reloadButton = JButton() //刷新按钮
        reloadButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/refresh.png")))
        reloadButton.setBounds(10, 0, 32, 32)
        reloadButton.isContentAreaFilled = false //设置按钮背景透明
        reloadButton.isBorderPainted = false //去掉按钮边框
        ctrlPanel.add(reloadButton)
        val addButton = JButton() //添加按钮
        addButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/add.png")))
        addButton.setBounds(52, 0, 32, 32)
        addButton.isContentAreaFilled = false //设置按钮背景透明
        addButton.isBorderPainted = false //去掉按钮边框
        ctrlPanel.add(addButton)
        val delButton = JButton() //删除按钮
        delButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/delete.png")))
        delButton.setBounds(94, 0, 32, 32)
        delButton.isContentAreaFilled = false //设置按钮背景透明
        delButton.isBorderPainted = false //去掉按钮边框
        ctrlPanel.add(delButton)

        // 设置卡片
        val config = Panel()
        cardPanel.add(config, "config")
        config.layout = BorderLayout(0, 0)
        val account = JPanel()
        account.border = TitledBorder(null, "\u8D26\u6237", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        config.add(account, BorderLayout.NORTH)
        account.layout = BorderLayout(0, 0)
        val accountInputPanel = JPanel()
        accountInputPanel.layout = BorderLayout(0, 0)
        val accountLabel = JLabel("账户：")
        accountInputPanel.add(accountLabel, BorderLayout.WEST)
        val accountTextField = JTextField()
        accountTextField.columns = 10
        accountInputPanel.add(accountTextField, BorderLayout.CENTER)
        account.add(accountInputPanel, BorderLayout.NORTH)
        val passwordInputPanel = JPanel()
        account.add(passwordInputPanel, BorderLayout.CENTER)
        passwordInputPanel.layout = BorderLayout(0, 0)
        val passwordLabel = JLabel("密码：")
        passwordInputPanel.add(passwordLabel, BorderLayout.WEST)
        val passwordField = JPasswordField()
        passwordField.toolTipText = ""
        passwordInputPanel.add(passwordField, BorderLayout.CENTER)
        val passwordVis = JCheckBox("显示密码")
        passwordInputPanel.add(passwordVis, BorderLayout.SOUTH)
        passwordVis.addActionListener { passwordField.isVisible = true }
        val ctrlButtonPanel = JPanel()
        account.add(ctrlButtonPanel, BorderLayout.SOUTH)
        ctrlButtonPanel.layout = GridLayout(0, 2, 0, 0)
        val edit = JButton("编辑")
        ctrlButtonPanel.add(edit)
        val exit = JButton("退出")
        exit.addActionListener { exitProcess(0) }
        ctrlButtonPanel.add(exit)

        // 卡片切换
        val cardButtonPanel = JPanel()
        cardButtonPanel.setLocation(0, 0)
        cardButtonPanel.background = Color(49, 191, 225)
        cardButtonPanel.setSize(48, 560)
        this.contentPane.add(cardButtonPanel, BorderLayout.WEST)
        cardButtonPanel.layout = null
        val dataPanelButton = JButton()
        dataPanelButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/dataIcon.png")))
        dataPanelButton.isContentAreaFilled = false //设置按钮背景透明
        dataPanelButton.isBorderPainted = false //去掉按钮边框
        dataPanelButton.setBounds(0, 0, 48, 48) //设置按钮大小及位置
        val consolePanelButton = JButton()
        consolePanelButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/consoleIcon.png")))
        consolePanelButton.isContentAreaFilled = false //设置按钮背景透明
        consolePanelButton.isBorderPainted = false //去掉按钮边框
        consolePanelButton.setBounds(0, 48, 48, 48) //设置按钮大小及位置
        val pluginPanelButton = JButton()
        pluginPanelButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/pluginIcon.png")))
        pluginPanelButton.isContentAreaFilled = false //设置按钮背景透明
        pluginPanelButton.isBorderPainted = false //去掉按钮边框
        pluginPanelButton.setBounds(0, 96, 48, 48) //设置按钮大小及位置
        val configPanelButton = JButton()
        configPanelButton.icon = ImageIcon(Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/button/configIcon.png")))
        configPanelButton.isContentAreaFilled = false //设置按钮背景透明
        configPanelButton.isBorderPainted = false //去掉按钮边框
        configPanelButton.setBounds(0, 512, 48, 48) //设置按钮大小及位置
        cardButtonPanel.add(consolePanelButton)
        cardButtonPanel.add(pluginPanelButton)
        cardButtonPanel.add(dataPanelButton)
        cardButtonPanel.add(configPanelButton)
        dataPanelButton.addActionListener { cl.show(cardPanel, "data") }
        consolePanelButton.addActionListener { cl.show(cardPanel, "console") }
        pluginPanelButton.addActionListener { cl.show(cardPanel, "plugin") }
        configPanelButton.addActionListener { cl.show(cardPanel, "config") }

        // 创建系统托盘
        if (SystemTray.isSupported()) {
            val popupMenu = PopupMenu()
            val itemDisplay = MenuItem("显示主界面")
            itemDisplay.addActionListener { this.isVisible = true }
            popupMenu.add(itemDisplay)
            val itemExit = MenuItem("退出")
            itemExit.addActionListener { exitProcess(0) }
            popupMenu.add(itemExit)

            // 设置托盘图标
            val ico = ImageIcon(
                Objects.requireNonNull(MiraiBotConsoleUI::class.java.getResource("/assets/Icon16.png"))
            )
            val trayIcon = TrayIcon(ico.image, "MiraiBotConsole", popupMenu)
            trayIcon.addActionListener { this.isVisible = true }

            // 创建系统托盘
            try {
                SystemTray.getSystemTray().add(trayIcon)
            } catch (ignore: AWTException) { }
        }
        this.isVisible = true
    }

}