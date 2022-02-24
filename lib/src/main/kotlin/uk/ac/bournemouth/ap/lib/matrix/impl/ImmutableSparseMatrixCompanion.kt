package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.ext.FunSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Interface for the companion object of immutable sparse matrix implementations (and parent
 * interface for companions of subclasses). This enforces consistency, but also allows the companion
 * to be used as factory.
 * @suppress
 */
interface ImmutableSparseMatrixCompanion<B> : SparseMatrixCompanion<B> {
    /**
     * Create a new [SparseMatrix] that is a copy of the original.
     */
    override operator fun <T : B> invoke(original: SparseMatrix<T>): SparseMatrix<T>

    /**
     * Create a new [SparseMatrix] with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param initValue An value for the non-sparse elements of the matrix
     * @param validator A function that is used to determine whether a particular coordinate is contained
     *                 in the matrix.
     */
    override operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        initValue: T,
        validator: (Int, Int) -> Boolean
    ): SparseMatrix<T>


    /**
     * Create a new [SparseMatrix] with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param validator A function that is used to determine whether a particular coordinate is contained
     *                 in the matrix.
     * @param init An initialization function that sets the values for the matrix.
     */
    override operator fun <T : B> invoke(
        maxWidth: Int,
        maxHeight: Int,
        validator: (Int, Int) -> Boolean,
        init: (Int, Int) -> T
    ): SparseMatrix<T>

    /**
     * Create a new lazy [SparseMatrix] with the given size and value function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param validator A function that is used to determine whether a particular coordinate is contained
     *                 in the matrix.
     * @param valueFun A function that provides the value at a coordinate
     */
    fun <T : B> function(
        maxWidth: Int,
        maxHeight: Int,
        validator: (Int, Int) -> Boolean,
        valueFun: (Int, Int) -> T
    ): SparseMatrix<T> = FunSparseMatrix(maxWidth, maxHeight, validator, valueFun)

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
    ): SparseMatrix<T>

    /**
     * Create a new lazy [SparseMatrix] with the given size and intialization function. It also requires
     * a validate function
     * @param maxWidth The width of the matrix
     * @param maxHeight The height of the matrix
     * @param valueFun An initialization function that provides the validity/value of the sparse matrix
     */
    fun <T : B> function(
        maxWidth: Int,
        maxHeight: Int,
        valueFun: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
    ): SparseMatrix<T> = FunSparseMatrix(maxWidth, maxHeight, valueFun)

    override fun <T : B> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): SparseMatrix<T>
}