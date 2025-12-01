package aoc2024

import utils.findIndexOfSublist
import java.io.File
import kotlin.time.measureTime

fun main() {
    val input = File("./src/main/resources/aoc2024/day9_input.txt").readText().toList().map { it.toString().toInt() }

    data class CompactifyInput(
        val diskMap: List<Int>,
        val currentLeftID: Int,
        val currentRightID: Int,
        val leftIsBlockFile: Boolean
    )

    tailrec fun compactify(diskMap: List<Int>, currentLeftID: Int, currentRightID: Int, leftIsBlockFile: Boolean, accumulatedResult: List<Int>): List<Int> {
        val (newResultPiece, nextInput) = when {
            diskMap.isEmpty() -> return accumulatedResult
            leftIsBlockFile -> List(diskMap.first()) { currentLeftID } to CompactifyInput(diskMap.drop(1), currentLeftID + 1, currentRightID, false)
            diskMap.first() > diskMap.last() -> List(diskMap.last()) { currentRightID } to CompactifyInput(
                listOf(diskMap.first() - diskMap.last()) + diskMap.drop(1).dropLast(2),
                currentLeftID, currentRightID - 1,  false
            )
            diskMap.first() < diskMap.last() -> List(diskMap.first()) { currentRightID } to CompactifyInput(
                diskMap.drop(1).dropLast(1) + (diskMap.last() - diskMap.first()),
                currentLeftID, currentRightID, true
            )
            diskMap.first() == diskMap.last() -> List(diskMap.first()) { currentRightID } to CompactifyInput(
                diskMap.drop(1).dropLast(2), currentLeftID, currentRightID - 1,  true
            )
            else -> throw Exception("Won't happen")
        }
        return compactify(nextInput.diskMap, nextInput.currentLeftID, nextInput.currentRightID, nextInput.leftIsBlockFile, accumulatedResult + newResultPiece)
    }

    fun List<Int>.calculateCheckSum() = this.indices.sumOf { i -> i.toLong() * this[i] }

    measureTime {
        val answerPart1 = compactify(input, 0, (input.size - 1)/2, true, emptyList()).calculateCheckSum()
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }

    val extendedInput = input.flatMapIndexed { index: Int, size: Int ->
        if (index % 2 == 0) List(size) { index / 2 }
        else List(size) { -1 }
    }

    tailrec fun List<Int>.compactify(currentValue: Int): List<Int> {
        val endIndex = indexOfLast { it == currentValue }
        val startIndex = subList(0, endIndex).indexOfLast { it != currentValue } + 1
        val fileSize = endIndex - startIndex + 1
        val changedList = subList(0, startIndex).findIndexOfSublist(List(fileSize) { -1 })?.let { destinationStart ->
            (subList(0, destinationStart) + List(fileSize) { currentValue } + subList(destinationStart + fileSize, startIndex) +
                    List(fileSize) { -1 } + subList(endIndex + 1, size))
        } ?: this
        return if (currentValue == 1) changedList else changedList.compactify(currentValue - 1)
    }

    measureTime {
        val compactedInput = extendedInput.compactify(extendedInput.last()).also(::println)
        val answerPart2 = compactedInput.mapIndexed { index, value ->
            if (value == -1) 0L else index.toLong() * value
        }.sum()
        println("Index of 5421: ${compactedInput.indexOf(5421)}")
        println("Original index of 5421: ${extendedInput.indexOf(5421)}")
        println("Answer part 2: $answerPart2")
    }.also { println("Time taken: $it") }
}

