package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
/** Complete a box if a box can be completed, otherwise move randomly **/
class NormalAI: ComputerPlayer() {
    override fun makeMove(game: DotsAndBoxesGame) {
        val notDrawnLines = game.lines.filterNot { it.isDrawn }
        var lineToDraw = notDrawnLines.random()
        for (box in game.boxes) {
            val drawnLines = box.boundingLines.filter {it.isDrawn}
            // a box has 3 out of 4 required lines
            if (drawnLines.size == 3) {
                // we know this will return only one value but compiler does not know that
                lineToDraw = box.boundingLines.filterNot {it.isDrawn}.first()
            }
        }
        lineToDraw.drawLine()
    }
}