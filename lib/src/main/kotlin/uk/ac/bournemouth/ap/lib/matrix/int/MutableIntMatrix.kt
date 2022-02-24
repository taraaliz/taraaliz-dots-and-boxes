package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableMatrixCompanion

/**
 * Interface representing a specialised mutable matrix type for storing integers.
 */
interface MutableIntMatrix : IntMatrix, MutableSparseIntMatrix, MutableMatrix<Int> {
    override fun copyOf(): MutableIntMatrix


    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object : MutableMatrixCompanion<Int> {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Int> invoke(original: Matrix<T>): MutableMatrix<T> =
            invoke(source = original) as MutableMatrix<T>

        operator fun invoke(source: Matrix<Int>): MutableIntMatrix = when (source) {
            is IntMatrix -> ArrayMutableIntMatrix(source)
            else -> ArrayMutableIntMatrix(source.width, source.height, source::get)
        }

        operator fun invoke(source: IntMatrix): MutableIntMatrix =
            ArrayMutableIntMatrix(source)

        /**
         * Create a [MutableIntMatrix] initialized with the default value of the [Int] type (`0`)
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         */
        operator fun invoke(width: Int, height: Int): MutableIntMatrix {
            return ArrayMutableIntMatrix(width, height)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Int> invoke(width: Int, height: Int, initValue: T): MutableMatrix<T> =
            invoke(width, height, initValue) as MutableMatrix<T>

        /**
         * Create a [MutableIntMatrix] initialized with the default value of the [Int] type (`0`)
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The initial value of the elements of the matrix
         */
        operator fun invoke(width: Int, height: Int, initValue: Int): MutableIntMatrix {
            return ArrayMutableIntMatrix(width, height).apply { fill(initValue) }
        }

        override fun <T : Int> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): MutableMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return ArrayMutableIntMatrix(width, height, init) as MutableMatrix<T>
        }

        inline operator fun invoke(width: Int, height: Int, init: (Int, Int) -> Int):
                MutableIntMatrix {
            return ArrayMutableIntMatrix(width, height, init)
        }

    }

}