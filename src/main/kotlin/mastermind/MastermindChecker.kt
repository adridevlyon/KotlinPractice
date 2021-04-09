package mastermind

fun check(attempt: String, solution: String): MastermindState {
    val attemptChecker = MastermindAttemptChecker(solution = solution)
    return attemptChecker.process(attempt)
}

data class MastermindState(val placed: Int, val present: Int)

class MastermindAttemptChecker(val solution: String) {
    fun process(attempt: String): MastermindState {
        val processedChars = attempt.withIndex().map { (index, char) -> processChar(index, char) }
        val (placedChars, presentChars) = processedChars
            .filter { (placed, present) -> placed + present > 0 }
            .partition { (placed, _) -> placed > 0 }
        return MastermindState(placedChars.size, presentChars.size)
    }

    private fun processChar(index: Int, char: Char): Pair<Int, Int> {
        val (placed, present) = when {
            isPlaced(index, char) -> 1 to 0
            isPresent(char) -> 0 to 1
            else -> 0 to 0
        }
        removeCharFromCounts(char)
        return placed to present
    }

    private fun isPlaced(index: Int, char: Char) = solution[index] == char
    private fun isPresent(char: Char) = solutionCountsByChar.getOrElse(char) { 0 } > 0

    private val solutionCountsByChar: MutableMap<Char, Int> = mutableMapOf(
        *solution.groupBy { it }
            .map { (char, chars) -> char to chars.size }
            .toTypedArray()
    )

    private fun removeCharFromCounts(char: Char) {
        if (char !in solutionCountsByChar) return
        if (solutionCountsByChar[char] == 0) return

        solutionCountsByChar[char] = solutionCountsByChar.getOrElse(char) { 0 } - 1
    }
}