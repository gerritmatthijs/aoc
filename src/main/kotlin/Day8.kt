package org.example

import java.io.File

fun main() {
    val grid = File("./src/main/resources/day8_input.txt").readLines().map { it.toList() }.toGrid()
    val antennas = grid.indices.filterNot { (i, j) -> grid[i,j] == '.' }.groupBy { (i, j) -> grid[i, j] }

    fun Pair<Coordinate, Coordinate>.getAntinodes() = let { (a, b) ->
        listOf((2 * a.x - b.x) to (2 * a.y - b.y), (2 * b.x - a.x) to (2 * b.y - a.y))
    }

    fun List<Coordinate>.getAntinodes() = indices.flatMap { i ->
        (0..<i).flatMap { j -> (this[i] to this[j]).getAntinodes() }
    }

    val antinodePositions = antennas.values.flatMap { frequencyGroup -> frequencyGroup.getAntinodes() }.toSet().filter { (i, j) ->
        i >= 0 && i < grid.vSize && j >= 0 && j < grid.hSize
    }

    val answerPart1 = antinodePositions.size
    println("Answer part 1: $answerPart1")

//    fun Coordinate.containsAntinode(): Boolean = antennas.any
}

