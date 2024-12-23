package aoc2024

import utils.stripWindowsLineFeed
import java.io.File

fun main() {
    val codes = File("./src/main/resources/day21_input.txt").readLines().map { it.stripWindowsLineFeed() }
}




