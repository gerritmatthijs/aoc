package aoc2024

import utils.Coordinate
import utils.toGrid
import java.io.File

fun main() {
    val grid = File("./src/main/resources/aoc2024/day10_input.txt").readLines()
        .map { line -> line.toList().map { it.toString().toInt() } }.toGrid()

    fun Iterable<Coordinate>.getAdjacentPoints(current: Int) = flatMap { (i, j) ->
            listOf(i + 1 to j, i - 1 to j, i to j + 1, i to j - 1).filter { (k, l) ->
                k >= 0 && l >= 0 && k < grid.vSize && l < grid.hSize && grid[k, l] == current + 1
            }
        }

    fun Set<Coordinate>.findNext(current: Int): Set<Coordinate> = if (current == 9) this else getAdjacentPoints(current).toSet().findNext(current + 1)

    fun Coordinate.findEndpoints() = setOf(this).findNext(0)

    val answerPart1 = grid.indices.filter { (i, j) -> grid[i, j] == 0}.sumOf { coord ->
        coord.findEndpoints().size
    }
    println("Answer part 1: $answerPart1")

    fun List<Coordinate>.findNext(current: Int): List<Coordinate> = if (current == 9) this else getAdjacentPoints(current).findNext(current + 1)

    fun Coordinate.findPaths() = listOf(this).findNext(0)

    val answerPart2 = grid.indices.filter { (i, j) -> grid[i, j] == 0}.sumOf { coord ->
        coord.findPaths().size
    }
    println("Answer part 2: $answerPart2")

}

