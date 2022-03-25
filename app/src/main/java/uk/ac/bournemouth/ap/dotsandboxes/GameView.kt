package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
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
  private var linePaint: Paint = Paint().apply {
    style = Paint.Style.STROKE
    color = lineCol
  }
  private val computerPlayer: EasyAI = EasyAI()
  private val humanPlayer: NamedHuman = NamedHuman("Tara")
  private val playersList: List<Player> = listOf(computerPlayer, humanPlayer)
  val game = StudentDotsBoxGame(columns = 3, rows = 3, players = playersList)



  override fun onDraw(canvas: Canvas) {
    // draw the View
    // Background
    // Measure the size of the canvas, we could take into account padding here
    val canvasWidth = width.toFloat() + 10f
    val canvasHeight = height.toFloat() + 10f
    var xSep: Float = canvasWidth / game.columns
    var ySep: Float = canvasHeight / game.rows

    // Draw rectangle with drawRect(topleftX, topLeftY, bottomRightX, bottomRightY, Paint)
    // Use Ctrl-P to see the parameters for a function
    //canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backPaint)

    for (x in 0..(game.rows )) {
      for (y in 0..(game.columns)) {
        canvas.drawPoint(x * xSep, y * ySep, dotPaint)
        // starts where dot starts, ends on next row (vertical)
        if (y % 2 != 0) {
          canvas.drawLine(x * xSep, y * ySep, x * xSep, x * ySep, linePaint)
        }
        // horizontal lines - start where dot starts, end on next column
        else {
          canvas.drawLine(x * xSep, y * ySep, y * xSep, y * ySep, linePaint)
        }
      }
    }
  }

  // outputs a 5 x 6 grid of square-shaped red dots
  // no lines yet
  // left-aligned
  // new output: 3 x 3 grid, 16 dots, some lines drawn some not, top left aligned
  // new new output: 3x3 grid, 16 dots, about 6 lines drawn, fills whole screen no padding

}