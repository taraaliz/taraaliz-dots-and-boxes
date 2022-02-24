package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.*
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableSparseMatrixCompanion

/** A sparse matrix for storing booleans. It works just like SparseMatrix but optimizes boolean storage.
 * This particular interface only provides read access. The matrix needs to be initialized
 * appropriately or used on a class that is actually mutable.
 */
interface SparseBooleanMatrix : SparseMatrix<Boolean> {
    override fun copyOf(): SparseBooleanMatrix

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : ImmutableSparseMatrixCompanion<Boolean> {
        override fun <T : Boolean> invoke(original: SparseMatrix<T>): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source = original) as SparseMatrix<T>
        }

        /**
         * Create  a new matrix that is a copy of the original
         *
         * @param source The source for the matrix data
         */
        operator fun invoke(source: SparseMatrix<Boolean>): SparseBooleanMatrix =
            when (source) {
                is BooleanMatrix -> ArrayMutableBooleanMatrix(source)
                else -> ArrayMutableSparseBooleanMatrix(
                    source.maxWidth,
                    source.maxHeight,
                    source.validator,
                    source::get
                )
            }

        override fun <T : Boolean> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(
                maxWidth,
                maxHeight,
                init as SparseMatrix.SparseInit<Boolean>.(Int, Int) -> SparseMatrix.SparseValue<Boolean>
            ) as SparseMatrix<T>
        }

        override fun <T : Boolean> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): SparseMatrix<T> {
            return invoke(source.width, source.height) { x, y -> source[x,y] } as SparseMatrix<T>
        }

        /**
         * Create a [SparseBooleanMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param init The function initializing the matrix
         */
        inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<Boolean>.(Int, Int) -> SparseMatrix.SparseValue<Boolean>
        ): SparseBooleanMatrix {
//            TODO("Make a compact implementation for this")
            val context = ArraySparseMatrix.valueCreator<Boolean>()
            val data = Matrix(maxWidth, maxHeight) { x, y -> context.init(x, y) }
            val validatorData = BooleanMatrix(maxWidth, maxHeight) { x, y -> data[x, y].isValid }

            return SparseBooleanMatrix(
                maxWidth,
                maxHeight,
                validator = { x, y -> validatorData[x, y] },
                init = { x, y -> data[x, y].value }
            )
        }

        override fun <T : Boolean> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, initValue, validator) as SparseMatrix<T>
        }

        /**
         * Create a [SparseBooleanMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param initValue The value of all cells
         * @param validator The function that determines whether a given cell is valid.
         */
        operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: Boolean,
            validator: (Int, Int) -> Boolean
        ): SparseBooleanMatrix {
            return SingleValueSparseBooleanMatrix(maxWidth, maxHeight, initValue, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Boolean> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, validator, init) as SparseMatrix<T>
        }

        /**
         * Create a [SparseBooleanMatrix] initialized with the given value
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
            init: (Int, Int) -> Boolean
        ): SparseBooleanMatrix {
            return ArrayMutableSparseBooleanMatrix(
                maxWidth,
                maxHeight,
                validator = validator,
                init = init
            )
        }
    }

}