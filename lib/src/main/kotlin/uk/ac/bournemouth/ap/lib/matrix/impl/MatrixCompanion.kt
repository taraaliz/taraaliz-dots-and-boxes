package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix

/**
 * Interface that serves as base interface for the companion object of [Matrix] and [MutableMatrix]
 * (and parent interface for companions of subclasses). This enforces consistency, but also allows
 * the companion to be used as factory. This happens through [MutableMatrixCompanion] and
 * [ImmutableMatrixCompanion] that provide access to either matrix or mutable matrix
 * implementations.
 * @suppress
 */
interface MatrixCompanion<B> {
    /**
     * Create a new matrix that is a copy of the original. The copy is a shallow copy.
     * @param original the matrix to copy (dimensions and content).
     */
    operator fun <T : B> invoke(original: Matrix<T>): Matrix<Any?>

    /**
     * Create a new matrix with the given dimensions and initial value. Note that this function
     * is provided for concistency on [Matrix], but [SingleValueMatrix] is recommended for the
     * usecase.
     *
     * @param width The width of the matrix to create
     * @param height The height of the matrix to create
     * @param initValue The initial value of each cell (this is also the actual value for read-only matrices)
     */
    operator fun <T : B> invoke(width: Int, height: Int, initValue: T): Matrix<Any?>

    /**
     * Create a new matrix with the given dimensions and initialised using the [init] function.
     *
     * @param width The width of the matrix to create
     * @param height The height of the matrix to create
     * @param init The function that determines the initial value for each function
     */
    operator fun <T : B> invoke(width: Int, height: Int, init: (Int, Int) -> T): Matrix<Any?>
}