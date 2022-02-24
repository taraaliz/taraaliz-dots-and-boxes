package uk.ac.bournemouth.ap.lib.matrix.int
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableMatrixCompanion

interface IntMatrix : Matrix<Int>, SparseIntMatrix {
    fun toFlatArray(): IntArray
    fun contentEquals(other: IntMatrix): Boolean
    override fun copyOf(): IntMatrix

    override fun sum(): Int {
        var total = 0
        forEach { total += it }
        return total
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : ImmutableMatrixCompanion<Int> {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Int> invoke(original: Matrix<T>): Matrix<T> =
            invoke(source = original) as Matrix<T>

        operator fun invoke(source: Matrix<Int>): IntMatrix = when (source) {
            is IntMatrix -> ArrayMutableIntMatrix(source)
            else -> ArrayMutableIntMatrix(source.width, source.height, source::get)
        }

        operator fun invoke(source: IntMatrix): IntMatrix =
            ArrayMutableIntMatrix(source)

        @Suppress("UNCHECKED_CAST")
        override fun <T : Int> invoke(width: Int, height: Int, initValue: T): Matrix<T> =
            invoke(width, height, initValue) as Matrix<T>

        /**
         * Create an [IntMatrix] initialized with the given value
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The value of all cells
         */
        operator fun invoke(width: Int, height: Int, initValue: Int): IntMatrix {
            return SingleValueIntMatrix(width, height, initValue)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Int> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): Matrix<T> {
            @Suppress("UNCHECKED_CAST")
            return ArrayMutableIntMatrix(width, height, init) as Matrix<T>
        }

        /**
         * Create an [IntMatrix] initialized according to the init function.
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param init Function used to initialise each cell.
         */
        inline operator fun invoke(width: Int, height: Int, init: (Int, Int) -> Int):
                IntMatrix {
            return ArrayMutableIntMatrix(width, height, init)
        }

    }

}


