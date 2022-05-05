package org.example.student.dotsboxgame

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import uk.ac.bournemouth.ap.dotsandboxeslib.test.*
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import kotlin.random.Random

/**
 * This class must be created to link your own game implementation and make it testable. You can
 * add your own tests if you wish so (make a no-arg function and give it a `@Test` annotation).
 */
internal class StudentDotsBoxGameTest : TestDotsAndBoxes() {
    override fun createGame(columns: Int, rows: Int, players: List<Player>): DotsAndBoxesGame {
        return StudentDotsBoxGame(columns, rows, players)
    }
    /**
     * Test EasyAI
     * Get line to draw
     * Check line owner
     * Test that the line to draw does not already have an owner
     * */

    /**
     * Test NormalAI
     * Human Player plays 3 lines of one box
     * Then test that after the computer turn, that box is owned by the computer
     * */

    /**
     * Test HardAI
     * Human Player plays 2 lines of one box
     * Check that those lines are removed from computer's options
     * Human Player plays 3 lines of the box adjacent to the box with 2 lines
     * Check that the AI isn't scared to complete that box and give the human a free box adjacent
     * */
}