package uk.ac.bournemouth.ap.lib.matrix

import java.util.function.Consumer

/**
 * Implementation of a [MutableSparseMatrix] based upon an array to store the data.
 */
class ArrayMutableSparseMatrix<T> : ArrayMutableMatrixBase<T>,
    SparseMatrix<T> {

    override val validator: (Int, Int) -> Boolean

    @PublishedApi
    internal constructor(maxWidth: Int, data: Array<T?>, validator: (Int, Int) -> Boolean) :
            super(maxWidth, data) {
        this.validator = validator
    }

    /**
     * Create a new instance of the class with the given validation function. All elements of the
     * matrix will be set to the same initial value. The validation function does not need to
     * validate that the coordinate is within
     *
     * @param maxWidth The width of the matrix in columns
     * @param maxHeight The height of the matrix in rows
     * @param initValue The initial value for all cells
     * @param validator The function used to determine whether a cell is used in the matrix.
     */
    constructor(maxWidth: Int, maxHeight: Int, initValue: T, validator: (Int, Int) -> Boolean) :
            super(maxWidth, maxHeight, initValue) {
        this.validator = validator
    }

    /**
     * Create a new instance that is a copy of the original matrix. This will be a shallow copy as
     * in the elements will not be copied.
     */
    constructor(original: ArrayMutableSparseMatrix<T>) : super(original) {
        this.validator = original.validator
    }

    /** Create a copy of this matrix */
    override fun copyOf(): ArrayMutableSparseMatrix<T> =
        ArrayMutableSparseMatrix(this)


    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {

        /**
         * Create a new [ArrayMutableSparseMatrix] instance with the given parameters.
         *
         * @param maxWidth The maximum width of the matrix (maximum x coordinate+1)
         * @param maxHeight The maximum height of the matrix (maximum y coordinate+1)
         * @param validator The function that determines whether a given cell is valid
         * @param init Inline function used to initialize the data
         * @return The newly created matrix
         */
        inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): ArrayMutableSparseMatrix<T> {
            val data = Array<Any?>(maxWidth * maxHeight) { c ->
                val x = c % maxWidth
                val y = c / maxWidth
                when {
                    validator(x, y) -> init(x, y)
                    else -> null
                }
            } as Array<T?>
            return ArrayMutableSparseMatrix(maxWidth, data = data, validator)
        }
    }
}