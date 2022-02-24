package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMatrix

/**
 * Matrix that contains a single value for all coordinates.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property value The value for each cell
 */
open class SingleValueMatrix<T>(
    override val width: Int,
    override val height: Int,
    protected val value: T
) : AbstractMatrix<T>() {
    override fun doGet(x: Int, y: Int): T = value

    override fun copyOf(): SingleValueMatrix<T> {
        return SingleValueMatrix(width, height, value)
    }
}

