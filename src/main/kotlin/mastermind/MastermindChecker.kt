package mastermind

fun check(attempt: String, solution: String): MastermindState {
    val attemptChecker = MastermindAttemptChecker(solution)
    return attemptChecker.process(attempt)
}

data class MastermindState(val placed: Int = 0, val misplaced: Int = 0) {
    fun addPlaced() = MastermindState(placed + 1, misplaced)
    fun addMisplaced() = MastermindState(placed, misplaced + 1)
}

enum class PlacementType { Placed, Misplaced, Absent }

class MastermindAttemptChecker(val solution: String) {
    fun process(attempt: String): MastermindState {
        return attempt.foldIndexed(MastermindState()) { index, mastermindState, char ->
            when (processChar(index, char)) {
                PlacementType.Placed -> mastermindState.addPlaced()
                PlacementType.Misplaced -> mastermindState.addMisplaced()
                PlacementType.Absent -> mastermindState
            }
        }
    }

    private fun processChar(index: Int, char: Char): PlacementType {
        val charPlacementType = when {
            isPlaced(index, char) -> PlacementType.Placed
            isPresent(char) -> PlacementType.Misplaced
            else -> PlacementType.Absent
        }
        removeCharFromCounts(char)
        return charPlacementType
    }

    private fun isPlaced(index: Int, char: Char) = solution[index] == char
    private fun isPresent(char: Char) = remainingCountsByChar.getOrElse(char) { 0 } > 0

    private val remainingCountsByChar: MutableMap<Char, Int> = mutableMapOf(
        *solution.groupBy { it }
            .map { (char, chars) -> char to chars.size }
            .toTypedArray()
    )

    private fun removeCharFromCounts(char: Char) {
        if (char !in remainingCountsByChar) return
        if (remainingCountsByChar[char] == 0) return

        remainingCountsByChar[char] = remainingCountsByChar.getOrElse(char) { 0 } - 1
    }
}