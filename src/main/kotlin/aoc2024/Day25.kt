package aoc2024

import utils.stripWindowsLineFeed
import utils.toGrid
import java.io.File


fun main() {
    val lockSize = 5

    fun String.toKey() = toGrid().transpose().map { it.indexOf('.') - 1 }
    fun String.toLock() = toGrid().transpose().map { 6 - it.indexOf('#') }

    val (keys, locks) = File("./src/main/resources/aoc2024/day25_input.txt").readText().stripWindowsLineFeed()
        .split("\n\n").groupBy { it.first() }.let {
            it['#']!!.map { keyString -> keyString.toKey() } to
                    it['.']!!.map { lockString -> lockString.toLock() }
        }

    fun List<Int>.fitsIn(lock: List<Int>) = indices.all { index -> this[index] + lock[index] <= lockSize }

    val answerPart1 = keys.sumOf { key ->
        locks.count { lock -> key.fitsIn(lock) }
    }
    println("Answer part 1: $answerPart1")

}