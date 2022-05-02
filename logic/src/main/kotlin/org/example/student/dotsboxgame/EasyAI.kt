package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import kotlin.random.Random
/** Make a list of lines that have not been drawn, then pick and draw one at random**/
class EasyAI: ComputerPlayer() {
  override fun makeMove(game: DotsAndBoxesGame) {
    val notDrawnLines = game.lines.filterNot { it.isDrawn }
    val lineToDraw = notDrawnLines.random()
    lineToDraw.drawLine()
  }
}