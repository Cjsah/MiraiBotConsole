import javax.swing.JFrame

class TestUI : JFrame("MariaBotConsole") {
    init {
        this.setBounds(100, 100, 900, 600)
        this.isResizable = false
        //this.defaultCloseOperation = EXIT_ON_CLOSE
        this.defaultCloseOperation = EXIT_ON_CLOSE

        val image = toolkit.getImage(this.javaClass.getResource("/assets/Icon.ico"))

        this.iconImage = image

        this.isVisible = true

//        val image = JLabel(ImageIcon(this.javaClass.getResource("/assets/Icon.png")))
//        println(image)
//
//        this.contentPane.add(image)

    }
}