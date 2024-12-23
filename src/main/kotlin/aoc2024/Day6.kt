package aoc2024

import java.io.File
import kotlin.time.measureTime

fun main() {
    val grid = File("./src/main/resources/day6_input.txt").readLines().map { it.toList() }

    fun List<List<Char>>.print() = onEach { println(it) }

    fun List<List<Char>>.rotateLeft() = this.indices.map { i ->
        this[i].indices.map { j -> this[j][grid.size-1-i] }
    }

    fun List<List<Char>>.moveLeft(row: Int, beginCol: Int, endCol: Int) = toMutableList().also {
        it[row] = it[row].subList(0, endCol) + listOf(if (endCol == 0) 'X' else '^') + List(beginCol - endCol) { 'X' } + it[row].subList(beginCol + 1, grid.size)
    }

    fun List<List<Char>>.findGuard() = indexOfFirst { it.contains('^') }.takeIf { it >= 0 }?.let { row ->
        row to this[row].indexOf('^')
    }

    fun List<List<Char>>.play(counter: Int): Pair<List<List<Char>>, Int> = findGuard()?.let { (row, beginCol) ->
        val endCol = this[row].subList(0, beginCol).indexOfLast { it == '#' } + 1
        moveLeft(row, beginCol, endCol).rotateLeft().play(counter + 1)
    } ?: (this to counter)

    val gridWithPath = grid.rotateLeft().play(1).let { result ->
        (1..4-(result.second % 4)).fold(result.first) { g, _ -> g.rotateLeft() }
    }

    val answerPart1 = gridWithPath.print().sumOf { row -> row.count { it == 'X' } }
    println("Answer part 1: $answerPart1")

    data class Situation(
        val orientation: Int,
        val row: Int,
        val col: Int,
    )

    fun List<List<Char>>.containsLoop(memory: List<Situation>): Boolean = findGuard()?.let { (row, beginCol) ->
        val orientation = if (memory.isEmpty()) 0 else (memory.last().orientation + 1) % 4
        val currentSituation = Situation(orientation, row, beginCol)
        if (currentSituation in memory) true
        else {
            val endCol = this[row].subList(0, beginCol).indexOfLast { it == '#' } + 1
            moveLeft(row, beginCol, endCol).rotateLeft().containsLoop(memory + listOf(currentSituation))
        }
    } ?: false

    fun addObstacle(row: Int, col: Int): List<List<Char>> = grid.toMutableList().also {
        it[row] = it[row].toMutableList().also { row -> row[col] = '#' }
    }

    measureTime {
        val answerPart2 = grid.indices.sumOf { row ->
            grid[row].indices.count { col ->
                (gridWithPath[row][col] == 'X' && grid[row][col] != '^' && addObstacle(row, col).containsLoop(emptyList())).also {
                    if (it) println("Loop found for position $row, $col")
                }
            }
        }
        println("Answer part 2: $answerPart2")
    }.also { println("Time taken: $it") }

}

