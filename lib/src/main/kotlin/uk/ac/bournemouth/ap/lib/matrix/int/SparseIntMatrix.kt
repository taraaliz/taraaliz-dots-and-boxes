package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.ArraySparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableSparseMatrixCompanion

/** A sparse matrix for storing integers. It works just like SparseMatrix but optimizes int storage.
 * This particular interface only provides read access. The matrix needs to be initialized
 * appropriately or used on a class that is actually mutable.
 */
interface SparseIntMatrix : SparseMatrix<Int> {
    override operator fun get(x: Int, y: Int): Int

    override fun copyOf(): SparseIntMatrix

    /**
     * Calculate the sum of all values.
     */
    fun sum(): Int {
        var total = 0
        forEach { total += it }
        return total
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : ImmutableSparseMatrixCompanion<Int> {
        /**
         * Factory that creates a concrete implementation
         */
        @Deprecated("Use the type specific factory, this one is only there for compatibility", level = DeprecationLevel.ERROR)
        override fun <T : Int> invoke(original: SparseMatrix<T>): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source = original) as SparseMatrix<T>
        }

        /**
         * Create  a new matrix that is a copy of the original
         *
         * @param source The source for the matrix data
         */
        operator fun invoke(source: SparseMatrix<Int>): SparseIntMatrix =
            when (source) {
                is IntMatrix -> ArrayMutableIntMatrix(source)
                else -> ArrayMutableSparseIntMatrix(
                    source.maxWidth,
                    source.maxHeight,
                    source.validator,
                    source::get
                )
            }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Int> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(
                maxWidth,
                maxHeight,
                { x, y -> (this as SparseMatrix.SparseInit<T>).init(x, y) }
            ) as SparseMatrix<T>
        }

        /**
         * Create a [SparseIntMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param init The function initializing the matrix
         */
        inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<Int>.(Int, Int) -> SparseMatrix.SparseValue<Int>
        ): SparseIntMatrix {
            // TODO("Make a compact implementation for this")
            val context = ArraySparseMatrix.valueCreator<Int>()
            val data = Matrix(maxWidth, maxHeight) { x, y -> context.init(x, y) }
            val validatorData = BooleanMatrix(maxWidth, maxHeight) { x, y -> data[x, y].isValid }

            return SparseIntMatrix(
                maxWidth,
                maxHeight,
                validator = { x, y -> validatorData[x, y] },
                init = { x, y -> data[x, y].value }
            )
        }

        override fun <T : Int> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): SparseMatrix<T> {
            return invoke(source.width, source.height) { x, y -> source[x, y] } as SparseMatrix<T>
        }

        override fun <T : Int> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, initValue, validator) as SparseMatrix<T>
        }

        /**
         * Create a [SparseIntMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param initValue The value of all cells
         * @param validator The function that determines whether a given cell is valid.
         */
        operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: Int,
            validator: (Int, Int) -> Boolean
        ): SparseIntMatrix {
            return SingleValueSparseIntMatrix(maxWidth, maxHeight, initValue, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Int> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, validator, init) as SparseMatrix<T>
        }

        /**
         * Create a [SparseIntMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param init The function initializing the matrix
         * @param validator The function that determines whether a given cell is valid.
         */
        inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> Int
        ): SparseIntMatrix {
            return ArrayMutableSparseIntMatrix(
                maxWidth,
                maxHeight,
                validator = validator,
                init = init
            )
        }
    }

}