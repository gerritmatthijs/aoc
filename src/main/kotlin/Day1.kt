package org.example

import java.io.File
import kotlin.math.abs

fun main() {
    val contents = File("./src/main/resources/day1_part1_input.txt").readLines()
        .map { it.split("   ").map(String::toInt) }
    val part1Result = (contents.map { it.first() }.sorted() zip contents.map { it.last() }.sorted())
        .sumOf { (first, second) -> abs(first - second) }
    println(part1Result)

    val part2Result = contents.map { it.first() }.sumOf { leftItem ->
        leftItem * contents.count { it.last() == leftItem }
    }
    println(part2Result)
}