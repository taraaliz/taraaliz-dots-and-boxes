package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.*
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableSparseMatrixCompanion

/**
 * A mutable sparse matrix for Booleans. This interface supports mutating the values.
 */
interface MutableSparseBooleanMatrix : SparseBooleanMatrix, MutableSparseMatrix<Boolean> {
    override fun copyOf(): MutableSparseBooleanMatrix

    override fun contentEquals(other: SparseMatrix<*>): Boolean

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : MutableSparseMatrixCompanion<Boolean> {
        override fun <T : Boolean> invoke(original: SparseMatrix<T>): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source = original) as MutableSparseMatrix<T>
        }

        operator fun invoke(source: SparseMatrix<Boolean>): MutableSparseBooleanMatrix =
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
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(
                maxWidth,
                maxHeight,
                init as SparseMatrix.SparseInit<Boolean>.(Int, Int) -> SparseMatrix.SparseValue<Boolean>
            ) as MutableSparseMatrix<T>
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
        ): MutableSparseBooleanMatrix {
//            TODO("Make a compact implementation for this")
            val context = ArraySparseMatrix.valueCreator<Boolean>()
            val data = Matrix(maxWidth, maxHeight) { x, y -> context.init(x, y) }
            val validatorData = BooleanMatrix(maxWidth, maxHeight) { x, y -> data[x, y].isValid }

            return invoke(
                maxWidth,
                maxHeight,
                validator = { x, y -> validatorData[x, y] },
                init = { x, y -> data[x, y].value }
            )
        }

        override fun <T : Boolean> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source.width, source.height, init = { x, y -> source[x,y]}) as MutableSparseMatrix<T>
        }

        override fun <T : Boolean> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, initValue, validator) as MutableSparseMatrix<T>
        }

        /**
         * Create a [MutableBooleanMatrix] initialized with the default value of the [Boolean] type (`0`)
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param validator Function that determines which cells are part of the matrix
         */
        operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            validator: (Int, Int) -> Boolean
        ): MutableSparseBooleanMatrix {
            return ArrayMutableSparseBooleanMatrix(maxWidth, maxHeight, validator)
        }

        /**
         * Create a [MutableSparseBooleanMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param initValue The initial value for the elements
         * @param validator The function that determines whether a given cell is valid.
         */
        operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: Boolean,
            validator: (Int, Int) -> Boolean
        ): MutableSparseBooleanMatrix {
            val data = BooleanArray(maxWidth * maxHeight).apply { if (initValue) fill(initValue) }
            return ArrayMutableSparseBooleanMatrix(maxWidth, data, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Boolean> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, validator, init) as MutableSparseMatrix<T>
        }

        /**
         * Create a [MutableSparseBooleanMatrix] initialized with the given value
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
        ): MutableSparseBooleanMatrix {
            return ArrayMutableSparseBooleanMatrix(
                maxWidth,
                maxHeight,
                validator = validator,
                init = init
            )
        }
    }
}

