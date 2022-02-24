package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMatrix
import java.util.function.Consumer

/**
 * An implementation of a read-only matrix that is backed by an array.
 *
 * @constructor Internal version that exposes the underlying storage format.
 */
class ArrayMatrix<T> @PublishedApi internal constructor(
    private val data: Array<T>,
    override val width: Int
) : AbstractMatrix<T>() {
    override val height: Int = data.size / width

    override fun doGet(x: Int, y: Int): T {
        return data[x + y * width]
    }

    /**
     * Create a (shallow) copy of this matrix.
     */
    override fun copyOf(): ArrayMatrix<T> = ArrayMatrix(data, width)


    /**
     * Optimized implementation of forEach.
     */
    @Suppress("NewApi")
    override fun forEach(action: Consumer<in T>) {
        for(element in data) {
            action.accept(element)
        }
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {

        /**
         * Factory function to create an [ArrayMatrix].
         *
         * @param width The width of the matrix.
         * @param height The height of the matrix.
         * @param init Function that determines the value at a particular location.
         */
        @Suppress("UNCHECKED_CAST")
        inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): ArrayMatrix<T> = ArrayMatrix(
            Array<Any?>(width * height) {
                init(it % width, it / width)
            } as Array<T>,
            width
        )
    }
}