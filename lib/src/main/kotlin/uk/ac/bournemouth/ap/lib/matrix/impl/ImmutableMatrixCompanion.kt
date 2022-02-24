package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.ext.FunMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix

/**
 * Interface for the companion object of [Matrix] (and parent interface for companions of
 * subclasses). This enforces consistency, but also allows the companion to be used as factory.
 * @suppress
 */
interface ImmutableMatrixCompanion<B>: MatrixCompanion<B> {
    /**
     * Create a matrix where each value is determined by invoking the [valueFun] function. It
     * does not store any values.
     */
    fun <T : B> fromFunction(width: Int, height: Int, valueFun: (Int, Int) -> T): Matrix<T> =
        FunMatrix(width, height, valueFun)
}