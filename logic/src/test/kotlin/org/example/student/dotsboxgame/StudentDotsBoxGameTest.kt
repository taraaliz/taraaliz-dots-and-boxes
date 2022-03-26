package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import uk.ac.bournemouth.ap.dotsandboxeslib.test.TestDotsAndBoxes

/**
 * This class must be created to link your own game implementation and make it testable. You can
 * add your own tests if you wish so (make a no-arg function and give it a `@Test` annotation).
 */
internal class StudentDotsBoxGameTest : TestDotsAndBoxes() {
    override fun createGame(boxes: Int, lines: Int, players: List<Player>): DotsAndBoxesGame {
        return StudentDotsBoxGame(boxes, lines, players)
        TODO("Create a game of your game type with the correct columns, rows and players")
        // lines = columns?
    }
}