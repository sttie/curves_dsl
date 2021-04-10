import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.lang.Double.max
import java.lang.Double.min
import java.security.Key
import java.util.*
import javax.swing.*
import kotlin.math.abs


private object LinesComponent : JComponent() {
    val points = ArrayList<LinkedList<Point2D.Double>>()
    private var xOffset: Double = 0.0
    private var yOffset: Double = 0.0

    fun addPoint(x: Double, y: Double) {
        points.last().add(Point2D.Double(x, y))
        repaint()
    }

    fun nextList() {
        points.add(LinkedList())
    }

    fun addOffset(x: Double, y: Double) {
        xOffset += x
        yOffset += y
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        for (i in 0 until points.size) {
            for (j in 0 until (points[i].size - 1))
                (g as Graphics2D).draw(Line2D.Double(
                    points[i][j].x   + xOffset,
                    points[i][j].y   + yOffset,
                    points[i][j+1].x + xOffset,
                    points[i][j+1].y + yOffset))
        }
    }
}

object PlotWindow : JFrame() {
    private const val step: Double = 0.02
    var scale: Double = 50.0

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        LinesComponent.preferredSize = Dimension(700, 700)
        add(LinesComponent, BorderLayout.CENTER)

        addKeyListener(object : KeyListener {
            override fun keyPressed(event: KeyEvent) {
                when (event.keyCode) {
                    KeyEvent.VK_LEFT -> { LinesComponent.addOffset(10.0, 0.0) }
                    KeyEvent.VK_UP -> { LinesComponent.addOffset(0.0, 10.0) }
                    KeyEvent.VK_RIGHT -> { LinesComponent.addOffset(-10.0, 0.0) }
                    KeyEvent.VK_DOWN -> { LinesComponent.addOffset(0.0, -10.0) }
                }
                LinesComponent.repaint()
            }

            override fun keyTyped(p0: KeyEvent?) {}
            override fun keyReleased(p0: KeyEvent?) {}
        })
    }

    fun addCurve(curve: Curve, ) {
        LinesComponent.nextList()
        val range = curve.getRangeList()

        for (i in 0 until (range.size - 1)) {
            var current = range[i].toDouble()
            while (current <= range[i + 1].toDouble()) {
                val xy = curve.evaluate(current)
                LinesComponent.addPoint(xy.first * scale, xy.second * scale)
                current += step
            }
        }
    }
}