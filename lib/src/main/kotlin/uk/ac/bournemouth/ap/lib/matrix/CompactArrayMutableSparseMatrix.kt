package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.validate

/**
 * Implementation of a [MutableSparseMatrix] based upon an array to store data and validity.
 * It uses the same array to record sparseness as well as data. It does not support changing
 * validity of cells (at this point).
 *
 * @constructor This constructor is not intended for use, but is invoked by the inline factory function. It
 * exposes the underlying data format. Note that sparse locations are fixed and cannot be changed
 * after the fact.
 */
class CompactArrayMutableSparseMatrix<T> @PublishedApi internal constructor(
    override val maxWidth: Int,
    private val data: Array<Any?>
) : MutableSparseMatrix<T> {

    override val maxHeight get() = data.size / maxWidth

    override val validator: (Int, Int) -> Boolean =
        { x, y -> data[x + y * maxWidth] != ArraySparseMatrix.SPARSE_CELL }

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
            this(maxWidth,
                Array(maxWidth * maxHeight) {
                    val x = it % maxWidth
                    val y = it / maxWidth
                    when {
                        validator(x, y) -> initValue
                        else -> ArraySparseMatrix.SPARSE_CELL
                    }
                })

    /**
     * Create a new instance that is a copy of the original matrix. This will be a shallow copy as
     * in the elements will not be copied.
     */
    constructor(original: CompactArrayMutableSparseMatrix<T>) : this(
        original.maxWidth,
        original.data.copyOf()
    )

    /**
     * A constructor that creates a new [CompactArrayMutableSparseMatrix] given a matrix of
     * [SparseValue]s. Those are used to initialize the matrix.
     */
    constructor(source: Matrix<SparseMatrix.SparseValue<T>>) : this(
        source.maxWidth,
        Array(source.width * source.height) { i ->
            val v = source[i % source.width, i / source.height]
            when {
                v.isValid -> v.value
                else -> ArraySparseMatrix.SPARSE_CELL
            }
        }
    )

    /** Helper function to check that the coordinates given are valid. */
    private fun validate(x: Int, y: Int, value: () -> Any?): T {
        if (x !in 0 until maxWidth || y !in 0 until maxHeight) {
            throw IndexOutOfBoundsException("($x,$y) out of range: ([0,$maxWidth), [0,$maxHeight))")
        }

        val v = value()

        if (v == ArraySparseMatrix.SPARSE_CELL) {
            throw IndexOutOfBoundsException("($x, $y) is a sparse index")
        }

        return v as T
    }

    override fun get(x: Int, y: Int): T {
        return validate(x, y) { data[x + y * maxWidth] }
    }

    override fun set(x: Int, y: Int, value: T) {
        validate(x, y)
        data[x + y * maxWidth] = value
    }

    /** Create a copy of this matrix */
    override fun copyOf(): CompactArrayMutableSparseMatrix<T> =
        CompactArrayMutableSparseMatrix(this)

    /**
     * Determine whether a given coordinate is valid for the matrix.
     */
    override fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until maxWidth &&
                y in 0 until maxHeight &&
                data[x + y * maxWidth] != ArraySparseMatrix.SPARSE_CELL
    }

    override fun toString(): String = toString("CompactMutableSparseMatrix")

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {

        /**
         * Factory function that creates and initializes a compact mutable sparse matrix.
         * [maxWidth] and [maxHeight] are used for the underlying array dimensions. The
         * [validator] will be called exactly once for each coordinate. If it returns `true`
         * the init function is called to determine the initial value.
         *
         * @param maxWidth The maximum width allowed in the matrix
         * @param maxHeight The maximum height allowed in the matrix
         * @param validator Function that determines whether a particular location is valid or not.
         * @param init Function that determines the initial value for a location.
         */
        inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): CompactArrayMutableSparseMatrix<T> {
            val sparse = ArraySparseMatrix.valueCreator<T>().sparse
            val data = Array<Any?>(maxWidth * maxHeight) { c ->
                val x = c % maxWidth
                val y = c / maxWidth
                when {
                    validator(x, y) -> init(x, y)
                    else -> sparse
                }
            }
            return CompactArrayMutableSparseMatrix(maxWidth, data)
        }

        /**
         * Factory function that creates and initializes a compact mutable sparse matrix.
         * [maxWidth] and [maxHeight] are used for the underlying array dimensions. This
         * variant uses a sealed return type to determine whether a value is sparse or not.
         *
         * @param maxWidth The maximum width allowed in the matrix
         * @param maxHeight The maximum height allowed in the matrix
         * @param init Function that determines the initial value for a location.
         */
        inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): CompactArrayMutableSparseMatrix<T> {
            val context = ArraySparseMatrix.valueCreator<T>()
            val data = Array<Any?>(maxWidth * maxHeight) { c ->
                val x = c % maxWidth
                val y = c / maxWidth
                val e = context.init(x, y)
                when {
                    e.isValid -> e.value
                    else -> e
                }
            }
            return CompactArrayMutableSparseMatrix(maxWidth, data)
        }

    }
}