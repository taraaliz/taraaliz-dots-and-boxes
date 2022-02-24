package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableSparseMatrix

/**
 * Helper base class for array based int matrices.
 */
abstract class ArrayMutableIntMatrixBase internal constructor(
    override val maxWidth: Int,
    override val maxHeight: Int,
    protected val data: IntArray
) : AbstractMutableSparseMatrix<Int>(), MutableSparseIntMatrix {

    constructor(maxWidth: Int, maxHeight: Int) :
            this(maxWidth, maxHeight, IntArray(maxWidth * maxHeight))

    /**
     * Get a onedimensional array that stores all elements of the matrix.
     */
    fun toFlatArray(): IntArray {
        return data.copyOf()
    }

    final override fun doGet(x: Int, y: Int): Int = data[y * maxWidth + x]

    override fun doSet(x: Int, y: Int, value: Int) {
        data[y * maxWidth + x] = value
    }

    override fun contentEquals(other: SparseIntMatrix): Boolean {
        val maxX = maxOf(maxWidth, other.maxWidth)
        val maxY = maxOf(maxHeight, other.maxHeight)
        for (x in 0 until maxX) {
            for (y in 0 until maxY) {
                val valid = isValid(x, y)
                val otherValid = other.isValid(x, y)
                if (valid != otherValid) return false
                if (valid) {
                    if (get(x, y) != other.get(x, y)) return false
                }
            }
        }
        return true
    }

}