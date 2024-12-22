package org.example

import java.io.File
import kotlin.time.measureTime

fun main() {
    val startingPrices = File("./src/main/resources/day22_input.txt").readLines().map { it.stripWindowsLineFeed().toInt() }
    val modulo = 16777216

    fun Long.mixAndPrune(otherNumberCalculator: (Long) -> Long) = (this xor otherNumberCalculator(this)) % modulo

    fun Long.calculatePrices(steps: Int) = generateSequence(this) { currentPrice ->
        currentPrice.mixAndPrune { it * 64 }.mixAndPrune { it / 32 }.mixAndPrune { it * 2048 }
    }.take(steps)

    measureTime {
        val answerPart1 = startingPrices.sumOf { price ->
            price.toLong().calculatePrices(2001).last()
        }
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }

    val changeSeqToPriceMap = mutableMapOf<List<Int>, Int>()

    measureTime {
        startingPrices.forEach { price ->
            val priceProgression = price.toLong().calculatePrices(2001).toList().map { (it % 10).toInt() }
            priceProgression.windowed(2).map { (a, b) -> b - a }.windowed(4).let { changeSeqSeq ->
                changeSeqSeq.toSet().forEach { changeSeq ->
                    val value = changeSeqSeq.indexOf(changeSeq).let { priceProgression[it + 4] }
                    changeSeqToPriceMap[changeSeq] = value + (changeSeqToPriceMap[changeSeq] ?: 0)
                }
            }
        }

        val answerPart2 = changeSeqToPriceMap.maxOf { it.value }
        println("Answer part 2: $answerPart2")
    }.also { println("Time taken: $it") }

}




