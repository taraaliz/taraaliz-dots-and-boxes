package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

internal class SparseMatrixIndices(private val matrix: SparseMatrix<*>) :
    Iterable<Coordinate> {

    override fun iterator(): Iterator<Coordinate> = IteratorImpl(matrix)

    private class IteratorImpl(private val matrix: SparseMatrix<*>) :
        Iterator<Coordinate> {
        private var nextPoint = -1
        private val maxPoint get() = matrix.maxWidth * matrix.maxHeight
        private val width get() = matrix.maxWidth

        init {
            moveToNext()
        }

        private fun moveToNext() {
            do {
                nextPoint++
            } while (nextPoint < maxPoint && !isValidPoint(nextPoint))
        }

        private fun isValidPoint(point: Int): Boolean {
            return matrix.isValid(point % matrix.maxWidth, point / matrix.maxWidth)
        }

        override fun hasNext(): Boolean {
            return nextPoint < maxPoint
        }

        override fun next(): Coordinate {
            val point = nextPoint
            moveToNext()
            return Coordinate(point % width, point / width)
        }
    }
}