package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import org.example.student.dotsboxgame.EasyAI
import org.example.student.dotsboxgame.HardAI
import org.example.student.dotsboxgame.NormalAI
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame.GameChangeListener
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import kotlin.math.abs
import kotlin.math.round

class GameView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val backCol: Int = Color.GRAY
    private val lineCol: Int = Color.LTGRAY
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
    private var notDrawnLinePaint: Paint = Paint().apply {
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
            // columnWidth = 180
            // rowWidth = 264
            // for touching line at 0,0:
            val x = e.x // 308.99048
            val y = e.y // 272.9297
//            val notRoundedX = (x - columnWidth) / columnWidth // 2.2
//            val notRoundedY = (y - rowWidth) / rowWidth
            // (308 - 180) / 180 = 0.7
            // (272 - 264) / 264 = 0.03
            /** Whole number X co-ordinate of the closest box*/
            val closestXCoord = ((x - columnWidth) / columnWidth).toInt() // 1
            /** Whole number Y co-ordinate of the closest box*/
            val closestYCoord = ((y - rowWidth) / rowWidth).toInt() // 0
            if (closestYCoord in 0 until game.columns) {
                if (closestXCoord in 0 until game.rows) {
                    val closestBox = game.boxes[closestXCoord, closestYCoord]
//                    val distanceToVerticalLine = closestXCoord - notRoundedX // 0.2

                    // columnWidth = dotSpacingX
                    val sideLeft = (closestXCoord + 1) * columnWidth // 2 * columnWidth = 360
                    val sideRight = (closestXCoord + 2) * columnWidth // 3 * columnWidth = 540
                    val sideTop = (closestYCoord + 1) * rowWidth // 264
                    val sideBot = (closestYCoord + 2) * rowWidth // 528

                    val distanceRight = abs((sideRight - e.x)) // 231.00952
                    val distanceLeft = abs((e.x - sideLeft))
                    val distanceTop = abs((sideTop - e.y))
                    val distanceBot = abs((e.y - sideBot))
                    val distances = listOf(distanceTop, distanceLeft, distanceBot, distanceRight)
                    val smallestDistance = minOf(distanceTop, distanceLeft, distanceBot, distanceRight)
                    val lineTouched = distances.indexOf(smallestDistance)
                    val lines = closestBox.boundingLines.toList()
                    val lineToDraw = lines[lineTouched] // when 0,0 touched, with 1,0 detected, lineToDraw is line at 0,2 (one under it)
                    if (!lineToDraw.isDrawn) {
                        lineToDraw.drawLine()
                        //invalidate()
                        //true
                        val lineX = lineToDraw.lineX
                        val lineY = lineToDraw.lineY
                        Snackbar
                            .make(this@GameView , "SingleTapUp x= $x y= $y, " +
                                "closestXCoord = $closestXCoord, closestYCoord = $closestYCoord" +
                                "lineX = $lineX, lineY = $lineY", Snackbar.LENGTH_LONG).show()
                        return true

                    }
                }

            }

        return super.onSingleTapUp(e)
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
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

    val computerPlayer: NormalAI = NormalAI()
    val playersList: List<Player> = listOf(computerPlayer, HumanPlayer())
    // 4x4 game
    var game: StudentDotsBoxGame = StudentDotsBoxGame(6, 6, playersList)
        set(value) {
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    var listener = object: GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    init {
        game.addOnGameChangeListener(listener)
    }
    override fun onDraw(canvas: Canvas) {
        val scores = game.getScores()
        var textPlayer1: String = "Player 1: " + scores[0]
        var textPlayer2: String = "Player 2: " + scores[1]

        // Measure the size of the canvas
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()

        val columnWidth = canvasWidth / (game.columns + 1)
        val rowWidth = canvasHeight / (game.rows + 1)
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
        var linePaint: Paint = Paint().apply{

        }
        var boxPaint: Paint = Paint().apply{

        }
        val unownedBoxPaint: Paint = Paint().apply{
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        val player1BoxPaint: Paint = Paint().apply{
            color = Color.rgb(255, 0, 77)
        }
        val player2BoxPaint: Paint = Paint().apply{
            color = Color.rgb(0, 228, 54)
        }
        // draw boxes so they can be coloured in later
        for (x in 1..(game.columns-1)) {
            for (y in 1..(game.rows-1)) {
                val left = x * columnWidth
                val top = y * rowWidth
                val right = (x+1) * columnWidth
                val bottom = (y+1) * rowWidth

                if (game.boxes[x-1, y-1].owningPlayer != null) {
                    if (game.players.indexOf(game.boxes[x-1, y-1].owningPlayer) == 0) {
                        boxPaint = player1BoxPaint
                    }
                    else if (game.players.indexOf(game.boxes[x-1, y-1].owningPlayer) == 1) {
                        boxPaint = player2BoxPaint
                    }

                }
                else {
                    boxPaint = unownedBoxPaint
                }
                canvas.drawRect(left, top, right, bottom, boxPaint)
            }
        }

        // horizontal
        for (x in 1..(game.columns-1))  {
            for (y in 1..(game.rows)) {
                if (game.lines.isValid(x-1,(y-1)*2)) {
                    if ( game.lines[x-1,(y-1)*2].isDrawn) {
                        linePaint = drawnLinePaint
                        Log.d("horizontal", "Line drawn at" + (x-1) + ", " + (y-1)*2)
                    } else {
                        linePaint = notDrawnLinePaint
                    }
                }
                // y stays same, x goes up by 1
                val startX = x * columnWidth
                val stopX = (x + 1) * columnWidth
                val startY = y * rowWidth
                canvas.drawLine(
                    (startX), (startY), (stopX),
                    (startY) , linePaint)
            }
        }
        // vertical
        for (x in 1..(game.columns)) {
            for (y in 1..(game.rows-1)) {
                if (game.lines.isValid(x-1,(2*y-1))) {
                    if (game.lines[x-1,(2*y-1)].isDrawn) {
                        linePaint = drawnLinePaint
                        Log.d("vertical", "Line drawn at" + (x-1) + ", " + (2*y-1))
                    } else {
                        linePaint = notDrawnLinePaint
                    }
                }
                val start = x * columnWidth
                val stop = y * rowWidth
                canvas.drawLine( (start), (stop),
                    (start), ((y + 1)* rowWidth),
                    linePaint)
            }
        }

        // draw dots on top of lines
        for (x in 1..(game.columns)) {
            for (y in 1..(game.rows)) {
                val start = x* columnWidth
                val stop = y* rowWidth
                canvas.drawPoint(start, stop, dotPaint)
            }
        }
    }
/**
 * (touchCoord - leftMargin) / columns
 * (touchCoord - topMargin) / rows
 * */
}