package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.MatrixIndices
import uk.ac.bournemouth.ap.lib.matrix.impl.SparseMatrixIndices

/**
 * In many cases it is easier to work with a single coordinate rather than
 * with a pair of coordinates. The coordinate class allows this.
 */
@JvmInline
value class Coordinate constructor(@PublishedApi internal val packed: Int) {

    /**
     * The coordinates actually only store shorts
     */
    constructor(x: Short, y: Short): this (((x.toInt() and 0xffff) shl 16) or (y.toInt() and 0xffff))

    /** Helper constructor to create a coordinate. */
    constructor(x: Int, y: Int): this(x.toShort(), y.toShort())

    /**
     * The x part of the coordinate
     */
    val x:Int inline get() = when((packed shr 16) and 0x8000) {
        0 -> (packed shr 16) and 0x7fff
        else -> (packed shr 16 or (0xffff shl 16))
    }

    /**
     * The y part of the coordinate
     */
    val y:Int inline get() = when(packed and 0x8000) {
        0 -> packed and 0x7fff
        else -> (packed and 0xffff or (0xffff shl 16))
    }

    /** Decomposition operator for x coordinate */
    operator fun component1(): Int=x
    /** Decomposition operator for y coordinate */
    operator fun component2(): Int=y

    override fun toString(): String = "($x, $y)"
}

/**
 * Helper function that implements [SparseMatrix.isValid] for coordinates
 */
fun <T> SparseMatrix<T>.isValid(pos: Coordinate): Boolean = isValid(pos.x, pos.y)

/**
 * Helper operator to get values based on a coordinate
 */
operator fun <T> SparseMatrix<T>.get(pos: Coordinate): T = get(pos.x, pos.y)

/**
 * Helper operator to set values based upon a coordinate
 */
operator fun <T> MutableSparseMatrix<T>.set(pos: Coordinate, value: T){ set(pos.x, pos.y, value) }

     /** Get an iterable with all valid indices in the matrix */
val SparseMatrix<*>.indices: Iterable<Coordinate> get() = SparseMatrixIndices(this)

val Matrix<*>.indices: Iterable<Coordinate> get() = MatrixIndices(this)
