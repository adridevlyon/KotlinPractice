package mastermind

class MastermindChecker {
    fun check(attempt: String, solution: String): MastermindState {
        return MastermindState()
    }
}

class MastermindState(var placed: Int = 0, var present: Int = 0) {}