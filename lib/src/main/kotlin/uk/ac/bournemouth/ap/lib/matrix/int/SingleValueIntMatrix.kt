package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueMatrix


/**
 * [IntMatrix] that contains a single value for all nonsparse coordinates.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
class SingleValueIntMatrix(width: Int, height: Int, value: Int) :
    SingleValueMatrix<Int>(width, height, value), IntMatrix {
    @Deprecated("Do not call this directly it is meaningless")
    override fun toFlatArray(): IntArray = IntArray(width * height) { value }

    override fun contentEquals(other: IntMatrix): Boolean = when (other) {
        is SingleValueIntMatrix -> other.value == value
        else -> other.all { it == value }
    }

    override fun copyOf(): SingleValueIntMatrix {
        return SingleValueIntMatrix(width, height, value)
    }
}

