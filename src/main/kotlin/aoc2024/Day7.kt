package aoc2024

import java.io.File

fun main() {
    val equations = File("./src/main/resources/day7_input.txt").readLines()
        .map { it.split(": ") }.map { (total, rest) -> total.toLong() to rest.split(' ').map(String::toInt) }

    fun operate(x: Long, y: Int, operation: Int) = when (operation) {
        0 -> x + y
        1 -> x * y
        2 -> (x.toString() + y.toString()).toLong()
        else -> throw IllegalArgumentException()
    }

    fun List<Int>.applyOperations(operations: List<Int>, limit: Long) = (this.subList(1, this.size) zip operations).fold(this.first().toLong()) {
        x, (y, operation) -> if (x == 0L) 0 else operate(x, y, operation).let { result ->
            if (result > limit) 0 else result
        }
    }

    fun Int.generatePossibleOperationSequences(): List<List<Int>> {
        fun List<List<Int>>.addOption(remaining: Int): List<List<Int>> =
            if (remaining == 0) this else
                flatMap { listOf(it + listOf(0), it + listOf(1)) }.addOption(remaining - 1)
        return listOf(emptyList<Int>()).addOption(this)
    }

    fun Pair<Long, List<Int>>.checkPossible() = (this.second.size - 1).generatePossibleOperationSequences()
        .any { operations ->
            (this.second.applyOperations(operations, this.first) == this.first).also {
                if (it) println("Solution found for equation $this: $operations")
            }
        }

    val answerPart1 = equations.filter { equation -> equation.checkPossible() }.sumOf { it.first }
    println("Answer part 1: $answerPart1")

    fun Int.generatePossibleOperationSequencesPartTwo(): List<List<Int>> {
        fun List<List<Int>>.addOption(remaining: Int): List<List<Int>> =
            if (remaining == 0) this else
                flatMap { listOf(it + listOf(0), it + listOf(1), it + listOf(2)) }.addOption(remaining - 1)
        return listOf(emptyList<Int>()).addOption(this)
    }

    fun Pair<Long, List<Int>>.checkPossiblePartTwo() = (this.second.size - 1).generatePossibleOperationSequencesPartTwo()
        .any { operations ->
            (this.second.applyOperations(operations, this.first) == this.first).also {
                if (it) println("Solution found for equation $this: $operations")
            }
        }

    val answerPart2 = equations.filter { equation -> equation.checkPossiblePartTwo() }.sumOf { it.first }
    println("Answer part 2: $answerPart2")
}

