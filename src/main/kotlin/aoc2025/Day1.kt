package aoc2025

import java.io.File
import kotlin.math.abs

fun main() {
    val contents = File("./src/main/resources/aoc2025/day1_input.txt").readLines()
        .map { it.replace("R", "").replace("L", "-").toInt() }

    fun calculateAnswer(countingMethod: (Int, Int) -> Int) =
        contents.fold(0 to 50) { (count, lastPosition), newNumber ->
            (count + countingMethod(lastPosition, newNumber)) to (lastPosition + newNumber).mod()
        }.first

    val answerPart1 = calculateAnswer { lastPosition, newNumber ->
        if ((lastPosition + newNumber) % 100 == 0) 1 else 0
    }
    println("answer part 1: $answerPart1")

    val answerPart2 = calculateAnswer { lastPosition, newNumber ->
        val (hundreds, remainder) = (abs(newNumber) / 100) to newNumber % 100
        hundreds + if (lastPosition == 0 || lastPosition + remainder in 1..99) 0 else 1
    }
    println("answer part 2: $answerPart2")
}

fun Int.mod() = (this % 100).let { if (it < 0) it + 100 else it }