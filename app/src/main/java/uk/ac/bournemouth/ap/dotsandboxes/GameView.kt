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
        color = lineCol
        strokeWidth = 10f
    }
    private var linePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = lineCol
        strokeWidth = 10f
    }
    private var wordsPaint: Paint = Paint().apply {
        color = wordCol
        textAlign = Paint.Align.CENTER
        textSize = 100f
        typeface = Typeface.SANS_SERIF
    }

    val computerPlayer: EasyAI = EasyAI()
    val humanPlayer: NamedHuman = NamedHuman("Tara")
    val playersList: List<Player> = listOf(computerPlayer, humanPlayer)
    val game = StudentDotsBoxGame(columns = 3, rows = 3, players = playersList)
    val scores = game.getScores()
    val textPlayer1: String = "Player 1" //+ game.players[0] + scores[0]
    val textPlayer2: String = "Player 2"/// + game.players[1] + scores[1]


    override fun onDraw(canvas: Canvas) {

        // draw the View
        // Background
        // Measure the size of the canvas, we could take into account padding here
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()
        val textViewx = canvasWidth / 2f
        // 16dp from top of view (assuming top is 0)
        val textView1y = canvasHeight / 2f
        // 16dp from bottom of view (assuming bottom is 0)
        //val textView2y = canvasHeight / 4f
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

        canvas.drawText("Text", 100f, 100f, wordsPaint)
        //canvas.drawText(textPlayer2, textViewx, textView2y, wordsPaint)
        // Draw rectangle with drawRect(topleftX, topLeftY, bottomRightX, bottomRightY, Paint)
        // Use Ctrl-P to see the parameters for a function
        canvas.drawRect(125f, 125f, canvasWidth, canvasHeight, backPaint)
        canvas.drawRect(125f, 125f, canvasWidth, canvasHeight, borderPaint)
        //val rows = game.rows + 1
        //val columns = game.columns + 1
        // draw lines first
//        for (x in 0..(game.rows)) {
//            for (y in 0..(game.columns)) {
//                //if (y % 2 != 0) {
//                canvas.drawLine(x * xSep + minX, y * ySep + minY, x * xSep,
//                    x * ySep, linePaint)
//                //}
//                // horizontal lines - start where dot starts, end on next column
//                //else {
//                canvas.drawLine(x * xSep + minX, y * ySep + minY, y * xSep - right,
//                    y * ySep - bottom, linePaint)
//                //}
//            }
//        }
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