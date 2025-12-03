package aoc2025

import java.io.File

typealias Bank = String

fun main() {
    val contents: List<Bank> = File("./src/main/resources/aoc2025/day3_input.txt").readLines()
    println("Answer part 1: ${contents.sumOf { it.getMaxJoltage(2) }}")
    println("Answer part 2: ${contents.sumOf { it.getMaxJoltage(12) }}")
}

fun Bank.getMaxJoltage(length: Int) =
    generateSequence("" to this) { (chosenBatteries, remainingBank) ->
        remainingBank.dropLast(length - 1 - chosenBatteries.length).max().let { nextBattery ->
            chosenBatteries + nextBattery to remainingBank.substringAfter(nextBattery)
        }
    }.take(length + 1).last().first.toLong()