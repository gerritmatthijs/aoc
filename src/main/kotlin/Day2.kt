package org.example

import java.io.File
import kotlin.math.abs

fun main() {
    val contents = File("./src/main/resources/day2_input.txt").readLines()
        .map { it.split(" ").map(String::toInt) }
    val answerPart1 = contents.count { it.checkSafe() }
    println(answerPart1)

    val answerPart2 = contents.count { report ->
        report.indices.any { index ->
            report.toMutableList().apply { removeAt(index) }.checkSafe()
        }
    }
    println(answerPart2)
}

fun List<Int>.checkSafe(): Boolean =
    windowed(2).all { (a, b) ->
        (b - a) * (this[1] - this[0]) > 0 && abs(a - b) <= 3
    }