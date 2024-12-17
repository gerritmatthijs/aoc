package org.example

import java.io.File
import kotlin.math.ceil
import kotlin.math.log
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    val instructions = listOf(2,4,1,3,7,5,0,3,4,3,1,5,5,5)
    val startInput = listOf(22571680L, 0L, 0L)
    val outputs = mutableListOf<Int>()

    fun combo(operand: Int, input: List<Long>) = when (operand) {
        4 -> input[0]
        5 -> input[1]
        6 -> input[2]
        else -> operand.toLong()
    }

    fun instructionZero(operand: Int, input: List<Long>) = listOf(input.first() / 2.0.pow(combo(operand, input).toInt()).toLong()) + input.drop(1)

    fun instructionOne(operand: Int, input: List<Long>) = listOf(input[0], input[1] xor operand.toLong(), input[2])

    fun instructionTwo(operand: Int, input: List<Long>) = listOf(input[0], combo(operand, input) % 8, input[2])

    fun instructionFour(input: List<Long>) = listOf(input[0], input[1] xor input[2], input[2])

    fun instructionFive(operand: Int, input: List<Long>) = input.also { outputs.add((combo(operand, input) % 8).toInt()) }

    fun instructionSix(operand: Int, input: List<Long>) = listOf(input[0], input.first() / 2.0.pow(combo(operand, input).toInt()).toLong(), input[2])

    fun instructionSeven(operand: Int, input: List<Long>) = input.dropLast(1) + (input.first() / 2.0.pow(combo(operand, input).toInt()).toLong())

    fun Pair<Int, Int>.convertToInstruction(): (List<Long>) -> List<Long> = when(first) {
        0 -> { input: List<Long> -> instructionZero(second, input) }
        1 -> { input: List<Long> -> instructionOne(second, input) }
        2 -> { input: List<Long> -> instructionTwo(second, input) }
        4 -> { input: List<Long> -> instructionFour(input) }
        5 -> { input: List<Long> -> instructionFive(second, input) }
        6 -> { input: List<Long> -> instructionSix(second, input) }
        7 -> { input: List<Long> -> instructionSeven(second, input) }
        else -> throw Exception("Shouldn't happen")
    }

    val parsedInstructions = instructions.chunked(2).map { (it.first() to it.last()).convertToInstruction() }

    val nrOfLoops = ceil(log(startInput[0].toDouble(), 8.0)).toInt()
    (1..nrOfLoops).fold(startInput) { inputBeginLoop, _ ->
        parsedInstructions.fold(inputBeginLoop) { input, instruction -> instruction(input) }
    }
    val answerPart1 = outputs.joinToString(",")
    println("Answer part 1: $answerPart1")
}




