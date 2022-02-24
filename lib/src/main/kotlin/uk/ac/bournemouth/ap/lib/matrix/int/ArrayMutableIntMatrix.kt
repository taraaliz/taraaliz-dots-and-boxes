package uk.ac.bournemouth.ap.lib.matrix.int

/**
 * An implementation of a mutable matrix backed by an [IntArray]. This matrix optimizes storing Int
 * values.
 */
class ArrayMutableIntMatrix :
    ArrayMutableIntMatrixBase,
    MutableIntMatrix {

    constructor(width: Int, height: Int) : super(width, height)

    constructor(other: IntMatrix) : this(other.width, other.height, other.toFlatArray())

    private constructor(maxWidth: Int, maxHeight: Int, data: IntArray) : super(
        maxWidth,
        maxHeight,
        data
    )

    override fun fill(element: Int) {
        data.fill(element)
    }

    override fun copyOf(): ArrayMutableIntMatrix {
        return ArrayMutableIntMatrix(width, height, data.copyOf())
    }

    override fun contentEquals(other: IntMatrix): Boolean {
        if (width != other.width || height != other.height) return false
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (get(x, y) != other.get(x, y)) return false
            }
        }
        return true
    }

    override fun contentEquals(other: SparseIntMatrix): Boolean = when (other) {
        is IntMatrix -> contentEquals(other)
        else -> super<MutableIntMatrix>.contentEquals(other)
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {

        /**
         * Create a new instance with given [width], [height] and initialized according to [init].
         */
        inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: (Int, Int) -> Int
        ): ArrayMutableIntMatrix {
            val matrix = ArrayMutableIntMatrix(maxWidth, maxHeight)
            for (x in 0 until maxWidth) {
                for (y in 0 until maxHeight) {
                    matrix[x, y] = init(x, y)
                }
            }
            return matrix
        }
    }
}