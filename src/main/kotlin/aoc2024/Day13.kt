package aoc2024

import utils.Coordinate
import utils.LongCoordinate
import utils.x
import utils.y
import java.io.File

// n * x1 + m * x2 = x3
// n * y1 + m * y2 = y3
// n * x1 + m * x1 * y2 / y1 = y3 * x1 / y1
// m * (x1 * y2 / y1 - x2) = y3 * x1 / y1 - x3
// m = (y3 * x1 / y1 - x3) / (x1 * y2 / y1 - x2)

fun main() {
    val machines = File("./src/main/resources/aoc2024/day13_input.txt").readText().split("\n\n").also(::println).map { machineText ->
        machineText.split("\n").map { line ->
            "[0-9]{1,5}".toRegex().findAll(line).toList().map {
                it.groupValues.first().toInt()
            }
        }
    }.map { (result1, result2, result3) ->
        Machine(result1.first() to result1.last(), result2.first() to result2.last(), result3.first().toLong() to result3.last().toLong())
    }.also(::println)

    fun Machine.calculateSolution() : Pair<Long, Long>? {
        val numerator = prize.y * A.x - prize.x * A.y
        val denominator = A.x * B.y - A.y * B.x
        val bPresses = (numerator/denominator).takeIf { numerator % denominator == 0L }
        val aPresses = bPresses?.let { prize.x - it * B.x }?.let { num -> (num / A.x).takeIf { num % A.x == 0L } }
        return aPresses?.let { it to bPresses}
    }

    fun Machine.calculatePrice() = calculateSolution()?.let { (a, b) -> a * 3 + b } ?: 0

    val answerPart1 = machines.sumOf { it.calculatePrice() }
    println("Answer part 1: $answerPart1")

    val machinesPart2 = machines.map { machine ->
        machine.copy(prize = (machine.prize.x + 10000000000000) to (machine.prize.y + 10000000000000))
    }
    val answerPart2 = machinesPart2.sumOf { it.calculatePrice() }
    println("Answer part 2: $answerPart2")
}

data class Machine(
    val A: Coordinate,
    val B: Coordinate,
    val prize: LongCoordinate
)

