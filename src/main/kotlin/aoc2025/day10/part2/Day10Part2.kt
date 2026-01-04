package aoc2025.day10.part2

import utils.print
import kotlin.time.measureTime

fun solvePart2(input: List<List<String>>) {
    val machines = input.map { (_, buttons, requirements) ->
        val parsedRequirements = requirements.split(",").map(String::toInt)
        val parsedButtons = buttons.drop(1).dropLast(1).split(") (").map {
            it.split(",").map(String::toInt).toButton(parsedRequirements.size)
        }
        Machine(parsedButtons,parsedRequirements)
    }

    machines.mapIndexed { i, machine ->
        val nextIndex = machine.findNextIndex()
        val requirement = machine.requirements[nextIndex]
        val numberOfButtons = machine.buttons.indices.count { machine.buttons[it][nextIndex] }
        "Machine nr ${i+1}, requirement: $requirement, relevant buttons: $numberOfButtons, " +
                "number of options: ${calculateNumberOfOptions(requirement, numberOfButtons)}"
    }.print()

    val answerPart2 = machines.drop(34).forEachIndexed { index, machine ->
        measureTime {
            val minimumButtonPresses = machine.getAllSolutions().min()
            println("Machine nr ${index + 35}, minimum button presses: $minimumButtonPresses")
        }.also { println("Time taken: $it") }
//        minimumButtonPresses
    }
    println("Answer part 2: $answerPart2")


//    machines[0].let { machine ->
//        val nextIndex = machine.findNextIndex()
//        val relevantButtons = machine.buttons.indices.filter { machine.buttons[it][nextIndex] }
//        relevantButtons.getAllPossibleButtonCombinations(machine.requirements[nextIndex]).forEach { buttonPresses ->
//            println("Button presses: $buttonPresses, new situation: ${machine.getNewSituation(buttonPresses)}")
//        }
//    }
}

typealias Button = List<Boolean>

data class Machine(
    val buttons: List<Button>,
    val requirements: List<Int>,
)

fun List<Int>.toButton(length: Int) = (0..<length).map { this.contains(it) }

fun Machine.findNextIndex() =
    requirements.indices.minBy { index -> requirements[index] }
//    buttons.findAllRequirementIndicesWithLowestCoverage().minBy { index -> requirements[index] }

fun List<Button>.findAllRequirementIndicesWithLowestCoverage() = first().indices.minOf { index -> count { it[index] } }.let { minValue ->
    first().indices.filter { index -> count { it[index] } == minValue }
}

fun calculateNumberOfOptions(requirement: Int, numberOfButtons: Int): Long = when (numberOfButtons) {
    1 -> 1L
    else -> (0..requirement).sumOf { firstButtonPresses ->
        calculateNumberOfOptions(requirement - firstButtonPresses, numberOfButtons - 1)
    }
}

fun List<Int>.getAllPossibleButtonCombinations(requirement: Int): Sequence<Map<Int, Int>> = when (size) {
    0 -> emptySequence()
    1 -> sequenceOf(mapOf(first() to requirement))
    else -> (0..requirement).asSequence().flatMap { firstButtonsPresses ->
        this.drop(1).getAllPossibleButtonCombinations(requirement - firstButtonsPresses).map { it + mapOf(first() to firstButtonsPresses) }
    }
}

fun Machine.getNewSituation(buttonPresses: Map<Int, Int>): Machine {
    val joltageIncreases = buttonPresses.map { (buttonIndex, presses) ->
        buttons[buttonIndex].map { if (it) presses else 0 }
    }.reduce { acc, list -> acc.addValues(list) }
    val newRequirements = requirements.mapIndexed { index, value -> value - joltageIncreases[index] }
    val fulfilledRequirements = newRequirements.indices.filter { newRequirements[it] == 0 }
    val filteredRequirements = newRequirements.filterNot { it == 0 }
    val filteredButtons = buttons.filterNot { button -> button.indices.any { button[it] && it in fulfilledRequirements } }
    val newButtons = filteredButtons.map { button -> button.filterIndexed { index, _ -> index !in fulfilledRequirements } }
    return Machine(newButtons, filteredRequirements)
}

fun List<Int>.addValues(other: List<Int>): List<Int> {
    require(size == other.size)
    return mapIndexed { index, value -> value + other[index] }
}

fun Machine.getNextIterations(nextIndex: Int): Sequence<Machine> {
    val relevantButtons = buttons.indices.filter { buttons[it][nextIndex] }
    return relevantButtons.getAllPossibleButtonCombinations(requirements[nextIndex]).map { buttonPresses -> getNewSituation(buttonPresses) }
}

fun Machine.getAllSolutions(): Sequence<Int> = when  {
    requirements.isEmpty() -> sequenceOf(0)
    buttons.isEmpty() || !buttons.checkFullCoverage() -> emptySequence()
    buttons.size == 1 && requirements.toSet().size == 1 -> sequenceOf(requirements.first()) // shortcut for last step
    else -> {
        val nextIndex = findNextIndex()
        val requirement = requirements[nextIndex]
        getNextIterations(nextIndex).flatMap { nextMachine -> nextMachine.getAllSolutions().map { it + requirement } }
    }
}

fun List<Button>.checkFullCoverage() = first().indices.all { index -> any { it[index] } }