package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix

/**
 * Interface for the companion object of [MutableMatrix] (and parent interface for companions of
 * subclasses). This enforces consistency, but also allows the companion to be used as factory.
 * @suppress
 */
interface MutableMatrixCompanion<B> : MatrixCompanion<B> {
    override fun <T : B> invoke(original: Matrix<T>): MutableMatrix<T>
    override fun <T : B> invoke(width: Int, height: Int, initValue: T): MutableMatrix<T>
    override operator fun <T : B> invoke(
        width: Int,
        height: Int,
        init: (Int, Int) -> T
    ): MutableMatrix<T>
}