package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.example.student.dotsboxgame.EasyAI
import org.example.student.dotsboxgame.StudentDotsBoxGame
import org.example.student.dotsboxgame.NamedHuman
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class GameView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val backCol: Int = Color.rgb(250,250,200)
    private val lineCol: Int = Color.GRAY
    private val wordCol: Int = Color.BLACK

    // Paint variables
    private var dotPaint: Paint = Paint().apply {
        // Controls the size of the dot
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND

        // Set the paint color
        color = Color.BLACK
    }
    private var backPaint: Paint = Paint().apply {
        // Set up the paint style
        style = Paint.Style.FILL
        color = backCol
    }
    private var borderPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = wordCol
        strokeWidth = 10f
    }
    private var linePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = lineCol
        strokeWidth = 10f
    }
    private var wordsPaint: Paint = Paint().apply {
        color = wordCol
        textAlign = Paint.Align.LEFT
        textSize = 50f
        typeface = Typeface.SANS_SERIF
    }

    val computerPlayer: EasyAI = EasyAI()
    val humanPlayer: NamedHuman = NamedHuman("Tara")
    val playersList: List<Player> = listOf(computerPlayer, humanPlayer)
    val game = StudentDotsBoxGame(columns = 3, rows = 3, players = playersList)




    override fun onDraw(canvas: Canvas) {
        val scores = game.getScores()
        var textPlayer1: String = "Player 1: " + scores[0]
        var textPlayer2: String = "Player 2: " + scores[1]
        // draw the View
        // Background
        // Measure the size of the canvas, we could take into account padding here
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()
        val textViewX = canvasWidth / 32f
        // placed in the initial 32nd of the canvas horizontally
        val textView1Y = canvasHeight / 16f
        // placed in the initial 16th of the canvas vertically
        val textView2Y = 15 * (canvasHeight / 16f)
        // placed in the final 16th of the canvas vertically
        // Padding
        val minX = paddingLeft
        val minY = paddingTop
        val maxX = width - paddingLeft - paddingRight
        val maxY = height - paddingTop - paddingBottom

        val xSep: Float = maxX.toFloat() / game.columns
        val ySep: Float = maxY.toFloat() / game.rows
        // Draw text

//        val textViewPlayer1 = findViewById<TextView?>(R.id.textViewPlayer1)
//        textViewPlayer1?.text = textPlayer1
//        val textViewPlayer2 = findViewById<TextView?>(R.id.textViewPlayer2)
//        textViewPlayer2?.text = textPlayer2

        canvas.drawText(textPlayer1, textViewX, textView1Y, wordsPaint)
        canvas.drawText(textPlayer2, textViewX, textView2Y, wordsPaint)
        // Draw rectangle with drawRect(topleftX, topLeftY, bottomRightX, bottomRightY, Paint)
        // Use Ctrl-P to see the parameters for a function
        canvas.drawRect(20f, 150f, canvasWidth - 16f, canvasHeight - 176f, backPaint)
        canvas.drawRect(20f, 150f, canvasWidth - 16f, canvasHeight - 176f, borderPaint)
        //val rows = game.rows + 1
        //val columns = game.columns + 1
        // draw lines first
        for (x in 0..(game.rows)) {
            for (y in 0..(game.columns)) {
//                //if (y % 2 != 0) {
//                canvas.drawLine(x * xSep + minX, y * ySep + minY, x * xSep,
//                    x * ySep, linePaint)
//                //}
//                // horizontal lines - start where dot starts, end on next column
//                //else {
//                canvas.drawLine(x * xSep + minX, y * ySep + minY, y * xSep - right,
//                    y * ySep - bottom, linePaint)
//                //}
            }
        }
//        // draw dots on top of lines
//        for (x in 1..(game.rows)) {
//            for (y in 1..(game.columns)) {
//                // starts where dot starts, ends on next row (vertical)
//                canvas.drawPoint(x * xSep + left, y * ySep + top, dotPaint)
//            }
//        }
    }

    // outputs a 5 x 6 grid of square-shaped red dots
    // no lines yet
    // left-aligned
    // new output: 3 x 3 grid, 16 dots, some lines drawn some not, top left aligned
    // new new output: 3x3 grid, 16 dots, about 6 lines drawn, fills whole screen no padding

}