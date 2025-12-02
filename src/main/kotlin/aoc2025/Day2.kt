package aoc2025

import java.io.File
import kotlin.math.pow

fun main() {
    val contents = File("./src/main/resources/aoc2025/day2_input.txt").readText()
        .split(",").map { it.split("-") }
    val answerPart1 = contents.sumOf { (lowerBound, upperBound) ->
        (lowerBound.lowestInvalidIdHalf()..upperBound.highestInvalidId()).sumOf { it.repeat() }
    }
    println("Answer part 1: $answerPart1")
}

fun String.lowestInvalidIdHalf() = if (length % 2 == 0)
    take(this.length / 2).toLong().let { if (it.repeat() < this.toLong()) it + 1 else it }
else 10.0.pow(this.length / 2).toLong()

fun String.highestInvalidId() = if (length % 2 == 0)
    take(this.length / 2).toLong().let { if (it.repeat() > this.toLong()) it - 1 else it }
else 10.0.pow(this.length / 2).toLong() - 1

fun String.repeat() = this + this
fun Long.repeat() = toString().repeat().toLong()