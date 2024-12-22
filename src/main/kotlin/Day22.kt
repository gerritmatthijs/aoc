package org.example

import java.io.File
import kotlin.time.measureTime

fun main() {
    val startingPrices = File("./src/main/resources/day22_input.txt").readLines().map { it.stripWindowsLineFeed().toInt() }
    val modulo = 16777216

    fun Long.mixAndPrune(otherNumberCalculator: (Long) -> Long) = (this xor otherNumberCalculator(this)) % modulo

    fun Long.calculatePriceAfterSteps(steps: Int) = generateSequence(this) {currentPrice ->
        currentPrice.mixAndPrune { it * 64 }.mixAndPrune { it / 32 }.mixAndPrune { it * 2048 }
    }.take(steps).last()

    measureTime {
        val answerPart1 = startingPrices.sumOf { price ->
            price.toLong().calculatePriceAfterSteps(2001)
        }
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }
}




