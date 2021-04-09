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
        }
    }

    private fun isPlaced(index: Int, item: Char): Boolean {
        return solution[index] == item
    }

    private fun addPlaced(item: Char) {
        placed++
        removeFromItemsByType(item)
    }

    private fun removeFromItemsByType(item: Char) {
        itemsByType[item] = max(0, itemsByType.getOrDefault(item, 0) - 1)
    }

    private val itemsByType: MutableMap<Char, Int> = computeItemsByType(solution)
    private fun computeItemsByType(solution: String): MutableMap<Char, Int> {
        val itemsByType = mutableMapOf<Char, Int>()
        for (ch in solution) {
            itemsByType[ch] = itemsByType.getOrDefault(ch, 0) + 1
        }
        return itemsByType
    }
}