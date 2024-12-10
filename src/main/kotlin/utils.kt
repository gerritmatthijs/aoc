package org.example

class Grid<T>(private val contents: List<List<T>>) : Iterable<T> {
    private val flattened = contents.flatten()
    val hSize = contents.size
    val vSize = contents.first().size
    val indices: List<Coordinate> = contents.indices.flatMap { i ->
        contents[i].indices.map { j -> i to j }
    }

    operator fun get(i: Int, j: Int) = contents[i][j]

    operator fun get(i: Int) = contents[i]

    override fun iterator(): Iterator<T> = flattened.iterator()

    override fun toString(): String = contents.joinToString(separator = "\n")
}

typealias Coordinate = Pair<Int, Int>

fun main() {
    val grid = Grid(listOf(listOf(1, 2), listOf(3, 4)))
    println(grid)
}