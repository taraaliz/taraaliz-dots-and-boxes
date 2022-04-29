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
    private val drawnLineCol: Int = Color.RED
    private val wordCol: Int = Color.BLACK
    private var dotSize: Float = 30f

    // Paint variables
    private var dotPaint: Paint = Paint().apply {
        // Controls the size of the dot
        strokeWidth = dotSize
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
        strokeWidth = 30f
    }
    private var drawnLinePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = drawnLineCol
        strokeWidth = 30f
    }
    private var wordsPaint: Paint = Paint().apply {
        color = wordCol
        textAlign = Paint.Align.LEFT
        textSize = 50f
        typeface = Typeface.SANS_SERIF
    }
    // Padding
    var dotSpacingY = 0f
    var dotSpacingX = 0f
    val leftPadding = 1

    private var leftMargin = 300

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // things that need to be recalculated if size changes
        dotSpacingY = (height - leftMargin) / (game.rows + 1).toFloat()
        dotSpacingX = (width - leftMargin) / (game.columns + 1).toFloat()
        dotSize = width / 64f
    }

    val computerPlayer: EasyAI = EasyAI()
    val playersList: List<Player> = listOf(computerPlayer, HumanPlayer())
    var game: StudentDotsBoxGame = StudentDotsBoxGame(5, 5, playersList)
        set(value) {
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()

        }


    override fun onDraw(canvas: Canvas) {
        val scores = game.getScores()
        var textPlayer1: String = "Player 1: " + scores[0]
        var textPlayer2: String = "Player 2: " + scores[1]
        // draw the View
        // Measure the size of the canvas, we could take into account padding here
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()
        val dotSpacingX = canvasWidth / (game.columns + 1)
        val dotSpacingY = (canvasHeight) / (game.rows + 1)
        // Text Location
        val textViewX = canvasWidth / 32f
        // placed in the initial 32nd of the canvas horizontally
        val textView1Y = canvasHeight / 16f
        // placed in the initial 16th of the canvas vertically
        val textView2Y = 15 * (canvasHeight / 16f)
        // placed in the final 16th of the canvas vertically
        // Padding

        // Draw text

        canvas.drawText(textPlayer1, textViewX, textView1Y, wordsPaint)
        canvas.drawText(textPlayer2, textViewX, textView2Y, wordsPaint)
        // Draw rectangle with drawRect(topleftX, topLeftY, bottomRightX, bottomRightY, Paint)
        // Use Ctrl-P to see the parameters for a function
        canvas.drawRect(20f, 150f, canvasWidth - 16f, canvasHeight - 176f, backPaint)
        canvas.drawRect(20f, 150f, canvasWidth - 16f, canvasHeight - 176f, borderPaint)

        // horizontal
        for (x in 1..game.columns) {
            for (y in 1..game.rows) {
//                if (game.lines[x,y*2].isDrawn) {
//                    linePaint = drawnLinePaint
//                }
                // y stays same, x goes up by 1
                val start = x * dotSpacingX
                val stop = y * dotSpacingY
                canvas.drawLine(
                    (start), (stop), (x + 1 * dotSpacingX + 1),
                    (stop) , linePaint)
            }
        }
        // vertical
        for (x in 1..game.columns) {
            for (y in 1..game.rows) {
//                if (game.lines[x,y*2].isDrawn) {
//                    linePaint = drawnLinePaint
//                }
                val start = x * dotSpacingX
                val stop = y * dotSpacingY
                canvas.drawLine( (start), (stop),
                    (start), (y + 1* dotSpacingY + 1),
                    linePaint)
            }
        }

        // draw dots on top of lines
        for (x in 1..(game.columns)) {
            for (y in 1..(game.rows)) {
                val start = x* dotSpacingX
                val stop = y* dotSpacingY
                // starts where dot starts, ends on next row (vertical)
                canvas.drawPoint(start, stop, dotPaint)
            }
        }
    }

}