package org.example

import java.io.File
import kotlin.time.measureTime

fun main() {
    val (availableTowels, patterns) = File("./src/main/resources/day19_input.txt").readText().split("\n\n").let { (towels, patterns) ->
        towels.split(", ") to patterns.split("\n")
    }

    fun String.existsTowelArrangement(): Boolean = if (isEmpty()) true else
        availableTowels.filter { towel -> startsWith(towel) }.any { towel ->
            drop(towel.length).existsTowelArrangement()
        }

    measureTime {
        val answerPart1 = patterns.count { pattern -> pattern.existsTowelArrangement() }
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }

    val cache = mutableMapOf<String, Long>()

    fun String.allTowelArrangements(): Long = cache[this] ?:
        (if (isEmpty()) 1L
        else availableTowels.filter { towel -> startsWith(towel) }.sumOf { towel ->
            drop(towel.length).allTowelArrangements()
        }).also { cache[this] = it }

    measureTime {
        val answerPart2 = patterns.sumOf { pattern -> pattern.allTowelArrangements() }
        println("Answer part 2: $answerPart2")
    }.also { println("Time taken: $it") }
}




