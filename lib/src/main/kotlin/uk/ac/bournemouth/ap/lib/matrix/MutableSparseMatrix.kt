package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.indices
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableSparseMatrixCompanion

/**
 * A mutable version of [SparseMatrix] that adds a setter ([set]) to allow for changing the values
 * in the matrix.
 */
interface MutableSparseMatrix<T> : SparseMatrix<T> {
    /**
     * Set a specific value in the matrix
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param value The new value
     */
    operator fun set(x: Int, y: Int, value: T)

    /**
     * Helper function to set values
     */
    fun fill(value: T) {
        for ((x, y) in indices) {
            this[x, y] = value
        }
    }

    /**
     * Creates a copy of the matrix of an appropriate type with the same content.
     */
    override fun copyOf(): MutableSparseMatrix<T>

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [MutableSparseMatrix]).
     */
    companion object : MutableSparseMatrixCompanion<Any?> {
        override fun <T> invoke(original: SparseMatrix<T>): MutableSparseMatrix<T> {
            return when (original) {
                is ArrayMutableSparseMatrix -> ArrayMutableSparseMatrix(original)
                is Matrix -> MutableMatrix(original)
                else -> invoke(
                    original.maxWidth,
                    original.maxHeight,
                    original.validator,
                    original::get
                )
            }
        }

        override fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): MutableSparseMatrix<T> {
            return ArrayMutableSparseMatrix(maxWidth, maxHeight, initValue, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): MutableSparseMatrix<T> {
            return ArrayMutableSparseMatrix(maxWidth, maxHeight, validator, init)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): MutableSparseMatrix<T> {
            return CompactArrayMutableSparseMatrix<T>(
                maxWidth,
                maxHeight,
                init
            )
        }

        override fun <T> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): MutableSparseMatrix<T> {
            return CompactArrayMutableSparseMatrix(source)
        }

    }

}