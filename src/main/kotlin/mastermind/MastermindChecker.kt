package mastermind

fun check(attempt: String, solution: String): MastermindState {
    val attemptChecker = MastermindAttemptChecker(solution)
    return attemptChecker.process(attempt)
}

data class MastermindState(val placed: Int = 0, val misplaced: Int = 0) {
    fun addPlaced() = MastermindState(placed + 1, misplaced)
    fun addMisplaced() = MastermindState(placed, misplaced + 1)
}

class MastermindAttemptChecker(private val solution: String) {
    enum class PlacementType { Placed, Misplaced, Absent }

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
        fun isPlaced() = solution[index] == char
        fun isPresent() = remainingCountsByChar.getOrElse(char) { 0 } > 0

        val charPlacementType = when {
            isPlaced() -> PlacementType.Placed
            isPresent() -> PlacementType.Misplaced
            else -> PlacementType.Absent
        }
        removeCharFromCounts(char)
        return charPlacementType
    }

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