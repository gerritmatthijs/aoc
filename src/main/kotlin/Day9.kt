package org.example

import java.io.File
import kotlin.time.measureTime

fun main() {
    val input = File("./src/main/resources/day9_input.txt").readText().toList().map { it.toString().toInt() }

    data class CompactifyInput(
        val diskMap: List<Int>,
        val currentLeftID: Int,
        val currentRightID: Int,
        val leftIsBlockFile: Boolean
    )

    fun List<Int>.calculateCheckSum(currentIndex: Int) = this.indices.sumOf { i -> (i + currentIndex) * this[i] }

    tailrec fun compactifyAndUpdateCheckSum(diskMap: List<Int>, currentLeftID: Int, currentRightID: Int, leftIsBlockFile: Boolean, accumulatedResult: Long, currentIndex: Int): Long{
        val (newResultPiece, nextInput) = when {
            diskMap.isEmpty() -> return accumulatedResult
            leftIsBlockFile -> List(diskMap.first()) { currentLeftID } to CompactifyInput(diskMap.drop(1), currentLeftID + 1, currentRightID, false)
            diskMap.first() > diskMap.last() -> List(diskMap.last()) { currentRightID } to CompactifyInput(
                listOf(diskMap.first() - diskMap.last()) + diskMap.drop(1).dropLast(2),
                currentLeftID, currentRightID - 1,  false
            )
            diskMap.first() < diskMap.last() -> List(diskMap.first()) { currentRightID } to CompactifyInput(
                diskMap.drop(1).dropLast(1) + listOf(diskMap.last() - diskMap.first()),
                currentLeftID, currentRightID, true
            )
            diskMap.first() == diskMap.last() -> List(diskMap.first()) { currentRightID } to CompactifyInput(
                diskMap.drop(1).dropLast(2), currentLeftID, currentRightID - 1,  true
            )
            else -> throw Exception("Won't happen")
        }
        return compactifyAndUpdateCheckSum(nextInput.diskMap,
            nextInput.currentLeftID,
            nextInput.currentRightID,
            nextInput.leftIsBlockFile,
            accumulatedResult + newResultPiece.calculateCheckSum(currentIndex),
            currentIndex + newResultPiece.size
        )
    }

    measureTime {
        val answerPart1 = compactifyAndUpdateCheckSum(input, 0, (input.size + 1)/2, true, 0L, 0)
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }

}

