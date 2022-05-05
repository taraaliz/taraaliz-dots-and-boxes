package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix

class StudentDotsBoxGame (val columns: Int, val rows: Int, players: List<Player>): AbstractDotsAndBoxesGame() {
    override val players: List<Player> = mutableListOf<Player>().apply { addAll(players) }

    override var currentPlayer: Player = players[0]

    // NOTE: you may want to be more specific in the box type if you use that type in your class
    override val boxes: Matrix<StudentBox> = Matrix(columns, rows) { x, y -> StudentBox(x, y)}

    // validator function checks x is less than number of columns and y is less than number of rows
    // also that on the last column, no invalid lines exist ( where x = columns and y is odd)
    override val lines: MutableSparseMatrix<StudentLine> = MutableSparseMatrix(columns+1,
        rows*2+1, { x, y ->
            (x < columns || (x == columns && y % 2 !=0)) && y <= rows*2}, ::StudentLine)
    // x is less than columns OR x is equal to columns AND y is odd, AND y is less than rows * 2

    override var isFinished: Boolean = false
    // no getter provided as kotlin provides them by default, unless we need to do anything fancy

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }
    inner class InvalidMoveException(message: String) : Exception(message)

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
                val behindX: Int
                val behindY: Int
                val aheadX: Int?
                val aheadY: Int?

                when {
                    // Line is vertical
                    lineY % 2 != 0 -> {
                        behindX = lineX - 1
                        behindY = lineY/2
                        // we know lineY is odd so this is equivalent to (lineY - 1)/2
                        // result will be rounded down to nearest integer
                        aheadX = lineX
                        aheadY = lineY/2
                    }
                    // Line is horizontal
                    else -> {
                        behindX = lineX
                        behindY = (lineY / 2) - 1
                        // 1 is equivalent to (lineY + 1) % 2
                        // since lineY is even, this will always evaluate to 1
                        aheadX = lineX
                        aheadY = lineY/2
                    }
                }
                /** the box left or above the line - not all lines have one*/
                val behindBox: StudentBox? = if (boxes.isValid(behindX, behindY)) {
                    boxes[behindX, behindY]
                } else {
                    null
                }
                /** the box right or below the line - not all lines have one */
                val aheadBox: StudentBox? = if (boxes.isValid(aheadX, aheadY)) {
                    boxes[aheadX, aheadY]
                } else {
                    null
                }

                return Pair(behindBox, aheadBox)
            }

        override fun drawLine() {
            var nextPlayerIndex = players.indexOf(currentPlayer) + 1
            if (nextPlayerIndex >= players.size) {
                nextPlayerIndex = 0
            }
            if (isDrawn) {
                throw InvalidMoveException("Line is already drawn")
            }
            else {
                isDrawn = true
                for (box in adjacentBoxes.toList().filterNotNull()) {
                    if (box.boundingLines.all {it.isDrawn}) {
                        box.owningPlayer = currentPlayer
                        nextPlayerIndex = players.indexOf(currentPlayer)
                    }
                }
                val totalBoxesDrawn = getScores().sum()
                if (totalBoxesDrawn == boxes.count()) {
                    isFinished = true
                }
                if (isFinished)  {
                    val results = mutableListOf<Pair<Player,Int>>()
                    val scores = getScores()
                    for (player in players) {
                        val indexOfPlayer = players.indexOf(player)
                        results.add(Pair(player, scores[indexOfPlayer]))
                    }
                    fireGameChange()
                    fireGameOver(results)
                }
            }
            currentPlayer = players[nextPlayerIndex]
            playComputerTurns()
            fireGameChange()
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null
        // no getter or setter made, default value null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
        // top left bottom right
            get() = listOf(lines[boxX, 2 * boxY], lines[boxX, 2 * boxY + 1],
                lines[boxX, 2* boxY + 2], lines[boxX + 1, 2 * boxY + 1])
    }
}