package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
/** Complete a box if a box can be completed, otherwise select a random, valid, beneficial
 * move. A beneficial move is one that does not give the other player a free box.**/
class HardAI: ComputerPlayer() {
    override fun makeMove(game: DotsAndBoxesGame) {
        /** Initialised as a Mutable List of not drawn lines **/
        val moveOptions = game.lines.filterNot { it.isDrawn }.toMutableList()
        /** Immutable list of not drawn lines**/
        val notDrawnLines = game.lines.filterNot { it.isDrawn }
        var lineToDraw: DotsAndBoxesGame.Line
        first@ for (box in game.boxes) {
            val drawnLines = box.boundingLines.filter {it.isDrawn}
            // if the box has 3 lines drawn, so can be completed
            if (drawnLines.size == 3) {
                lineToDraw = box.boundingLines.filterNot { it.isDrawn }.first()
                // make this line the only option
                // ignores if this line was removed for satisfying else if clause of another box
                moveOptions.removeAll(moveOptions)
                moveOptions.add(lineToDraw)
                // break out of for loop and play this line
                break@first

            }
            // if the box has 2 lines drawn, so drawing a line would give next player a box
            else if (drawnLines.size == 2) {
                val badMoves = box.boundingLines.filterNot { it.isDrawn }
                moveOptions.removeAll(badMoves)
            }
        }
        // check that we have not removed ALL valid moves
        // if we have, computer is forced to move randomly
        lineToDraw = if (moveOptions.size > 0) {
            moveOptions.random()
        } else {
            notDrawnLines.random()
        }

        lineToDraw.drawLine()
    }
}