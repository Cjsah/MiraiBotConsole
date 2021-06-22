package net.cjsah.bot.console.gui


import javax.swing.*
import java.awt.*
import java.awt.event.*

@Deprecated("wait to update")
@Suppress("FloatWindows")
object FloatWindow {
    var mouseAtX = 0
    var mouseAtY = 0
    @kotlin.jvm.JvmStatic
    fun floatWindow() {
        val kit: Toolkit = Toolkit.getDefaultToolkit()
        val sc: Dimension = kit.screenSize
        val jf = JFrame()
        jf.setSize(256, 384)
        jf.setLocation(sc.width - 128, sc.height / 3)
        jf.isUndecorated = true //窗口去边框
        jf.isAlwaysOnTop = true //设置窗口总在最前
        jf.background = Color(0, 0, 0, 0) //设置窗口背景为透明色

        jf.addMouseListener(object : MouseAdapter() {
            //设置窗口可拖动，添加监听器
            override fun mousePressed(e: MouseEvent) {        //获取点击鼠标时的坐标
                mouseAtX = e.point.x
                mouseAtY = e.point.y
            }
        })
        jf.addMouseMotionListener(object : MouseMotionAdapter() {
            //设置拖拽后，窗口的位置
            override fun mouseDragged(e: MouseEvent) {
                jf.setLocation(e.xOnScreen - mouseAtX, e.yOnScreen - mouseAtY)
            }
        })

        val botIcon = ImageIcon("\\src\\main\\resources\\Icon.png") //实例化图像对象以作为窗口主题贴图
        val l = JLabel(botIcon) //把上面的主题贴图添加到标签对象里面去
        l.setBounds(0, 0, 256, 256) //设置标签对象大小及位置

        //按钮添加
        val buttonIcon = ImageIcon("\\src\\main\\resources\\start.png") //实例化图像对象以作为按钮贴图
        val button = JButton(buttonIcon) //将上面的图像对象设置为按钮贴图
        button.isContentAreaFilled = false //设置按钮背景透明
        button.isBorderPainted = false //去掉按钮边框
        button.setBounds(0, 256, 256, 128) //设置按钮大小及位置

        val p = JPanel()
        p.layout = null
        p.add(l)
        p.add(button)
        p.isOpaque = false
        jf.contentPane.add(p)
        jf.show()
    }
}


