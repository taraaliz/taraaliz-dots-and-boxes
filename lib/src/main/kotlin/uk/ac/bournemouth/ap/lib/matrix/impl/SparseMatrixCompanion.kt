package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Interface for the companion object of [SparseMatrix] (and parent interface for companions of
 * subclasses). This enforces consistency, but also allows the companion to be used as factory.
 * @suppress
 */
interface SparseMatrixCompanion<B> {
    /**
     * Create a new [SparseMatrix] that is a copy of the original.
     */
    operator fun <T : B> invoke(original: SparseMatrix<T>): SparseMatrix<Any?>

    /**
     * Create a new [SparseMatrix] with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param initValue An value for the non-sparse elements of the matrix
     * @param validator A function that is used to determine whether a particular coordinate is contained
     *                 in the matrix.
     */
    operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        initValue: T,
        validator: (Int, Int) -> Boolean
    ): SparseMatrix<Any?>


    /**
     * Create a new [SparseMatrix] subclass instance with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param validator A function that is used to determine whether a particular coordinate is contained
     *                 in the matrix.
     * @param init An initialization function that sets the values for the matrix.
     */
    operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        validator: (Int, Int) -> Boolean,
        init: (Int, Int) -> T
    ): SparseMatrix<Any?>

    /**
     * Create a new [SparseMatrix] with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param init An initialization function that sets the values for the matrix.
     */
    operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
    ): SparseMatrix<Any?>

    /**
     * Factory unction to create an instance based upon the matrix of [SparseValue]s as initializers.
     * This is not an invoke operator due to overloading issues
     */
    fun <T : B> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): SparseMatrix<T>
}