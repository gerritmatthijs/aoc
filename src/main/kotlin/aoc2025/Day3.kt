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

    val answerPart2 = contents.sumOf { bank ->
        generateSequence("" to bank) { (chosenBatteries, remainingBank) ->
            val nextDigit = remainingBank.dropLast(11 - chosenBatteries.length).max()
            chosenBatteries + nextDigit to remainingBank.substringAfter(nextDigit)
        }.take(13).last().first.toLong()
    }

    println("Answer part 2: $answerPart2")

}