package aoc2025

import utils.Coordinate
import utils.Grid
import utils.getSurroundingWithinGrid
import utils.toGrid
import java.io.File
import kotlin.time.measureTime

fun main() {
    val grid = File("./src/main/resources/aoc2025/day4_input.txt").readText().toGrid().map { it == '@' }

    measureTime {
        println("Answer part 1: ${grid.indices.count { it.isAccessible(grid) }}")
    }.also { println("Time taken: $it") }


    measureTime {
        val finalGrid = generateSequence(grid) { currentGrid ->
            currentGrid.mapIndexed { coordinate, hasRoll ->
                hasRoll && !coordinate.isAccessible(currentGrid)
            }.takeIf { newGrid -> newGrid != currentGrid }
        }.last()
        println("Answer part 2: ${ grid.count { it } - finalGrid.count { it }}")
    }.also { println("Time taken: $it") }
}

fun Coordinate.isAccessible(grid: Grid<Boolean>) = grid[this] && getSurroundingWithinGrid(grid).count { grid[it] } < 4
