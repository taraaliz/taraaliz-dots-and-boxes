package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.indices
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableSparseMatrixCompanion
import uk.ac.bournemouth.ap.lib.matrix.impl.SparseMatrixIndices

/**
 * A 2-dimensional storage type/matrix that does not require values in all cells. This is a
 * read-only type. The writable version is [MutableSparseMatrix]. The minimum coordinate is always
 * 0. This implements [Iterable] to allow you to get all values (this relies on [isValid]).
 */
interface SparseMatrix<out T> : Iterable<T> {
    /** The maximum x coordinate that is valid which can be stored. */
    val maxWidth: Int

    /** The maximum x coordinate that is valid */
    val maxHeight: Int

    /**
     * This function can be used to determine whether the given coordinates are valid. Returns
     * true if valid. This function works on any value for the coordinates and should return `false`
     * for all values out of range (`x<0 || x>=[maxWidth]`), (`y<0 || y>=[maxHeight]`).
     */
    fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until maxWidth &&
                y in 0 until maxHeight &&
                validator(x, y)
    }

    /**
     * Whatever the actual type, allow them to be read to read any value. Implementations are
     * expected to use more precise return types.
     */
    operator fun get(x: Int, y: Int): T

    /**
     * For copying allow retrieving the/a validator function
     */
    val validator: (Int, Int) -> Boolean

    /**
     * Creates a copy of the matrix of an appropriate type with the same content.
     */
    fun copyOf(): SparseMatrix<T>

    /**
     * Get an iterator over all elements in the (sparse) matrix.
     */
    override fun iterator(): Iterator<T> = object : Iterator<T> {

        private val indexIterator = indices.iterator()

        override fun hasNext(): Boolean {
            return indexIterator.hasNext()
        }

        override fun next(): T {
            val idx = indexIterator.next()
            return get(idx.x, idx.y)
        }

    }

    /**
     * Get all elements in the matrix as sequence.
     */
    fun asSequence(): Sequence<T> = object : Sequence<T> {
        override fun iterator(): Iterator<T> = this@SparseMatrix.iterator()
    }

    /**
     * Determine whether the content of this matrix is the same as the other by checking equality
     * on the cell values. Sparse matrices with different dimensions, but the same valid indices
     * can be equal.
     */
    fun contentEquals(other: SparseMatrix<*>): Boolean {
        for (x in (maxWidth + 1) until other.maxWidth) {
            for (y in 0 until other.maxHeight) {
                if (other.isValid(x, y)) return false
            }
        }

        for (x in other.maxWidth + 1 until maxWidth) {
            for (y in 0 until maxHeight) {
                if (isValid(x, y)) return false
            }
        }

        val width = minOf(maxWidth, other.maxWidth)
        for (y in (maxHeight + 1) until other.maxHeight) {
            for (x in 0 until width) {
                if (other.isValid(x, y)) return false
            }
        }

        for (y in (other.maxHeight + 1) until maxHeight) {
            for (x in 0 until width) {
                if (isValid(x, y)) return false
            }
        }

        val height = minOf(maxHeight, other.maxHeight)

        for (col in 0 until width) {
            for (row in 0 until height) {
                if (isValid(col, row)) {
                    if (!other.isValid(col, row)) return false
                    if (get(col, row) != other.get(col, row)) return false
                } else {
                    if (other.isValid(col, row)) return false
                }
            }
        }
        return true
    }

    /**
     * Function that helps with actual implementation of toString. It creates a wrapped string,
     * starting with the prefix, the other lines indented appropriately.
     * @suppress
     */
    fun toString(prefix: String): String {
        val strings: SparseMatrix<String> = map<T, String> { it.toString() }
        val maxColWidth = strings.asSequence().map { it.length }.maxOrNull() ?: 0
        val capacity = maxWidth * maxColWidth + 2
        val lineSep = buildString(prefix.length+1) {
            append('\n')
            repeat(prefix.length) { append(' ') }
        }

        return buildString(capacity) {
            (0 until maxHeight).joinTo(this, lineSep, "${prefix}(", ")") { row ->
                (0 until maxWidth).joinToString { col ->
                    val cellString = if (isValid(col, row)) strings[col, row] else ""
                    cellString.padStart(maxColWidth)
                }
            }
        }
    }


    interface SparseValue<out T> {
        val isValid: Boolean
        val value: T
    }

    /**
     * This class provides access to a single function initialising a sparse matrix by returing
     * either a value, or marking it sparse. It is used as receiver of the lambda, that is expected
     * to return the result of either invoking [value] or [sparse].
     */
    abstract class SparseInit<T> internal constructor() {
        /**
         * Create a wrapper representing [v] as a value.
         */
        abstract fun value(v: T): SparseValue<T>

        /**
         * a value that represents a sparse cell.
         */
        abstract val sparse: SparseValue<Nothing>
    }

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [SparseMatrix]).
     */
    companion object : ImmutableSparseMatrixCompanion<Any?> {
        override fun <T> invoke(original: SparseMatrix<T>): SparseMatrix<T> {
            return original.copyOf()
        }

        override fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): SparseMatrix<T> {
            return SingleValueSparseMatrix(maxWidth, maxHeight, initValue, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): SparseMatrix<T> {
            return ArraySparseMatrix(maxWidth, maxHeight, validator, init)
        }

        override fun <T> fromSparseValueMatrix(source: Matrix<SparseValue<T>>): SparseMatrix<T> {
            return CompactArrayMutableSparseMatrix(source)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseInit<T>.(Int, Int) -> SparseValue<T>
        ): SparseMatrix<T> {
            return CompactArrayMutableSparseMatrix<T>(
                maxWidth,
                maxHeight,
                init
            )
        }

    }

}