package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.indices

/**
 * Helper function to set values into a [MutableSparseMatrix].
 */
inline fun <T> MutableSparseMatrix<T>.fill(setter: (Int, Int) -> T) {
    for ((x, y) in indices) {
        this[x, y] = setter(x, y)
    }
}

/**
 * A map function that creates a new matrix with each cell mapped by [transform].
 *
 * @receiver The matrix to copy from. This also determines the dimensions
 * @param transform The function used to transform the
 */
inline fun <T, R> Matrix<T>.map(transform: (T) -> R): Matrix<R> {
    return Matrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * A map function that creates a new (sparse) matrix with each cell mapped by [transform].
 *
 * @receiver The matrix to copy from. This also determines the dimensions
 * @param transform The function used to transform the cell values
 */
inline fun <T, R> SparseMatrix<T>.map(transform: (T) -> R): SparseMatrix<R> = when {
    validator == Matrix.VALIDATOR ||
            this is Matrix<T> -> (this as Matrix<T>).map(transform) //delegate to more specific function

    else -> MutableSparseMatrix(
        maxWidth,
        maxHeight,
        validator = validator,
        init = { x, y -> transform(get(x, y)) }
    )
}

/**
 * Perform the [action] for each index in the (sparse) matrix. This skips sparse indices.
 */
inline fun SparseMatrix<*>.forEachIndex(action: (Int, Int) -> Unit) {
    for (x in 0 until maxWidth) {
        for (y in 0 until maxHeight) {
            if (isValid(x, y)) {
                action(x, y)
            }
        }
    }
}

/**
 * Perform the [action] for each index in the matrix. This version uses the non-sparse nature of
 * the matrix.
 */
inline fun Matrix<*>.forEachIndex(action: (Int, Int) -> Unit) {
    for (x in 0 until maxWidth) {
        for (y in 0 until maxHeight) {
            action(x, y)
        }
    }
}

/**
 * Perform the [action] for each value in the (sparse) matrix.
 */
inline fun <T> SparseMatrix<T>.forEach(action: (T) -> Unit) {
    for (x in 0 until maxWidth) {
        for (y in 0 until maxHeight) {
            if (isValid(x, y)) {
                action(get(x, y))
            }
        }
    }
}

/**
 * Perform the [action] for each value in the matrix. This version uses the non-sparse nature of
 * the matrix.
 */
inline fun <T> Matrix<T>.forEach(action: (T) -> Unit) {
    for (x in 0 until maxWidth) {
        for (y in 0 until maxHeight) {
            action(get(x, y))
        }
    }
}
