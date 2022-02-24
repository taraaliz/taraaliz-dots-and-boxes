package uk.ac.bournemouth.ap.lib.matrix.int

/**
 * Mutable sparse int matrix backed by an array.
 */
class ArrayMutableSparseIntMatrix : ArrayMutableIntMatrixBase {

    override val validator: (Int, Int) -> Boolean

    constructor(maxWidth: Int, maxHeight: Int, validator: (Int, Int) -> Boolean) :
            super(maxWidth, maxHeight) {
        this.validator = validator
    }

    constructor(maxWidth: Int, maxHeight: Int, data: IntArray, validator: (Int, Int) -> Boolean) :
            super(maxWidth, maxHeight, data) {
        this.validator = validator
    }

    override fun copyOf(): MutableSparseIntMatrix {
        return ArrayMutableSparseIntMatrix(maxWidth, maxHeight, data.copyOf(), validator)
    }


    override fun fill(element: Int) {
        data.fill(element)
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {
        /**
         * Create a new instance with given [width], [height], [validator] and initialized according
         * to [init].
         * @param validator This function determines whether a particular cell is part of the matrix.
         *                  Note that the implementation expect this function to return the same
         *                  result for all invocations.
         */
        inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> Int
        ): ArrayMutableSparseIntMatrix {
            return ArrayMutableSparseIntMatrix(maxWidth, maxHeight, validator).apply {
                fill(init)
            }
        }

    }

}