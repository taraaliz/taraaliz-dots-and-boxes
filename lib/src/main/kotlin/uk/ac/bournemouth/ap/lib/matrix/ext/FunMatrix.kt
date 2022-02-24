package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMatrix

/**
 * [Matrix] where values are determined by a function, not storage.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property valueFun The function that determines the value of a cell
 */
class FunMatrix<T>(
    override val width: Int,
    override val height: Int,
    val valueFun: (Int, Int) -> T
) : AbstractMatrix<T>() {
    override fun doGet(x: Int, y: Int): T {
        return valueFun(x, y)
    }

    override fun copyOf(): FunMatrix<T> {
        return FunMatrix(width, height, valueFun)
    }
}