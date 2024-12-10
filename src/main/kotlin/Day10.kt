package org.example

import java.io.File

fun main() {
    val grid = File("./src/main/resources/day10_input.txt").readLines()
        .map { line -> line.toList().map { it.toString().toInt() } }
        .let { Grid(it) }

    fun Set<Coordinate>.findNext(current: Int): Set<Coordinate> = if (current == 9) this else flatMap { (i, j) ->
        listOf(i + 1 to j, i - 1 to j, i to j + 1, i to j - 1).filter { (k, l) ->
            k >= 0 && l >= 0 && k < grid.hSize && l < grid.vSize && grid[k, l] == current + 1 }
    }.toSet().findNext(current + 1)

    fun Coordinate.findEndpoints() = setOf(this).findNext(0)

    val answerPart1 = grid.indices.filter { (i, j) -> grid[i, j] == 0}.sumOf { coord ->
        coord.findEndpoints().size
    }
    println("Answer part 1: $answerPart1")
    grid.indices.filter { (i, j) -> grid[i, j] == 0}.forEach { (i, j) ->
        println("($i, $j): ${(i to j).findEndpoints()}")
    }

}

