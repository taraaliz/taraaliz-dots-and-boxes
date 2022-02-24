package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.fillWith
import java.util.function.Consumer

/**
 * Mutable matrix implementation based upon an array to store the data. It is the basis for
 * implementing both [ArrayMutableMatrix] and [ArrayMutableSparseMatrix].
 *
 * @constructor This constructor is for "internal use" in that it takes the data array as parameter.
 * @property maxWidth The maximum width of the matrix
 * @property data The actual array to get the data
 */
abstract class ArrayMutableMatrixBase<T> protected constructor(
    override val maxWidth: Int,
    internal val data: Array<T?>
) : AbstractMutableSparseMatrix<T>() {

    /**
     * Get the maximum height of
     */
    final override val maxHeight: Int get() = data.size / maxWidth

    /**
     * Create a new instance with the given initial value.
     *
     * @param width the width of the matrix
     * @param height the height of the matrix
     * @param initValue The initial value of each cell
     */
    @Suppress("UNCHECKED_CAST")
    protected constructor(width: Int, height: Int, initValue: T) :
            this(width, (arrayOfNulls<Any?>(width * height) as Array<T?>).fillWith(initValue))

    /**
     * Create a new instance taht copies the [original].
     */
    protected constructor(original: ArrayMutableMatrixBase<T>) : this(
        original.maxWidth,
        original.data.copyOf()
    )

    /**
     * Optimized implementation of forEach to perform [action] for each element in the array.
     */
    @Suppress("NewApi")
    override fun forEach(action: Consumer<in T>) {
        for(element in data) {
            if (element != ArraySparseMatrix.SPARSE_CELL) {
                action.accept(element as T)
            }
        }
    }

    /**
     * Creates a copy of the matrix of an appropriate type with the same content.
     */
    abstract override fun copyOf(): ArrayMutableMatrixBase<T>

    override fun doSet(x: Int, y: Int, value: T) {
        data[x + y * maxWidth] = value
    }

    final override fun doGet(x: Int, y: Int): T {
        @Suppress("UNCHECKED_CAST")
        return data[x + y * maxWidth] as T
    }

}