package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Helper function to fill an array with a specific value and return it as a possibly non-null type.
 */
internal fun <T> Array<T?>.fillWith(value: T): Array<T> {
    fill(value)
    @Suppress("UNCHECKED_CAST") // this will be correct per rules of fill
    return this as Array<T>
}


/**
 * Helper function for implementing matrices that throws an exception if the
 * coordinates are out of range.
 */
internal fun SparseMatrix<*>.validate(x: Int, y: Int) {
    if (!isValid(x, y)) {
        when { // In this case we distinguish between a sparse cell and an out-of-range one for sanity.
            x in 0 until maxWidth && y in 0 until maxHeight ->
                throw IndexOutOfBoundsException("($x, $y) is a sparse index")

            else ->
                throw IndexOutOfBoundsException("($x,$y) out of range: ([0,$maxWidth), [0,$maxHeight))")
        }
    }
}

/**
 * Helper function for implementing matrices that throws an exception if the
 * coordinates are out of range.
 */
internal fun Matrix<*>.validate(x: Int, y: Int) {
    if (x !in 0 until width || y !in 0 until height) {
        throw IndexOutOfBoundsException("($x,$y) out of range: ([0,$width), [0,$height))")
    }
}
