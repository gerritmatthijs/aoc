package org.example

import java.io.File

fun main() {
    val initialStones = File("./src/main/resources/day11_input.txt").readText().split(" ")
//    val initialStones = listOf("125", "17")

    fun String.removeZeroes(): String = if (this[0] == '0' && length > 1) drop(1).removeZeroes() else this

    fun List<String>.blink() = flatMap { stone ->
        when {
            stone == "0" -> listOf("1")
            stone.length % 2 == 0 -> listOf(stone.dropLast(stone.length/2).removeZeroes(), stone.drop(stone.length/2).removeZeroes())
            else -> listOf((stone.toBigInteger().multiply( 2024.toBigInteger())).toString())
        }
    }

    val answerPart1 = initialStones.indices.sumOf { index ->
        (1..25).fold(listOf(initialStones[index])) { stones, _ -> stones.blink() }.size
    }
    println("Answer part 1: $answerPart1")

}

