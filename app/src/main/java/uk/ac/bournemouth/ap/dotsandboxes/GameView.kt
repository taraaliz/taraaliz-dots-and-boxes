package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.example.student.dotsboxgame.EasyAI
import org.example.student.dotsboxgame.StudentDotsBoxGame
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

  private val circleCol: Int = Color.RED
  private val backCol: Int = Color.rgb(250,250,200)

  // Paint variables
  private var dotPaint: Paint
  private var circlePaint: Paint
  private var backPaint: Paint

  private var xSep: Float = 130f
  private var ySep: Float = 200f

  init {

    circlePaint = Paint().apply {
      setStyle(Paint.Style.FILL)
      setAntiAlias(true)

      // set the paint color using the circle color specified
      setColor(circleCol)
    }

    backPaint = Paint().apply {
      // Set up the paint style
      setStyle(Paint.Style.FILL)
      setColor(backCol)
    }

    dotPaint = Paint().apply {
      // Controls the size of the dot
      setStrokeWidth(20f)
      setStrokeCap(Paint.Cap.SQUARE)

      // Set the paint color
      setColor(Color.RED)
    }
  }
  val computerPlayer: EasyAI = TODO()
  val humanPlayer: HumanPlayer = TODO()
  val playersList: List<Player> = listOf(computerPlayer, humanPlayer)
  val game = StudentDotsBoxGame(columns = 3, rows = 4, players = playersList)
  override fun onDraw(canvas: Canvas) {
    // draw the View
    // Background
    // Measure the size of the canvas, we could take into account padding here
    val canvasWidth = width.toFloat()
    val canvasHeight = height.toFloat()

    // Draw rectangle with drawRect(topleftX, topLeftY, bottomRightX, bottomRightY, Paint)
    // Use Ctrl-P to see the parameters for a function
    canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backPaint)

    for (x in 1..5) {
      for (y in 1..10) {
        canvas.drawPoint(x * xSep, y * ySep, dotPaint)
        canvas.drawLine(x * xSep, y * ySep, linePaint)
      }
    }
  }

  // outputs a 5 x 6 grid of square-shaped red dots
  // no lines yet
  // left-aligned

}