package org.example

import java.io.File

fun main() {
    val grid = File("./src/main/resources/day8_input.txt").readLines().map { it.toList() }.toGrid()
    val antennas = grid.indices.filterNot { (i, j) -> grid[i,j] == '.' }.groupBy { (i, j) -> grid[i, j] }

    fun Pair<Coordinate, Coordinate>.getSingleAntinodes() = let { (a, b) ->
        listOf((2 * a.x - b.x) to (2 * a.y - b.y), (2 * b.x - a.x) to (2 * b.y - a.y))
    }

    fun List<Coordinate>.getAntinodes(antinodeFinder: Pair<Coordinate, Coordinate>.() -> List<Coordinate>) = indices.flatMap { i ->
        (0..<i).flatMap { j -> (this[i] to this[j]).antinodeFinder() }
    }

    val antinodePositions = antennas.values.flatMap { frequencyGroup -> frequencyGroup.getAntinodes { getSingleAntinodes() } }.toSet().filter { it.isWithinGrid(grid) }

    val answerPart1 = antinodePositions.size
    println("Answer part 1: $answerPart1")

    fun Pair<Coordinate, Coordinate>.getAllAntinodes() = let { (a, b) ->
        generateSequence(b) { (x, y) -> x + b.x - a.x to y + b.y - a.y }.takeWhile { it.isWithinGrid(grid) } +
                generateSequence(a) { (x, y) -> x + a.x - b.x to y + a.y - b.y }.takeWhile { it.isWithinGrid(grid) }
    }.toList()

    val allAntinodePositions = antennas.values.flatMap { frequencyGroup -> frequencyGroup.getAntinodes { getAllAntinodes() } }.toSet().filter { it.isWithinGrid(grid) }

    val answerPart2 = allAntinodePositions.size
    println("Answer part 2: $answerPart2")

//    fun Coordinate.containsAntinode(): Boolean = antennas.any
}

