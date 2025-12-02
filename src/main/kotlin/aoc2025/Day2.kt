package aoc2025

import java.io.File
import kotlin.math.pow

fun main() {
    val contents = File("./src/main/resources/aoc2025/day2_input.txt").readText()
        .split(",").map { it.split("-") }
    val answerPart1 = contents.sumOf { (lowerBound, upperBound) ->
        (lowerBound.lowestInvalidIdHalf(2)..upperBound.highestInvalidId(2)).sumOf { it.repeat(2) }
    }
    println("Answer part 1: $answerPart1")

    val answerPart2 = contents.flatMap { (lowerBound, upperBound) ->
        (2..upperBound.length).filter { it.isPrime() }.flatMap { repeats ->
            (lowerBound.lowestInvalidIdHalf(repeats)..upperBound.highestInvalidId(repeats)).map { it.repeat(repeats) }
        }
    }.toSet().sum()
    println("Answer part 2: $answerPart2")
}

fun String.lowestInvalidIdHalf(repeats: Int) = if (length % repeats == 0)
    take(this.length / repeats).toLong().let { if (it.repeat(repeats) < this.toLong()) it + 1 else it }
else 10.0.pow(this.length / repeats).toLong()

fun String.highestInvalidId(repeats: Int) = if (length % repeats == 0)
    take(this.length / repeats).toLong().let { if (it.repeat(repeats) > this.toLong()) it - 1 else it }
else 10.0.pow(this.length / repeats).toLong() - 1

fun String.repeat(times: Int) = List(times) { this }.joinToString("")
fun Long.repeat(times: Int) = toString().repeat(times).toLong()
fun Int.isPrime() = (2..<this).none { this % it == 0 }