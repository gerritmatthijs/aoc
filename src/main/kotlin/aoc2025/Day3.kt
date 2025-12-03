package aoc2025

import java.io.File

fun main() {
    val contents = File("./src/main/resources/aoc2025/day3_input.txt").readLines()
    val answerPart1 = contents.sumOf { bank ->
        val firstDigit = bank.dropLast(1).max()
        val secondDigit = bank.substringAfter(firstDigit).max()
        "$firstDigit$secondDigit".toInt()
    }
    println("Answer part 1: $answerPart1")
}