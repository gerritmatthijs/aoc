package aoc2025

import utils.toGrid
import java.io.File

fun main() {
    val content = File("./src/main/resources/aoc2025/day6_input.txt").readLines()
        .map { it.trim().split(Regex("\\s+")) }.toGrid().transpose()
        .map { problemLine ->
            Problem(
                problemLine.dropLast(1).map(String::toLong),
                problemLine.last().single().getOperation()
            )
        }
    println("Answer part 1: ${content.sumOf { it.solve() }}")
}

fun Char.getOperation(): (Long, Long) -> Long = when (this) {
    '*' -> Long::times
    '+' -> Long::plus
    else -> throw Exception("Unknown operation: $this")
}

data class Problem(
    val numbers: List<Long>,
    val operation: (Long, Long) -> Long
) {
    fun solve() = numbers.reduce(operation)
}