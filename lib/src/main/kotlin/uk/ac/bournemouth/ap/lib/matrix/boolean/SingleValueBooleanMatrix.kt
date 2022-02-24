package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueMatrix

/**
 * [BooleanMatrix] that contains a single value for all nonsparse coordinates.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
class SingleValueBooleanMatrix(width: Int, height: Int, value: Boolean) :
    SingleValueMatrix<Boolean>(width, height, value), BooleanMatrix {
    @Deprecated("Do not call this directly it is meaningless")
    override fun toFlatArray(): BooleanArray = BooleanArray(width * height) { value }

    override fun contentEquals(other: BooleanMatrix): Boolean = when (other) {
        is SingleValueBooleanMatrix -> other.value == value
        else -> other.all { it == value }
    }

    override fun copyOf(): SingleValueBooleanMatrix {
        return SingleValueBooleanMatrix(width, height, value)
    }
}