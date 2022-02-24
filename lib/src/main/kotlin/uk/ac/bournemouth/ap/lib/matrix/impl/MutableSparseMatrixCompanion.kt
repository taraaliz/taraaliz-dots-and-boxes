package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Interface for the companion object of [MutableSparseMatrix] (and parent interface for companions of
 * subclasses). This enforces consistency, but also allows the companion to be used as factory.
 * @suppress
 */
interface MutableSparseMatrixCompanion<B> : SparseMatrixCompanion<B> {
    override fun <T : B> invoke(original: SparseMatrix<T>): MutableSparseMatrix<T>

    override fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        initValue: T,
        validator: (Int, Int) -> Boolean
    ): MutableSparseMatrix<T>

    override operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        validator: (Int, Int) -> Boolean,
        init: (Int, Int) -> T
    ): MutableSparseMatrix<T>

    /**
     * Create a new [SparseMatrix] with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param init An initialization function that sets the values for the matrix.
     */
    override operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
    ): MutableSparseMatrix<T>

    override fun <T : B> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): MutableSparseMatrix<T>
}