package aoc2024

import utils.*
import java.io.File
import kotlin.time.measureTime

fun main() {
    val (grid, instructions) = File("./src/main/resources/aoc2024/day15_input.txt").readText().stripWindowsLineFeed()
        .split("\n\n").let { (part1, part2) ->
            part1.toGrid() to part2.replace("\n", "")
        }

    fun Grid<Char>.executeInstruction(instruction: Char, currentPosition: Coordinate) = when (instruction) {
        '^' -> move(currentPosition, Direction.UP)
        '>' -> move(currentPosition, Direction.RIGHT)
        'v' -> move(currentPosition, Direction.DOWN)
        '<' -> move(currentPosition, Direction.LEFT)
        else -> throw Exception("Unrecognised instruction $instruction")
    } ?: this

//    measureTime {
//        val finalGrid = instructions.fold(grid) { currentGrid, instruction ->
//            currentGrid.executeInstruction(instruction, currentGrid.indexOf('@')!!)
//        }
//
//        val answerPart1 = finalGrid.indices.sumOf { (i, j) ->
//            if (finalGrid[i, j] == 'O') i * 100 + j else 0
//        }
//        println("Answer part 1: $answerPart1")
//    }.also { println("Time taken for part 1: $it") }


    val widenedGrid = grid.flatMap {
        when (it) {
            '.' -> ".."
            '#' -> "##"
            'O' -> "[]"
            '@' -> "@."
            else -> throw Exception("Unknown input $it")
        }.toList()
    }


    measureTime {
        val finalGridPart2 = instructions.fold(widenedGrid) { currentGrid, instruction ->
            currentGrid.executeInstruction(instruction, currentGrid.indexOf('@')!!)
        }

        val answerPart2 = finalGridPart2.indices.sumOf { (i, j) ->
            if (finalGridPart2[i, j] == '[') i * 100 + j else 0
        }
        println("Answer part 2: $answerPart2")
    }.also { println("Time taken for part 2: $it") }
}

fun Grid<Char>.moveWideBlock(from: Coordinate, to: Coordinate, direction: Direction, isLeft: Boolean): Grid<Char>? =
    if (direction in listOf(Direction.LEFT, Direction.RIGHT)) move(to, direction)?.switch(from, to)
    else move(to, direction)?.switch(from, to)?.let {intermediateGrid ->
        val otherHalf = if (isLeft) to.getAdjacent(Direction.RIGHT) else to.getAdjacent(Direction.LEFT)
        intermediateGrid.move(otherHalf, direction)
    }

fun Grid<Char>.move(from: Coordinate, direction: Direction): Grid<Char>? =
    from.getAdjacent(direction).let { to ->
        when (this[to]) {
            '.' -> switch(from, to)
            '#' -> null
            'O' -> move(to, direction)?.switch(from, to)
            '[' -> moveWideBlock(from, to, direction, true)
            ']' -> moveWideBlock(from, to, direction, false)
            else -> throw Exception("Shouldn't happen")
        }
    }


