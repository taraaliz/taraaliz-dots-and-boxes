package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableMatrixCompanion
import uk.ac.bournemouth.ap.lib.matrix.Matrix

/**
 * Interface representing a matrix that stores Boolean values natively.
 */
interface BooleanMatrix : Matrix<Boolean>, SparseBooleanMatrix {
    /**
     * This function provides access to the underlying array for copying. It should be internal.
     * @suppress
     */
    fun toFlatArray(): BooleanArray
    fun contentEquals(other: BooleanMatrix): Boolean
    override fun copyOf(): BooleanMatrix

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : ImmutableMatrixCompanion<Boolean> {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Boolean> invoke(original: Matrix<T>): Matrix<T> =
            invoke(source = original) as Matrix<T>

        operator fun invoke(source: Matrix<Boolean>): BooleanMatrix = when (source) {
            is BooleanMatrix -> ArrayMutableBooleanMatrix(source)
            else -> ArrayMutableBooleanMatrix(source.width, source.height, source::get)
        }

        operator fun invoke(source: BooleanMatrix): BooleanMatrix =
            ArrayMutableBooleanMatrix(source)

        @Suppress("UNCHECKED_CAST")
        override fun <T : Boolean> invoke(width: Int, height: Int, initValue: T): Matrix<T> =
            invoke(width, height, initValue) as Matrix<T>

        /**
         * Create an [BooleanMatrix] initialized with the given value
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The value of all cells
         */
        operator fun invoke(width: Int, height: Int, initValue: Boolean): BooleanMatrix {
            return SingleValueBooleanMatrix(width, height, initValue)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Boolean> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): Matrix<T> {
            @Suppress("UNCHECKED_CAST")
            return ArrayMutableBooleanMatrix(width, height, init) as Matrix<T>
        }

        /**
         * Create an [BooleanMatrix] initialized according to the init function.
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param init Function used to initialise each cell.
         */
        inline operator fun invoke(width: Int, height: Int, init: (Int, Int) -> Boolean):
                BooleanMatrix {
            return ArrayMutableBooleanMatrix(width, height, init)
        }

    }

}