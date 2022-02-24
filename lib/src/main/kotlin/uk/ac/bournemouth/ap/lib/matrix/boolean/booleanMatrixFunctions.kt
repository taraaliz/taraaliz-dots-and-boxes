package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.indices

/**
 * A map implementation that creates a boolean matrix based upon the receiver and the transformation
 * function.
 */
inline fun <T> Matrix<T>.mapBoolean(transform: (T) -> Boolean): BooleanMatrix {
    return BooleanMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * A map implementation that creates a boolean sparse matrix based upon the receiver and the
 * transformation function.
 */
inline fun <T> SparseMatrix<T>.mapBoolean(transform: (T) -> Boolean): SparseBooleanMatrix = when {
    validator == Matrix.VALIDATOR ||
            this is BooleanMatrix ->
        MutableBooleanMatrix(maxWidth, maxHeight).also { it.fill { x, y -> transform(get(x, y)) } }


    else -> {
        val validate: (Int, Int) -> Boolean = when (this) {
            is ArrayMutableSparseBooleanMatrix -> validator
            else -> { x, y -> isValid(x, y) }
        }

        MutableSparseBooleanMatrix(
            maxWidth,
            maxHeight,
            validator = validate,
            init = { x, y -> transform(get(x, y)) }
        )
    }
}

/**
 * Helper function to set values into a [MutableSparseBooleanMatrix].
 */
inline fun MutableSparseBooleanMatrix.fill(setter: (Int, Int) -> Boolean) {
    for ((x, y) in indices) {
        this[x, y] = setter(x, y)
    }
}
