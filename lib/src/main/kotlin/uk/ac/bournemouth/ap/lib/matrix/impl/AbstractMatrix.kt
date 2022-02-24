package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.Matrix

/**
 * Abstract base class for [Matrix] implementations.
 * @suppress
 */
abstract class AbstractMatrix<T> protected constructor() : AbstractSparseMatrix<T>(), Matrix<T> {
    abstract override val width: Int
    abstract override val height: Int

    final override val maxWidth: Int get() = width
    final override val maxHeight: Int get() = height

    /** Get a string representation of the matrix (line wrapped) */
    override fun toString(): String = toString("Matrix")
}