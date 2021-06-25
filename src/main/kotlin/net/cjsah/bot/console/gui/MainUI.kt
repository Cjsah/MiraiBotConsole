package net.cjsah.bot.console.gui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.Panel
import java.awt.SystemColor
import javax.swing.*
import javax.swing.border.TitledBorder

//@Deprecated("wait to update")
class MainUI : JFrame("MariaBotConsole") {

    private var command = JTextField()
    private var accountTextField = JTextField()
    private var passwordField = JPasswordField()

    init {
        this.setBounds(100, 100, 900, 600)
        this.isResizable = false
        this.defaultCloseOperation = EXIT_ON_CLOSE
        val tabbedPane = JTabbedPane()
        this.contentPane.add(tabbedPane, BorderLayout.CENTER)
        val console = Panel()
        console.background = Color.LIGHT_GRAY
        tabbedPane.addTab("控制台", null, console, null)
        tabbedPane.setEnabledAt(0, true)
        console.layout = BorderLayout(0, 0)
        console.add(command, BorderLayout.SOUTH)
        command.columns = 255
        val log = JPanel()
        log.border = TitledBorder(null, "日志", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        console.add(log, BorderLayout.CENTER)
        log.layout = BorderLayout(0, 0)
        val logScrollPane = JScrollPane()
        logScrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        log.add(logScrollPane)
        val logTextarea = JTextArea()
        logTextarea.isEditable = false
        logScrollPane.setViewportView(logTextarea)
        val state = JPanel()
        state.border =
            TitledBorder(null, "在线状态", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        console.add(state, BorderLayout.NORTH)
        state.layout = BorderLayout(0, 0)
        val stateLabel = JLabel("在线")
        state.add(stateLabel, BorderLayout.CENTER)
        val plugin = Panel()
        tabbedPane.addTab("插件管理", null, plugin, null)
        plugin.layout = BorderLayout(0, 0)
        val pluginListPanel = JPanel()
        pluginListPanel.border =
            TitledBorder(null, "插件列表", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        plugin.add(pluginListPanel, BorderLayout.WEST)
        pluginListPanel.layout = BorderLayout(0, 0)
        val pluginScrollPane = JScrollPane()
        pluginScrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        pluginListPanel.add(pluginScrollPane, BorderLayout.CENTER)
        val pluginList: JList<*> = JList<Any?>()
        pluginList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        pluginScrollPane.setViewportView(pluginList)
        val readmeCtrlPanel = JPanel()
        plugin.add(readmeCtrlPanel)
        readmeCtrlPanel.layout = BorderLayout(0, 0)
        val scrollPane = JScrollPane()
        scrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        readmeCtrlPanel.add(scrollPane, BorderLayout.CENTER)
        val textArea = JTextArea()
        scrollPane.setViewportView(textArea)
        val ctrlPanel = JPanel()
        ctrlPanel.border =
            TitledBorder(null, "控制按钮", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        readmeCtrlPanel.add(ctrlPanel, BorderLayout.SOUTH)
        ctrlPanel.layout = GridLayout(2, 2, 0, 0)
        val addButton = JButton("添加插件")
        ctrlPanel.add(addButton)
        val delButton = JButton("删除插件")
        ctrlPanel.add(delButton)
        val reloadButton = JButton("重新加载")
        ctrlPanel.add(reloadButton)
        val configButton = JButton("配置文件")
        ctrlPanel.add(configButton)
        tabbedPane.setBackgroundAt(1, SystemColor.control)
        tabbedPane.setEnabledAt(1, true)
        val friend = Panel()
        tabbedPane.addTab("联系人", null, friend, null)
        friend.layout = GridLayout(1, 0, 0, 0)
        val friendPanel = JPanel()
        friendPanel.border =
            TitledBorder(null, "好友列表", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        friend.add(friendPanel)
        friendPanel.layout = BorderLayout(0, 0)
        val friendScroll = JScrollPane()
        friendScroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        friendPanel.add(friendScroll)
        val friendList = JTextArea()
        friendList.isEditable = false
        friendScroll.setViewportView(friendList)
        val groupPanel = JPanel()
        groupPanel.border =
            TitledBorder(null, "群组列表", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        friend.add(groupPanel)
        groupPanel.layout = BorderLayout(0, 0)
        val groupScroll = JScrollPane()
        groupScroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        groupPanel.add(groupScroll)
        val groupList = JTextArea()
        groupList.isEditable = false
        groupScroll.setViewportView(groupList)
        tabbedPane.setBackgroundAt(2, SystemColor.control)
        val config = Panel()
        tabbedPane.addTab("设置", null, config, null)
        config.layout = BorderLayout(0, 0)
        val accountPanelCtrl = JPanel()
        config.add(accountPanelCtrl, BorderLayout.CENTER)
        accountPanelCtrl.layout = BorderLayout(0, 0)
        val ctrlButtonPanel = JPanel()
        accountPanelCtrl.add(ctrlButtonPanel, BorderLayout.NORTH)
        ctrlButtonPanel.layout = GridLayout(1, 0, 0, 0)
        val edit = JButton("编辑")
        ctrlButtonPanel.add(edit)
        val exit = JButton("退出")
        ctrlButtonPanel.add(exit)
        val account = JPanel()
        account.border = TitledBorder(null, "账户", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        config.add(account, BorderLayout.NORTH)
        account.layout = BorderLayout(0, 0)
        val accountInputPanel = JPanel()
        account.add(accountInputPanel, BorderLayout.NORTH)
        accountInputPanel.layout = BorderLayout(0, 0)
        val accountLabel = JLabel("账户：")
        accountInputPanel.add(accountLabel, BorderLayout.WEST)
        accountInputPanel.add(accountTextField)
        accountTextField.columns = 10
        val passwordInputPanel = JPanel()
        account.add(passwordInputPanel)
        passwordInputPanel.layout = BorderLayout(0, 0)
        val passwordLabel = JLabel("密码：")
        passwordInputPanel.add(passwordLabel, BorderLayout.WEST)
        passwordField.toolTipText = ""
        passwordInputPanel.add(passwordField, BorderLayout.CENTER)
        val passwordVis = JCheckBox("显示密码")
        passwordInputPanel.add(passwordVis, BorderLayout.SOUTH)
        tabbedPane.setBackgroundAt(3, SystemColor.control)

        command.addActionListener { }

        this.isVisible = true

    }

}