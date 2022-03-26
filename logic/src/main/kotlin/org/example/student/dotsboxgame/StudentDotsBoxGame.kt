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

    // validator function checks x is less than number of columns and y is less than number of rows
    // also that on the last column, no invalid lines exist ( where x = columns and y is odd)
    override val lines: MutableSparseMatrix<StudentLine> = MutableSparseMatrix(columns+1,
        rows*2+1, { x, y ->
            x < columns && (if (x == columns) {(y % 2) != 0} else y < rows)}, ::StudentLine)

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
        // even y coords are horizontal
        // odd y coords are vertical
        override var isDrawn: Boolean = false

        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                // no strict formula, depends on if line is horizontal or vertical
                // most are x-1, y-1 and x-1, y (horizontal)
                // vertical is x,y, and x-1, y
                // if odd y
                var box1_x: Int
                var box1_y: Int
                var box2_x: Int?
                var box2_y: Int?
                var Box1: StudentBox?
                var Box2: StudentBox? = null



                // handling lines with x coord 3 in a 4x4 game - lines on the right edge
                // lines with x coord 3 and even y coord are invalid
                // do we need to check for even y here if it is checked on creation in
                // SpareMatrix?
                if (lineX == columns && lineY % 2 != 0) {
                    // if y coord is odd - vertical and on border
                        box1_x = lineX - 1
                        box1_y = (lineY + 1) / (columns - 1)
                }
                // handling lines with x coord 0 - lines on the left edge
                else if (lineX == 0) {
                    box1_x = lineX
                    box1_y = lineY / 2
                    // if y coord is odd (vertical), or on the borders, no second box
                    // for the lines with even y coords (horizontal) not on the borders
                    if (lineY % 2 == 0 || lineY != columns * 2 || lineY != 0) {
                        box2_x = lineX
                        box2_y = (lineY - 1) / 2
                        Box2 = boxes[box2_x, box2_y]
                    }
                }
                // handling all other lines
                else {
                    // horizontal lines only - this is reassigned for vertical lines
                    box1_x = lineX
                    // if on the borders
                    if (lineY == 0 || lineY == columns*2) {
                        box1_y = lineY / columns
                    }
                    else {
                        // if line is vertical box1_x is lineX - 1 instead of lineX
                        if (lineY % 2 != 0) {
                            box1_x = lineX - 1
                            // box to left of it is lineX - 1
                        }
//                        1 / 2 (0) - 2 % 2 (0) = 0
//                        2 / 2 (1) - 3 % 2 (1) = 0
//                        3 / 2 (1) - 4 % 2 (0) = 1
//                        4 / 2 (2) - 5 % 2 (1) = 1
//                        5 / 2 (2) - 6 % 2 (0) = 2
                        box1_y = lineY / (columns - 1) - (lineY + 1) % 2
                        box2_y = lineY / (columns - 1)
                        box2_x = lineX
                        // box to right of it is lineX
                        Box2 = boxes[box2_x, box2_y]
                    }
                }
                // all cases have at least one box
                // this might go wrong if the validator function for lines isn't right
                Box1 = boxes[box1_x, box1_y]
                //return boxes[x, y] to boxes[a, b]
                return Pair(Box1, Box2)
                TODO("You need to look up the correct boxes for this to work")
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