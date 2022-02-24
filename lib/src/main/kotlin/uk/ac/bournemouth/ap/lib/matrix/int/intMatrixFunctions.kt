package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.indices
import uk.ac.bournemouth.ap.lib.matrix.forEachIndex


/**
 * Create a new [IntMatrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
inline fun <T> Matrix<T>.mapInt(transform: (T) -> Int): IntMatrix {
    return IntMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * Create a new [IntMatrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
inline fun IntMatrix.mapInt(transform: (Int) -> Int): IntMatrix {
    return IntMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * Create a new [SparseIntMatrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
inline fun <T> SparseMatrix<T>.mapInt(transform: (T) -> Int): SparseIntMatrix = when {
    validator == Matrix.VALIDATOR ||
            this is IntMatrix ->
        MutableIntMatrix(maxWidth, maxHeight).also { it.fill { x, y -> transform(get(x, y)) } }


    else -> {
        val validate: (Int, Int) -> Boolean = when (this) {
            is ArrayMutableSparseIntMatrix -> validator
            else -> { x, y -> isValid(x, y) }
        }

        MutableSparseIntMatrix(
            maxWidth,
            maxHeight,
            validator = validate
        ).also {
            it.fill { x, y -> transform(get(x, y)) }
        }
    }
}

/**
 * Create a new [Matrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
inline fun <R> IntMatrix.mapInt(transform: (Int) -> R): Matrix<R> {
    return MutableMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * Helper function to set values into a [MutableIntSparseMatrix].
 */
inline fun MutableSparseIntMatrix.fill(setter: (Int, Int) -> Int) {
    for ((x, y) in indices) {
        this[x, y] = setter(x, y)
    }
}

/**
 * Element-wise addition of two matrices. It requires the [other] matrix to be strictly larger
 * than the left side matrix. The result is a matrix of the size of the receiver.
 */
operator fun IntMatrix.plus(other: Int): IntMatrix =
    IntMatrix(width, height) { x, y -> get(x, y) + other }

/**
 * Element-wise subtraction of two matrices. It requires the [other] matrix to be strictly larger
 * than the left side matrix. The result is a matrix of the size of the receiver.
 */
operator fun IntMatrix.minus(other: Int): IntMatrix =
    IntMatrix(width, height) { x, y -> get(x, y) - other }

/**
 * Element-wise division of two matrices. It requires the [other] matrix to be strictly larger
 * than the left side matrix. The result is a matrix of the size of the receiver.
 */
operator fun IntMatrix.div(other: Int): IntMatrix =
    IntMatrix(width, height) { x, y -> get(x, y) / other }

/**
 * Element-wise remainder of two matrices. It requires the [other] matrix to be strictly larger
 * than the left side matrix. The result is a matrix of the size of the receiver.
 */
operator fun IntMatrix.rem(other: Int): IntMatrix =
    IntMatrix(width, height) { x, y -> get(x, y) % other }

/**
 * Element-wise addition of two matrices into the receiver. It requires the [other] matrix to be
 * strictly larger than the left side matrix.
 */
operator fun MutableIntMatrix.plusAssign(other: Int) =
    forEachIndex { x, y -> set(x, y, get(x, y) + other) }

/**
 * Element-wise subtraction of two matrices into the receiver. It requires the [other] matrix to be
 * strictly larger than the left side matrix.
 */
operator fun MutableIntMatrix.minusAssign(other: Int) =
    forEachIndex { x, y -> set(x, y, get(x, y) - other) }

/**
 * Element-wise division of two matrices into the receiver. It requires the [other] matrix to be
 * strictly larger than the left side matrix.
 */
operator fun MutableIntMatrix.divAssign(other: Int) =
    forEachIndex { x, y -> set(x, y, get(x, y) / other) }

/**
 * Element-wise remainder of two matrices into the receiver. It requires the [other] matrix to be
 * strictly larger than the left side matrix.
 */
operator fun MutableIntMatrix.remAssign(other: Int) =
    forEachIndex { x, y -> set(x, y, get(x, y) % other) }

/**
 * Multiply the two matrices (using matrix multiplication). This requires the width of the left matrix to be equal to the height of
 * the right matrix.
 */
operator fun IntMatrix.times(other: IntMatrix): IntMatrix {
    if (width != other.height) throw IllegalArgumentException("Matrix multiplication requires the width of the first operand to match the height of the second")
    val common = width
    return IntMatrix(other.width, height) { x, y ->
        var sum = 0
        for (n in 0 until common) {
            sum += get(n, y) * other.get(x, n)
        }
        sum
    }
}
