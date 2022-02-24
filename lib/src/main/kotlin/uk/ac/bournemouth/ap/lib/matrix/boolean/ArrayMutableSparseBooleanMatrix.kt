package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * A mutable sparse boolean matrix implementation backed by an array.
 */
class ArrayMutableSparseBooleanMatrix : ArrayMutableBooleanMatrixBase {

    override val validator: (Int, Int) -> Boolean

    constructor(maxWidth: Int, maxHeight: Int, validator: (Int, Int) -> Boolean) :
            super(maxWidth, maxHeight) {
        this.validator = validator
    }

    constructor(
        maxWidth: Int,
        data: BooleanArray,
        validator: (Int, Int) -> Boolean
    ) : super(maxWidth, data) {
        this.validator = validator
    }

    override fun copyOf(): MutableSparseBooleanMatrix {
        return ArrayMutableSparseBooleanMatrix(maxWidth, data.copyOf(), validator)
    }

    override fun fill(element: Boolean) {
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
            init: (Int, Int) -> Boolean
        ): ArrayMutableSparseBooleanMatrix {
            return ArrayMutableSparseBooleanMatrix(maxWidth, maxHeight, validator).apply {
                fill(init)
            }
        }

        internal object SPARSE_CELL : SparseMatrix.SparseValue<Nothing> {
            override val isValid: Boolean get() = false
            override val value: Nothing get() = throw IllegalStateException("Sparse cells have no value")
            override fun toString(): String = "<sparse>"
        }

        private object TrueValue : SparseMatrix.SparseValue<Boolean> {
            override val isValid: Boolean get() = true
            override val value: Boolean get() = true
            override fun toString(): String = "true"
        }

        private object FalseValue : SparseMatrix.SparseValue<Boolean> {
            override val isValid: Boolean get() = true
            override val value: Boolean get() = false
            override fun toString(): String = "false"
        }


        @PublishedApi
        internal object valueCreator : SparseMatrix.SparseInit<Boolean>() {
            override fun value(v: Boolean): SparseMatrix.SparseValue<Boolean> = when (v) {
                true -> TrueValue
                else -> FalseValue
            }

            override val sparse: SparseMatrix.SparseValue<Nothing> = SPARSE_CELL
        }

    }
}