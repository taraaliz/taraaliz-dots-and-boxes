package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * An implementation of a mutable matrix backed by a [BooleanArray]. This matrix optimizes storing
 * Boolean values.
 */
class ArrayMutableBooleanMatrix :
    ArrayMutableBooleanMatrixBase,
    MutableBooleanMatrix {

    constructor(width: Int, height: Int) : super(width, height)

    constructor(
        width: Int,
        height: Int,
        initValue: Boolean
    ) : this(
        width,
        height,// Note that for a false value filling is not needed
        BooleanArray(width * height).apply { if (initValue) fill(initValue) })

    constructor(other: BooleanMatrix) : this(other.width, other.height, other.toFlatArray())

    @PublishedApi
    internal constructor(width: Int, height: Int, data: BooleanArray) :
            super(width, data)

    override fun copyOf(): ArrayMutableBooleanMatrix {
        return ArrayMutableBooleanMatrix(width, height, data.copyOf())
    }

    override fun contentEquals(other: BooleanMatrix): Boolean {
        if (width != other.width || height != other.height) return false
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (get(x, y) != other.get(x, y)) return false
            }
        }
        return true
    }

    override fun contentEquals(other: SparseMatrix<*>): Boolean = when (other) {
        is BooleanMatrix -> contentEquals(other)
        else -> super<ArrayMutableBooleanMatrixBase>.contentEquals(other)
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {
        /**
         * Create a new instance with given [width], [height] and initialized according to [init].
         */
        inline operator fun invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> Boolean
        ): ArrayMutableBooleanMatrix {
            return ArrayMutableBooleanMatrix(
                width,
                height,
                BooleanArray(width * height) { i -> init(i % width, i / width) })
        }


    }
}