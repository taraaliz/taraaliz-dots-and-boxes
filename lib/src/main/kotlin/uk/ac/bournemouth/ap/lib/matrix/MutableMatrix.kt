package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.MutableMatrixCompanion

/**
 * An extension to Matrix that is mutable. This is effectively a 2D array.
 */
interface MutableMatrix<T> : MutableSparseMatrix<T>, Matrix<T> {
    override fun copyOf(): MutableMatrix<T>

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [MutableMatrix]).
     */
    companion object : MutableMatrixCompanion<Any?> {
        override fun <T> invoke(original: Matrix<T>): MutableMatrix<T> {
            return ArrayMutableMatrix(original)
        }

        override fun <T> invoke(width: Int, height: Int, initValue: T): MutableMatrix<T> {
            return ArrayMutableMatrix(width, height, initValue)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): MutableMatrix<T> {
            return ArrayMutableMatrix<T>(width, height, init)
        }

    }

}