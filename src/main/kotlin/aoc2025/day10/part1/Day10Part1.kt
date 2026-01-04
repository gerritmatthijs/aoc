package aoc2025.day10.part1

import utils.getSubsetsOfSize

fun solvePart1(input: List<List<String>>) {
    val machines = input.map { (lights, buttons) ->
        val parsedButtons = buttons.drop(1).dropLast(1).split(") (").map {
            it.split(",").map(String::toInt).toButton()
        }
        Machine(lights.toLightsRequired(), parsedButtons)
    }
    val answerPart1 = machines.sumOf { it.findLowestNumberOfButtonPresses() }
    println("Answer part 1: $answerPart1")
}

typealias Button = Int

data class Machine(
    val lightsRequired: Button,
    val buttons: List<Button>,
) {
    fun findLowestNumberOfButtonPresses() = generateSequence(1) { it + 1 }.first { existsPossibilityWithNPresses(it) }

    fun existsPossibilityWithNPresses(n: Int) = buttons.getSubsetsOfSize(n).any { it.combine() == lightsRequired }
}

fun List<Int>.toButton(): Button = sumOf { 1 shl it }
fun String.toLightsRequired(): Button = indices.filter { this[it] == '#' }.toButton()

fun List<Button>.combine() = reduce { acc, button -> acc xor button }