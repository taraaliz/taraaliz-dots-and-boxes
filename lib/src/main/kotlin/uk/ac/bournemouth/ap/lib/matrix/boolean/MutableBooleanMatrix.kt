package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableMatrixCompanion
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Interface representing a mutable matrix containing specialised for boolean values.
 */
interface MutableBooleanMatrix : BooleanMatrix, MutableSparseBooleanMatrix, MutableMatrix<Boolean> {
    override fun copyOf(): MutableBooleanMatrix

    override fun contentEquals(other: SparseMatrix<*>): Boolean {
        return super<MutableMatrix>.contentEquals(other)
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : MutableMatrixCompanion<Boolean> {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Boolean> invoke(original: Matrix<T>): MutableMatrix<T> =
            invoke(source = original) as MutableMatrix<T>

        operator fun invoke(source: Matrix<Boolean>): MutableBooleanMatrix = when (source) {
            is BooleanMatrix -> ArrayMutableBooleanMatrix(source)
            else -> ArrayMutableBooleanMatrix(source.width, source.height, source::get)
        }

        operator fun invoke(source: BooleanMatrix): MutableBooleanMatrix =
            ArrayMutableBooleanMatrix(source)

        /**
         * Create a [MutableBooleanMatrix] initialized with the default value of the [Boolean] type (`0`)
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         */
        operator fun invoke(width: Int, height: Int): MutableBooleanMatrix {
            return ArrayMutableBooleanMatrix(width, height)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Boolean> invoke(width: Int, height: Int, initValue: T): MutableMatrix<T> =
            invoke(width, height, initValue) as MutableMatrix<T>

        /**
         * Create a [MutableBooleanMatrix] initialized with the default value of the [Boolean] type (`0`)
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The initial value of the elements of the matrix
         */
        operator fun invoke(width: Int, height: Int, initValue: Boolean): MutableBooleanMatrix {
            return ArrayMutableBooleanMatrix(width, height, initValue)
        }

        override fun <T : Boolean> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): MutableMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(width, height, init) as MutableMatrix<T>
        }

        operator fun invoke(width: Int, height: Int, init: (Int, Int) -> Boolean):
                MutableBooleanMatrix {
            return ArrayMutableBooleanMatrix(width, height, init)
        }

    }
}

