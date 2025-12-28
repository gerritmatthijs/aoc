package aoc2025

import utils.getSubsetsOfSize
import java.io.File


fun main() {
    val machines = File("./src/main/resources/aoc2025/day10_input.txt").readLines().map { line ->
        Regex("\\[([.#]+)] (\\(.*\\)) \\{(.*)}").matchEntire(line)!!.groupValues.drop(1)
    }.map { (lights, buttons, requirements) ->
        val parsedButtons = buttons.drop(1).dropLast(1).split(") (").map {
            it.split(",").map(String::toInt).toButton()
        }
        Machine(lights.toLightsRequired(), parsedButtons, requirements.split(",").map(String::toInt))
    }
    val answerPart1 = machines.sumOf { it.findLowestNumberOfButtonPresses() }
    println("Answer part 1: $answerPart1")
}

typealias Button = Int

data class Machine(
    val lightsRequired: Button,
    val buttons: List<Button>,
    val requirements: List<Int>
) {
    fun findLowestNumberOfButtonPresses() = generateSequence(1) { it + 1 }.first { existsPossibilityWithNPresses(it) }

    fun existsPossibilityWithNPresses(n: Int) = buttons.getSubsetsOfSize(n).any { it.combine() == lightsRequired }
}

fun List<Int>.toButton(): Button = sumOf { 1 shl it }
fun String.toLightsRequired(): Button = indices.filter { this[it] == '#' }.toButton()

fun List<Button>.combine() = reduce { acc, button -> acc xor button }