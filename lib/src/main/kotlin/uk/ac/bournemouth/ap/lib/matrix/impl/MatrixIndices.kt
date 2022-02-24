package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

internal class MatrixIndices(private val matrix: Matrix<*>) : Iterable<Coordinate> {
    override fun iterator(): Iterator<Coordinate> = IteratorImpl(matrix)

    private class IteratorImpl(matrix: Matrix<*>) : Iterator<Coordinate> {
        private var nextPoint = 0
        private val maxPoint = matrix.width * matrix.height
        private val width = matrix.width

        override fun hasNext(): Boolean {
            return nextPoint < maxPoint
        }

        override fun next(): Coordinate {
            val point = nextPoint
            ++nextPoint
            return Coordinate(point % width, point / width)
        }
    }

}