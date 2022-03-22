package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.lib.matrix.ArrayMutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import kotlin.random.Random

class StudentDotsBoxGame (val columns: Int, val rows: Int, players: List<Player>): AbstractDotsAndBoxesGame() {
    override val players: List<Player> = players

    override val currentPlayer: Player = players[0]

    // NOTE: you may want to be more specific in the box type if you use that type in your class
    override val boxes: Matrix<StudentBox> = Matrix(columns, rows) { x, y -> StudentBox(x, y)}

    override val lines: MutableSparseMatrix<StudentLine> = MutableSparseMatrix(columns+1,
        rows*2+1, { x, y -> x < columns && (y % 2) != 0}, ::StudentLine)

    override val isFinished: Boolean = false
    // no getter provided as kotlin provides them by default, unless we need to do anything fancy

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false

        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                // no strict formula, depends on if line is horizontal or vertical
                // most are x-1, y-1 and x-1, y (horizontal)
                // vertical is x,y, and x-1, y
                // if odd y
                if (if lineX % 2 != 0){
                    x = 0
                    y = 0
                    a = 0
                    b = 0
                }
                return boxes[x, y] to boxes[a, b]
                // return Pair(Box1, Box2)
                TODO("You need to look up the correct boxes for this to work")
                // box - 1, box + 1? but how do we access box coords?
            }

        override fun drawLine() {
            TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player.
            // does any logic go here? yes, all logic goes here, this is where isDrawn changes
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override val owningPlayer: Player? = null
        // no getter or setter made, default value null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() = listOf(StudentLine(boxX, boxY), StudentLine(boxX + 1, boxY),
                StudentLine(boxX, boxY + 1), StudentLine(boxX + 1, boxY + 1))
                //TODO("Look up the correct lines from the game outer class")
        // cant do 'this' keyword, so how do we refer to game outer class?
            // get box coords (boxX and boxY), calculate what the nearby line coords would be
        // are these drawn lines or not?
        // (boxX, boxY), (boxX + 1, boxY), (boxX, boxY + 1), (boxX + 1, boxY + 1)
        // wrong
    }
}