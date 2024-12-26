package aoc2024

import utils.convertBinaryToLong
import utils.stripWindowsLineFeed
import java.io.File
import java.lang.Exception
import kotlin.time.measureTime

// Exchanged:
// - z18 and dhq
// - z22 and pdg
// - z27 and jcp
// - kfp and hbs

fun main() {
    val (startingActiveWires, gates) = File("./src/main/resources/day24_input.txt").readText().stripWindowsLineFeed()
        .split("\n\n").let {
            (firstPart, secondPart) -> firstPart.toActiveWires() to
                secondPart.split("\n").map { it.toGate() }
        }

    fun Map<String, Boolean>.tryActivate(gate: Gate): Map<String, Boolean> =
        this[gate.inputs.first]?.let { firstValue ->
            this[gate.inputs.second]?.let { secondValue -> firstValue to secondValue }
        }?.let { (firstValue, secondValue) ->
            if (this[gate.output] == null) {
                when (gate.type) {
                    "AND" -> firstValue && secondValue
                    "OR" -> firstValue || secondValue
                    "XOR" -> firstValue xor secondValue
                    else -> throw Exception("Shouldn't happen")
                }.let { outputValue -> this + (gate.output to outputValue)}
            } else this
        } ?: this

    fun Map<String, Boolean>.tryActivateAll(): Map<String, Boolean> = gates.fold(this) { activeWires, gate ->
        activeWires.tryActivate(gate)
    }

    var counter = 0

    val finalWireStates = generateSequence(startingActiveWires) { it.tryActivateAll() }.takeWhile {
        it.size < startingActiveWires.size + gates.size
    }.last().tryActivateAll()

    val bits = finalWireStates.filterKeys { it.contains("z[0-9]{1,2}".toRegex()) }.map { (key, value) -> key.drop(1).toInt() to value }
        .sortedBy { it.first }.map { it.second }

    measureTime {
        val answerPart1 = bits.convertBinaryToLong()
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }

    val (_, correctedGates) = File("./src/main/resources/day24_corrected_input.txt").readText().stripWindowsLineFeed()
        .split("\n\n").let {
                (firstPart, secondPart) -> firstPart.toActiveWires() to
                secondPart.split("\n").map { it.toGate() }
        }

    fun findIncomingGate(output: String) = correctedGates.find { it.output == output }

    fun List<Gate>.checkGates(index: Int): List<Gate> {
        when {
            index == 0-> setOf("XOR")
            index % 2 == 1 -> setOf("XOR", "OR")
            else -> setOf("AND")
        }.let { expectedSet ->
            if (expectedSet != this.map { it.type }.toSet())
                println("Wrong gate found in gate list: $this")
        }
        return this
    }

    fun getPrecedingGates(gates: List<Gate>): List<List<Gate>> =
        listOf(gates) + (gates.flatMap { gate ->
            listOfNotNull(
                findIncomingGate(gate.inputs.first),
                findIncomingGate(gate.inputs.second),
            )
        }.takeUnless { it.isEmpty() }?.let { getPrecedingGates(it) } ?: emptyList())

    (listOf("z01","z02", "z03", "z04", "z05", "z06", "z07", "z08", "z09") + (10..44).map { "z$it" }).map {
        val precedingGates = getPrecedingGates(listOf(findIncomingGate(it)!!))
        precedingGates.dropLast(1).mapIndexed { index, gate ->
            gate.checkGates(index)
        }
        println("Size for output $it: ${getPrecedingGates(listOf(findIncomingGate(it)!!)).size}")
    }
}



fun String.toActiveWires() = split("\n").associate {
    it.split(": ").let { (name, value) -> name to (value == "1") }
}

data class Gate(
    val inputs: Pair<String, String>,
    val output: String,
    val type: String,
)

fun String.toGate() = "([a-z0-9]{3}) ([A-Z]{2,3}) ([a-z0-9]{3}) -> ([a-z0-9]{3})".toRegex().find(this)!!.groupValues
    .let { (_, firstInput, type, secondInput, output) -> Gate((firstInput to secondInput).sorted(), output, type) }

fun Pair<String, String>.sorted() = listOf(first, second).sorted().let { it.first() to it.last() }