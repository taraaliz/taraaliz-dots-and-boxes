package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueSparseMatrix

/**
 * [SparseBooleanMatrix] that contains a single value for all nonsparse coordinates.
 * @property maxWidth The width of the matrix
 * @property maxHeight The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
class SingleValueSparseBooleanMatrix(
    maxWidth: Int, maxHeight: Int, value: Boolean,
    override val validator: (Int, Int) -> Boolean
) : SingleValueSparseMatrix<Boolean>(maxWidth, maxHeight, value, validator), SparseBooleanMatrix {

    override fun copyOf(): SingleValueSparseBooleanMatrix =
        SingleValueSparseBooleanMatrix(maxWidth, maxHeight, value, validator)
}