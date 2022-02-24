package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractSparseMatrix

/**
 * Matrix that contains a single value for all nonsparse coordinates.
 * @property maxWidth The width of the matrix
 * @property maxHeight The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
open class SingleValueSparseMatrix<T>(
    override val maxWidth: Int,
    override val maxHeight: Int,
    protected val value: T,
    override val validator: (Int, Int) -> Boolean
) : AbstractSparseMatrix<T>() {
    override fun doGet(x: Int, y: Int): T = value

    override fun copyOf(): SparseMatrix<T> {
        return SingleValueSparseMatrix(maxWidth, maxHeight, value, validator)
    }
}