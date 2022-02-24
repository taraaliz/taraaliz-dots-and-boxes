package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueSparseMatrix

/**
 * [SparseIntMatrix] that contains a single value for all nonsparse coordinates.
 * @property maxWidth The width of the matrix
 * @property maxHeight The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
class SingleValueSparseIntMatrix(
    maxWidth: Int, maxHeight: Int, value: Int,
    override val validator: (Int, Int) -> Boolean
) : SingleValueSparseMatrix<Int>(maxWidth, maxHeight, value, validator), SparseIntMatrix {

    override fun copyOf(): SingleValueSparseIntMatrix =
        SingleValueSparseIntMatrix(maxWidth, maxHeight, value, validator)
}