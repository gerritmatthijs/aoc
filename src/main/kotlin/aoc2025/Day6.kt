package aoc2025

import utils.toGrid
import utils.transpose
import java.io.File

fun main() {
    val problemsPart1 = File("./src/main/resources/aoc2025/day6_input.txt").readLines()
        .map { it.trim().split(Regex("\\s+")) }.toGrid().transpose()
        .map { problemLine ->
            Problem(
                problemLine.dropLast(1).map(String::toLong),
                problemLine.last().single().getOperation()
            )
        }
    println("Answer part 1: ${problemsPart1.sumOf { it.solve() }}")

    val (numberLines, operations) = File("./src/main/resources/aoc2025/day6_input.txt").readLines().let {
        it.dropLast(1) to it.last().split(Regex("\\s+")).map(CharSequence::single)
    }
    val problemsNumberParts = numberLines.transpose().joinToString("\n").split(Regex("\n\\s+\n"))
        .map { problemNumbers ->
            problemNumbers.split("\n").map { it.trim().toLong() }
        }
    val problems = (problemsNumberParts zip operations).map { (numbers, operation) ->
        Problem(numbers, operation.getOperation())
    }
    println("Answer part 2: ${problems.sumOf { it.solve() }}")
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