object HighScores:

    def latest(scores: List[Int]): Int =
        scores.last

    def personalBest(scores: List[Int]): Int =
        scores.max

    def personalTop(scores: List[Int]): List[Int] =
        scores.sorted(Ordering[Int].reverse).take(3)

    def report(scores: List[Int]): String =
        val latestScore = latest(scores)
        val bestScore = personalBest(scores)
        if (latestScore == bestScore) {
            s"Your latest score was $latestScore. That's your personal best!"
        }
        else {
            val diff = bestScore - latestScore
            s"Your latest score was $latestScore. That's $diff short of your personal best!"
        }
