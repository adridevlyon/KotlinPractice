package mastermind

import kotlin.math.max

class MastermindChecker {
    fun check(attempt: String, solution: String): MastermindState {
        val mastermindState = MastermindState(solution = solution)
        for ((index, item) in attempt.withIndex()) {
            mastermindState.process(index, item)
        }
        return mastermindState
    }
}

class MastermindState(val solution: String, var placed: Int = 0, var present: Int = 0) {
    fun process(index: Int, item: Char) {
        if (isPlaced(index, item)) {
            addPlaced(item)
        } else if (isPresent(item)) {
            addPresent(item)
        }
    }

    private fun isPlaced(index: Int, item: Char): Boolean {
        return solution[index] == item
    }

    private fun addPlaced(item: Char) {
        placed++
        removeFromItemsByType(item)
    }

    private fun isPresent(item: Char): Boolean {
        return itemsByType.getOrDefault(item, 0) > 0
    }

    private fun addPresent(item: Char) {
        present++
        removeFromItemsByType(item)
    }

    private fun removeFromItemsByType(item: Char) {
        itemsByType[item] = max(0, itemsByType.getOrDefault(item, 0) - 1)
    }

    private val itemsByType: MutableMap<Char, Int> = computeItemsByType(solution)
    private fun computeItemsByType(solution: String): MutableMap<Char, Int> {
        return mutableMapOf(
            *solution.groupBy { it }
                .map { (char, chars) -> char to chars.size }
                .toTypedArray()
        )
    }
}