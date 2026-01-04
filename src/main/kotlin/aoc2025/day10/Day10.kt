package aoc2025.day10

import aoc2025.day10.part1.solvePart1
import aoc2025.day10.part2.solvePart2
import java.io.File


fun main() {
    val input = File("./src/main/resources/aoc2025/day10_input.txt").readLines().map { line ->
        Regex("\\[([.#]+)] (\\(.*\\)) \\{(.*)}").matchEntire(line)!!.groupValues.drop(1)
    }
    solvePart1(input)
    solvePart2(input)
}
