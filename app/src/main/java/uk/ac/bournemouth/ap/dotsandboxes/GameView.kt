package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import org.example.student.dotsboxgame.EasyAI
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import kotlin.math.round

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
    private val drawnLineCol: Int = Color.BLACK
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
    private val player1Paint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }
    private val player2Paint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }


    private val gestureDetector = GestureDetectorCompat(context, object:
    GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = e.x * columnWidth
            val y = e.y * rowWidth
//            val notRoundedX = (x - columnWidth) / columnWidth // 2.2
//            val notRoundedY = (y - rowWidth) / rowWidth
            val closestXCoord = round((x - columnWidth) / columnWidth).toInt() // 2.0
            val closestYCoord = round((y - rowWidth) / rowWidth).toInt()
            if (closestYCoord in 0 until game.columns) {
                if (closestXCoord in 0 until game.rows) {
                    val closestBox = game.boxes[closestXCoord, closestYCoord]
//                    val distanceToVerticalLine = closestXCoord - notRoundedX // 0.2

                    // columnWidth = dotSpacingX
                    val sideLeft = (closestXCoord + 1) * columnWidth // 3 * columnWidth
                    val sideRight = (closestXCoord + 2) * columnWidth // 4 * columnWidth
                    val sideTop = (closestYCoord + 1) * rowWidth
                    val sideBot = (closestYCoord + 2) * rowWidth

                    val distanceRight = sideRight - e.x
                    val distanceLeft = e.x - sideLeft
                    val distanceTop = sideTop - e.y
                    val distanceBot = e.y - sideBot
                    val distances = listOf(sideTop, sideLeft, sideBot, sideRight)
                    val smallestDistance = distances.sorted()[0]
                    val lineTouched = distances.indexOf(smallestDistance)
                    val lines = closestBox.boundingLines.toList()
                    val lineToDraw = lines[lineTouched]
                    if (!lineToDraw.isDrawn) {
                        lineToDraw.drawLine()
                    }
                }

            }

        Snackbar.make(this@GameView , "SingleTapUp x= $x y= $y, closestXCoord = $closestXCoord, closestYCoord = $closestYCoord", Snackbar.LENGTH_LONG).show()
        return true

        return super.onSingleTapUp(e)
        }
    })
    // Padding initialisers, these get set in onSizeChanged
    var rowWidth = 0f
    var columnWidth = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // things that need to be recalculated if size changes
        rowWidth = height / (game.rows + 1).toFloat()
        columnWidth = width / (game.columns + 1).toFloat()
        dotSize = width / 64f
    }

    val computerPlayer: EasyAI = EasyAI()
    val playersList: List<Player> = listOf(computerPlayer, HumanPlayer())
    // 4x4 game
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

        // Measure the size of the canvas
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()

        val dotSpacingX = canvasWidth / (game.columns + 1)
        val dotSpacingY = canvasHeight / (game.rows + 1)
        // Text Location
        val textViewX = canvasWidth / 32f
        // placed in the initial 32nd of the canvas horizontally
        val textView1Y = canvasHeight / 16f
        // placed in the initial 16th of the canvas vertically
        val textView2Y = 15 * (canvasHeight / 16f)
        // placed in the final 16th of the canvas vertically

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
                    (start), (stop), (x + 1 * dotSpacingX),
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
                    (start), (y + 1* dotSpacingY),
                    linePaint)
            }
        }

        // draw dots on top of lines
        for (x in 1..(game.columns)) {
            for (y in 1..(game.rows)) {
                val start = x* dotSpacingX
                val stop = y* dotSpacingY
                canvas.drawPoint(start, stop, dotPaint)
            }
        }
    }
/**
 * (touchCoord - leftMargin) / columns
 * (touchCoord - topMargin) / rows
 * */
}