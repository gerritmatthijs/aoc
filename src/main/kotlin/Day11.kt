package org.example

import java.io.File
import kotlin.time.measureTime

fun main() {
    val initialStones = File("./src/main/resources/day11_input.txt").readText().split(" ")
//    val initialStones = listOf("125", "17")

    fun String.removeZeroes(): String = if (this[0] == '0' && length > 1) drop(1).removeZeroes() else this

    fun List<String>.blink() = flatMap { stone ->
        when {
            stone == "0" -> listOf("1")
            stone.length % 2 == 0 -> listOf(stone.dropLast(stone.length/2).removeZeroes(), stone.drop(stone.length/2).removeZeroes())
            else -> listOf(stone.toBigInteger().multiply( 2024.toBigInteger()).toString())
        }
    }

    measureTime {
        val answerPart1 =
            (1..25).fold(initialStones) { stones, _ ->
                stones.blink()
            }.size
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken for part 1: $it") }

    val cache = mutableMapOf<Pair<String, Int>, Long>()

    fun String.calculateStonesAfter(numberOfBlinks: Int): Long = cache[this to numberOfBlinks] ?:
        when {
            numberOfBlinks == 0 -> 1
            this == "0" -> "1".calculateStonesAfter(numberOfBlinks - 1)
            length % 2 == 0 -> dropLast(length/2).removeZeroes().calculateStonesAfter(numberOfBlinks - 1) +
                    drop(length/2).removeZeroes().calculateStonesAfter(numberOfBlinks - 1)
            else -> toBigInteger().multiply(2024.toBigInteger()).toString().calculateStonesAfter(numberOfBlinks - 1)
        }.also { cache[this to numberOfBlinks] = it }

    measureTime {
        val answerPart1Method2 = initialStones.sumOf { stone -> stone.calculateStonesAfter(75) }
        println("Answer part 1: $answerPart1Method2")
    }.also { println("Time taken for part 2: $it") }
}

