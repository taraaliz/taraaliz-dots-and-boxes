@file:Suppress("UNCHECKED_CAST")

package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.fillWith
import java.util.function.Consumer

/**
 *  Mutable matrix class backed by an array.
 *
 *  @constructor This is an internal constructor that exposes the data format. Use the factory
 *               functions.
 */
class ArrayMutableMatrix<T> @PublishedApi internal constructor(
    override val width: Int,
    private val data: Array<T?>
) : AbstractMutableMatrix<T>(),
    MutableMatrix<T> {
    override val height: Int = data.size / width

    /**
     * Create a matrix that is initialized with a single value.
     *
     * @param width The width of the matrix
     * @param height The (initial) height of the matrix
     * @param initValue The initial value for each cell
     */
    constructor(width: Int, height: Int, initValue: T) : this(
        width,
        (arrayOfNulls<Any?>(width * height) as Array<T?>).fillWith(initValue)
    )

    /**
     * Create a matrix by copying the [original] matrix. Note that
     * this doesn't do a deep copy (copy the value objects).
     * @param original The matrix to copy from.
     */
    constructor(original: Matrix<T>) : this(
        original.maxWidth,
        when (original) {
            is ArrayMutableMatrix -> original.data.copyOf()
            else -> {
                val w = original.width
                val h = original.height
                Array<Any?>(w * h) { original[it % w, it / w] } as Array<T?>
            }
        })

    override fun doSet(x: Int, y: Int, value: T) {
        data[x + y * maxWidth] = value
    }

    override fun doGet(x: Int, y: Int): T {
        @Suppress("UNCHECKED_CAST")
        return data[x + y * maxWidth] as T
    }

    /**
     * Creates a copy of the matrix of an appropriate type with the same content.
     */
    override fun copyOf(): ArrayMutableMatrix<T> =
        ArrayMutableMatrix(this)

    /**
     * Optimized implementation of forEach.
     */
    @Suppress("NewApi")
    override fun forEach(action: Consumer<in T>) {
        for(element in data) {
            action.accept(element as T)
        }
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    companion object {
        /**
         * Create and initialize a matrix.
         *
         * @param width The width of the matrix to create.
         * @param height The height of the matrix to create.
         * @param init Initializer that determines the initial value for the given coordinate.
         */
        inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): ArrayMutableMatrix<T> {
            val data = Array<Any?>(width * height) { init(it % width, it / width) } as Array<T?>
            return ArrayMutableMatrix(width, data)
        }
    }
}
