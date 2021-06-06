package net.cjsah.bot.console.ui

import javax.swing.JFrame
import javax.swing.JTextField
import javax.swing.JPasswordField
import javax.swing.JTabbedPane
import javax.swing.JPanel
import javax.swing.border.TitledBorder
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants
import javax.swing.JTextArea
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListSelectionModel
import javax.swing.JButton
import javax.swing.JCheckBox
import java.awt.*
/*
import kotlin.jvm.JvmStatic
import java.lang.Exception
import javax.swing.event.*
*/

/*
Written by G-Lemon
 */

class GUI {
    private var frmQq: JFrame? = null
    private var command: JTextField? = null
    private var accountTextfield: JTextField? = null
    private var passwordField: JPasswordField? = null
    fun uiDisplay() {
        frmQq = JFrame()
        frmQq!!.title = "MariaBotConsole"
        frmQq!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frmQq!!.setBounds(100, 100, 900, 600)

        val tabbedPane = JTabbedPane()
        frmQq!!.contentPane.add(tabbedPane, BorderLayout.CENTER)
        val console = Panel()
        console.background = Color.LIGHT_GRAY
        tabbedPane.addTab("控制台", null, console, null)
        tabbedPane.setEnabledAt(0, true)
        console.layout = BorderLayout(0, 0)
        command = JTextField()
        console.add(command, BorderLayout.SOUTH)
        command!!.columns = 255
        val log = JPanel()
        log.border = TitledBorder(null, "日志", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        console.add(log, BorderLayout.CENTER)
        log.layout = BorderLayout(0, 0)
        val logScrollpane = JScrollPane()
        logScrollpane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        log.add(logScrollpane)
        val logTextarea = JTextArea()
        logTextarea.isEditable = false
        logScrollpane.setViewportView(logTextarea)
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
        val pluginScrollpane = JScrollPane()
        pluginScrollpane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        pluginListPanel.add(pluginScrollpane, BorderLayout.CENTER)
        val pluginList: JList<*> = JList<Any?>()
        pluginList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        pluginScrollpane.setViewportView(pluginList)
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
        val friendscroll = JScrollPane()
        friendscroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        friendPanel.add(friendscroll)
        val friendlist = JTextArea()
        friendlist.isEditable = false
        friendscroll.setViewportView(friendlist)
        val groupPanel = JPanel()
        groupPanel.border =
            TitledBorder(null, "群组列表", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        friend.add(groupPanel)
        groupPanel.layout = BorderLayout(0, 0)
        val groupscroll = JScrollPane()
        groupscroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        groupPanel.add(groupscroll)
        val grouplist = JTextArea()
        grouplist.isEditable = false
        groupscroll.setViewportView(grouplist)
        tabbedPane.setBackgroundAt(2, SystemColor.control)
        val config = Panel()
        tabbedPane.addTab("设置", null, config, null)
        config.layout = BorderLayout(0, 0)
        val accountpanelctrl = JPanel()
        config.add(accountpanelctrl, BorderLayout.CENTER)
        accountpanelctrl.layout = BorderLayout(0, 0)
        val ctrlbuttonpanel = JPanel()
        accountpanelctrl.add(ctrlbuttonpanel, BorderLayout.NORTH)
        ctrlbuttonpanel.layout = GridLayout(1, 0, 0, 0)
        val edit = JButton("编辑")
        ctrlbuttonpanel.add(edit)
        val exit = JButton("退出")
        ctrlbuttonpanel.add(exit)
        val account = JPanel()
        account.border = TitledBorder(null, "账户", TitledBorder.LEADING, TitledBorder.TOP, null, null)
        config.add(account, BorderLayout.NORTH)
        account.layout = BorderLayout(0, 0)
        val accountinputPanel = JPanel()
        account.add(accountinputPanel, BorderLayout.NORTH)
        accountinputPanel.layout = BorderLayout(0, 0)
        val accountLabel = JLabel("账户：")
        accountinputPanel.add(accountLabel, BorderLayout.WEST)
        accountTextfield = JTextField()
        accountinputPanel.add(accountTextfield)
        accountTextfield!!.columns = 10
        val passwordinputPanel = JPanel()
        account.add(passwordinputPanel)
        passwordinputPanel.layout = BorderLayout(0, 0)
        val passwordLabel = JLabel("密码：")
        passwordinputPanel.add(passwordLabel, BorderLayout.WEST)
        passwordField = JPasswordField()
        passwordField!!.toolTipText = ""
        passwordinputPanel.add(passwordField, BorderLayout.CENTER)
        val passwordVis = JCheckBox("显示密码")
        passwordinputPanel.add(passwordVis, BorderLayout.SOUTH)
        tabbedPane.setBackgroundAt(3, SystemColor.control)

        command!!.addActionListener { }

        EventQueue.invokeLater {
            try {
                val window = GUI()
                window.frmQq!!.isVisible = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



