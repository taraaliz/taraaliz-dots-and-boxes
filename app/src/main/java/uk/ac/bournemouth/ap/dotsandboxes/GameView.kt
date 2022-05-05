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
import org.example.student.dotsboxgame.NormalAI
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame.GameChangeListener
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import kotlin.math.abs

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
    private var circleSize = 80f
    private val player1Col = Color.rgb(255, 0, 77)
    private val player2Col = Color.rgb(0, 228, 54)

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
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }
    // colours for the circles beside the player text
    private val player1Paint: Paint = Paint().apply {
        strokeWidth = circleSize
        color = player1Col
        strokeCap = Paint.Cap.ROUND
    }
    private val player2Paint: Paint = Paint().apply {
        strokeWidth= circleSize
        color = player2Col
        strokeCap = Paint.Cap.ROUND
    }

    private var linePaint: Paint = Paint().apply{

    }
    var boxPaint: Paint = Paint().apply{

    }
    private val unownedBoxPaint: Paint = Paint().apply{
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val player1BoxPaint: Paint = Paint().apply{
        color = Color.rgb(255, 0, 77)
    }
    private val player2BoxPaint: Paint = Paint().apply{
        color = Color.rgb(0, 228, 54)
    }

    private val gestureDetector = GestureDetectorCompat(context, object:
    GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val x = e.x
            val y = e.y

            /** Whole number X co-ordinate of the closest box*/
            val closestXCoord = ((x - columnWidth) / columnWidth).toInt()
            /** Whole number Y co-ordinate of the closest box*/
            val closestYCoord = ((y - rowWidth) / rowWidth).toInt()
            if (closestYCoord in 0 until game.columns) {
                if (closestXCoord in 0 until game.rows) {
                    val closestBox = game.boxes[closestXCoord, closestYCoord]
                    val sideLeft = (closestXCoord + 1) * columnWidth
                    val sideRight = (closestXCoord + 2) * columnWidth
                    val sideTop = (closestYCoord + 1) * rowWidth
                    val sideBot = (closestYCoord + 2) * rowWidth

                    val distanceRight = abs((sideRight - e.x))
                    val distanceLeft = abs((e.x - sideLeft))
                    val distanceTop = abs((sideTop - e.y))
                    val distanceBot = abs((e.y - sideBot))
                    val distances = listOf(distanceTop, distanceLeft, distanceBot, distanceRight)
                    val smallestDistance = minOf(distanceTop, distanceLeft, distanceBot, distanceRight)
                    val lineTouched = distances.indexOf(smallestDistance)
                    val lines = closestBox.boundingLines.toList()
                    val lineToDraw = lines[lineTouched]
                    if (!lineToDraw.isDrawn) {
                        lineToDraw.drawLine()
                        val lineX = lineToDraw.lineX
                        val lineY = lineToDraw.lineY
                        Snackbar
                            .make(this@GameView , "SingleTapUp x= $x y= $y, " +
                                "boxXCoord = $closestXCoord, boxYCoord = $closestYCoord" +
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
    var rowWidth2 = 0f
    var columnWidth2 = 0f
    var columnWidth = minOf(rowWidth2, columnWidth2)
    var rowWidth = maxOf(rowWidth2, columnWidth2)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // things that need to be recalculated if size changes
        rowWidth2 = height / (game.rows +2).toFloat()
        columnWidth2 = width / (game.columns +2).toFloat()
        columnWidth = minOf(rowWidth2, columnWidth2)
        rowWidth = maxOf(rowWidth2, columnWidth2)
        dotSize = width / 64f
    }

    private val computerPlayer: NormalAI = NormalAI()
    private val playersList: List<Player> = listOf(HumanPlayer(), computerPlayer)
    // 4x4 game
    var game: StudentDotsBoxGame = StudentDotsBoxGame(4, 4, playersList)
        set(value) {
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    private var listener = object: GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    init {
        game.addOnGameChangeListener(listener)
    }

    override fun onDraw(canvas: Canvas) {

                val scores = game.getScores()
                val textPlayer1: String = "Player 1: " + scores[0]
                val textPlayer2: String = "Player 2: " + scores[1]

                // Measure the size of the canvas
                val canvasWidth = width.toFloat()
                val canvasHeight = height.toFloat()

                // Text Location
                val textViewX = 2* canvasWidth / 16f
                // placed in the initial 32nd of the canvas horizontally
                val textView1Y = canvasHeight / 16f
                // placed in the initial 16th of the canvas vertically
                val textView2Y = 15 * (canvasHeight / 16f)
                // placed in the final 16th of the canvas vertically

                // Player Colour Location
                val colourX = canvasWidth / 16f
                canvas.drawPoint(colourX, textView1Y, player1Paint)
                canvas.drawPoint(colourX, textView2Y, player2Paint)

                // Draw text
                canvas.drawText(textPlayer1, textViewX, textView1Y, wordsPaint)
                canvas.drawText(textPlayer2, textViewX, textView2Y, wordsPaint)

                // Draw rectangle with drawRect(topleftX, topLeftY, bottomRightX, bottomRightY, Paint)
                // Use Ctrl-P to see the parameters for a function
                canvas.drawRect(20f, 150f, canvasWidth - 16f, canvasHeight - 150f, backPaint)
                canvas.drawRect(20f, 150f, canvasWidth - 16f, canvasHeight - 150f, borderPaint)

                // draw boxes so they can be coloured in later
                for (x in 0..(game.columns-1)) {
                    for (y in 0..(game.rows-1)) {
                        val left = (x+1) * columnWidth
                        val top = (y+1) * rowWidth
                        val right = (x+2) * columnWidth
                        val bottom = (y+2) * rowWidth

                        if (game.boxes[x, y].owningPlayer != null) {
                            if (game.players.indexOf(game.boxes[x, y].owningPlayer) == 0) {
                                boxPaint = player1BoxPaint
                            }
                            else if (game.players.indexOf(game.boxes[x, y].owningPlayer) == 1) {
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
                for (x in 0..(game.columns-1))  {
                    for (y in 0..(game.rows)) {
                        if (game.lines.isValid(x,(y)*2)) {
                            if (game.lines[x,(y)*2].isDrawn) {
                                linePaint = drawnLinePaint
                            } else {
                                linePaint = notDrawnLinePaint
                            }
                        }
                        // y stays same, x goes up by 1
                        val startX = (x+1) * columnWidth
                        val stopX = (x + 2) * columnWidth
                        val startY = (y+1) * rowWidth
                        canvas.drawLine(
                            (startX), (startY), (stopX),
                            (startY) , linePaint)
                    }
                }
                // vertical
                for (x in 0..(game.columns)) {
                    for (y in 0..(game.rows-1)) {
                        if (game.lines.isValid(x,(2*y+1))) {
                            if (game.lines[x,(2*y+1)].isDrawn) {
                                linePaint = drawnLinePaint
                            } else {
                                linePaint = notDrawnLinePaint
                            }
                        }
                        val start = (x+1) * columnWidth
                        val stop = (y +1) * rowWidth
                        canvas.drawLine( (start), (stop),
                            (start), ((y + 2)* rowWidth),
                            linePaint)
                    }
                }

                // draw dots on top of lines
                for (x in 0..(game.columns)) {
                    for (y in 0..(game.rows)) {
                        val start = (x+1)* columnWidth
                        val stop = (y+1)* rowWidth
                        canvas.drawPoint(start, stop, dotPaint)
                    }
                }
            }
}