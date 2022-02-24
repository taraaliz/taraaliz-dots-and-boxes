package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.ArraySparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import uk.ac.bournemouth.ap.lib.matrix.boolean.mapBoolean
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableSparseMatrixCompanion

/**
 * A mutable sparse matrix for integers. This interface supports mutating the values.
 */
interface MutableSparseIntMatrix : SparseIntMatrix, MutableSparseMatrix<Int> {
    override operator fun set(x: Int, y: Int, value: Int)

    override fun copyOf(): MutableSparseIntMatrix

    fun contentEquals(other: SparseIntMatrix): Boolean

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : MutableSparseMatrixCompanion<Int> {
        @Deprecated("This version is only there for inheritance", level = DeprecationLevel.ERROR)
        override fun <T : Int> invoke(original: SparseMatrix<T>): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source = original) as MutableSparseMatrix<T>
        }

        /**
         * Create a new mutable matrix initialized from the source.
         */
        operator fun invoke(source: SparseMatrix<Int>): MutableSparseIntMatrix =
            when (source) {
                is IntMatrix -> ArrayMutableIntMatrix(source)
                else -> ArrayMutableSparseIntMatrix(
                    source.maxWidth,
                    source.maxHeight,
                    source.validator,
                    source::get
                )
            }

        /**
         * Factory function that creates and initializes a (readonly) [MutableSparseIntMatrix].
         * [maxWidth] and [maxHeight] are used for the underlying array dimensions. This
         * variant uses a sealed return type to determine whether a value is sparse or not.
         *
         * @param maxWidth The maximum width allowed in the matrix
         * @param maxHeight The maximum height allowed in the matrix
         * @param init Function that determines the initial value for a location.
         */
        override fun <T : Int> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(
                maxWidth,
                maxHeight,
                init as SparseMatrix.SparseInit<Int>.(Int, Int) -> SparseMatrix.SparseValue<Int>
            ) as MutableSparseMatrix<T>
        }

        override fun <T : Int> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): MutableSparseMatrix<T> {
            val validData = source.mapBoolean { it.isValid }
            return invoke<T>(
                source.maxWidth,
                source.maxHeight,
                { x, y -> validData[x, y] },
                { x, y -> source[x, y].value })
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
        ): MutableSparseIntMatrix {
            //TODO("Make a compact implementation for this")
            val context = ArraySparseMatrix.valueCreator<Int>()
            val data = Matrix(maxWidth, maxHeight) { x, y -> context.init(x, y) }
            val validatorData = BooleanMatrix(maxWidth, maxHeight) { x, y -> data[x, y].isValid }

            return invoke(
                maxWidth,
                maxHeight,
                validator = { x, y -> validatorData[x, y] },
                init = { x, y -> data[x, y].value }
            )
        }

        override fun <T : Int> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, initValue, validator) as MutableSparseMatrix<T>
        }

        /**
         * Create a [MutableIntMatrix] initialized with the default value of the [Int] type (`0`)
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param validator Function that determines which cells are part of the matrix
         */
        operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            validator: (Int, Int) -> Boolean
        ): MutableSparseIntMatrix {
            return ArrayMutableSparseIntMatrix(maxWidth, maxHeight, validator)
        }

        /**
         * Create a [MutableSparseIntMatrix] initialized with the given value
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The initial value for the elements
         * @param validator The function that determines whether a given cell is valid.
         */
        operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: Int,
            validator: (Int, Int) -> Boolean
        ): MutableSparseIntMatrix {
            val data = IntArray(maxWidth * maxHeight).apply { fill(initValue) }
            return ArrayMutableSparseIntMatrix(maxWidth, maxHeight, data, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Int> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, validator, init) as MutableSparseMatrix<T>
        }

        /**
         * Create a [MutableSparseIntMatrix] initialized with the given value
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
        ): MutableSparseIntMatrix {
            return ArrayMutableSparseIntMatrix(
                maxWidth,
                maxHeight,
                validator = validator,
                init = init
            )
        }
    }
}