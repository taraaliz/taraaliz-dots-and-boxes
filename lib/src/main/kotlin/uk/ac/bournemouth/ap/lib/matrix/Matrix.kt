package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import uk.ac.bournemouth.ap.lib.matrix.ext.FunMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableMatrixCompanion
import uk.ac.bournemouth.ap.lib.matrix.impl.MatrixIndices

/**
 * A matrix type (based upon [SparseMatrix]) but it has values at all coordinates.
 */
interface Matrix<out T> : SparseMatrix<T> {

    /** The width of the matrix. This is effectively the same as [maxWidth]. */
    val width: Int get() = maxWidth

    /** The height of the matrix. This is effectively the same as [maxWidth]. */
    val height: Int get() = maxHeight

    /**
     * The indices of all columns in the matrix
     */
    val columnIndices: IntRange get() = 0 until width

    /**
     * The indices of all rows in the matrix
     */
    val rowIndices: IntRange get() = 0 until height

    /**
     * This implementation will just check that the coordinates are in range. There should be no
     * reason to no use this default implementation.
     */
    override fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    override fun copyOf(): Matrix<T>

    /**
     * @inheritDoc
     *
     * This version special cases other being a matrix for efficiency.
     */
    override fun contentEquals(other: SparseMatrix<*>): Boolean = when (other) {
        is Matrix -> contentEquals(other)
        else -> super.contentEquals(other)
    }

    /**
     * Determine whether the content of this matrix is the same as the other by checking equality
     * on the cell values. Sparse matrices with different dimensions, but the same valid indices
     * can be equal.
     */
    fun contentEquals(other: Matrix<*>): Boolean {
        if (width != other.width || height != other.height) return false

        for (col in 0 until width) {
            for (row in 0 until height) {
                if (get(col, row) != other.get(col, row)) return false
            }
        }
        return true
    }

    override val validator: (Int, Int) -> Boolean
        get() = VALIDATOR

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [Matrix]).
     */
    companion object : ImmutableMatrixCompanion<Any?> {
        override fun <T> invoke(original: Matrix<T>): Matrix<T> {
            return original.copyOf()
        }

        override fun <T> invoke(width: Int, height: Int, initValue: T): Matrix<T> {
            return SingleValueMatrix(width, height, initValue)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): Matrix<T> {
            return ArrayMatrix(width, height, init)
        }

        override fun <T> fromFunction(width: Int, height: Int, valueFun: (Int, Int) -> T): Matrix<T> {
            return FunMatrix(width, height, valueFun)
        }

        @PublishedApi
        internal val VALIDATOR: (Int, Int) -> Boolean = { _, _ -> true }
    }
}

