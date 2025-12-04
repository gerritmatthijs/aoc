package aoc2025

import utils.Coordinate
import utils.getSurroundingWithinGrid
import utils.toGrid
import java.io.File
import kotlin.time.measureTime

fun main() {
    val grid = File("./src/main/resources/aoc2025/day4_input.txt").readText().toGrid()

    fun Coordinate.isAccessible() = grid[this] == '@' && getSurroundingWithinGrid(grid).count { grid[it] == '@' } < 4

    measureTime {
        println("Answer part 1: ${grid.indices.count { it.isAccessible() }}")
    }.also { println("Time taken: $it") }
}

