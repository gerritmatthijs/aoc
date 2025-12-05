package aoc2025

import java.io.File

typealias Range = Pair<Long, Long>
val Range.lowerBound
    get() = first
val Range.upperBound
    get() = second

fun main() {
    val (ranges, ids) = File("./src/main/resources/aoc2025/day5_input.txt").readText()
        .split("\n\n").let { (ranges, ids) ->
            ranges.split("\n").map { range ->
                range.split("-").map(String::toLong).let { it.first() to it.last()}
            } to ids.split("\n").map(String::toLong)
        }
    val answerPart1 = ids.count { id -> ranges.any { (lowerBound, upperBound) -> id in lowerBound..upperBound } }
    println("Answer part 1: $answerPart1")

    val disjointRanges = ranges.fold(listOf<Range>()) { acc, range ->
        range.deleteOverlap(acc)?.let { acc + it } ?: acc
    }
    val answerPart2 = disjointRanges.sumOf { (lowerBound, upperBound) -> upperBound - lowerBound + 1 }
    println("Answer part 2: $answerPart2")
}

fun Range.deleteOverlap(existingRanges: List<Range>) =
    existingRanges.fold(listOf(this)) { acc, range -> acc.flatMap { it.deleteOverlap(range) } }
        .takeIf { lowerBound <= upperBound }

fun Range.deleteOverlap(range: Range): List<Range> = when {
    lowerBound < range.lowerBound && upperBound in range.lowerBound..range.upperBound ->
        listOf(lowerBound to range.lowerBound - 1)
    upperBound > range.upperBound && lowerBound in range.lowerBound..range.upperBound ->
        listOf(range.upperBound + 1 to upperBound)
    lowerBound >= range.lowerBound && upperBound <= range.upperBound -> emptyList()
    lowerBound <= range.lowerBound && upperBound >= range.upperBound ->
        listOf(lowerBound to range.lowerBound - 1, range.upperBound + 1 to upperBound)
    else -> listOf(this)
}
