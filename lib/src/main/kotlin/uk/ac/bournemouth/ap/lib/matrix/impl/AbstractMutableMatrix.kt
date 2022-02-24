package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix

/**
 * Abstract base class for [MutableMatrix] implementations.
 * @suppress
 */
abstract class AbstractMutableMatrix<T> protected constructor() : AbstractMatrix<T>(),
    MutableMatrix<T> {

    final override fun set(x: Int, y: Int, value: T) {
        validate(x, y)
        doSet(x, y, value)
    }

    /** Actual implementation of setting values, does not need validation */
    abstract fun doSet(x: Int, y: Int, value: T)

    /** Get a string representation of the matrix (line wrapped) */
    override fun toString(): String = toString("MutableMatrix")
}