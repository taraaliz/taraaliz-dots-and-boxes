package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import kotlin.random.Random

class EasyAI: ComputerPlayer() {
  override fun makeMove(game: DotsAndBoxesGame) {
    /** Make a list of lines that have not been drawn, then pick and draw one at random**/
    //var notDrawnLines: List<DotsAndBoxesGame.Line> = mutableListOf()
    val notDrawnLines = game.lines.filterNot { it.isDrawn }
    val lineToDraw = notDrawnLines.random()
    lineToDraw.drawLine()
  }
}