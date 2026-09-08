PASSING_SCORE = 40
PERFECT_SCORE = 100
NUMBER_OF_GRADE_INTERVALS = 4
FIRST_RANK = 1


def round_scores(student_scores):
    return [
        round(score)
        for score in student_scores
    ]


def count_failed_students(student_scores):
    return sum(
            score <= PASSING_SCORE
            for score in student_scores
    )


def above_threshold(student_scores, threshold):
    return [
        score
        for score in student_scores
        if score >= threshold
    ]


def letter_grades(highest):
    grade_interval = (highest - PASSING_SCORE) // NUMBER_OF_GRADE_INTERVALS
    first_passing_score = PASSING_SCORE + 1

    return [
        first_passing_score + grade_interval * position
        for position in range(NUMBER_OF_GRADE_INTERVALS)
    ]


def student_ranking(student_scores, student_names):
    student_rankings = enumerate(
            zip(student_names, student_scores),
            start=FIRST_RANK,
    )

    rankings = []
    for rank, (student_name, student_score) in student_rankings :
        rankings.append(f"{rank}. {student_name}: {student_score}")

    return rankings


def perfect_score(student_info):
    for student_name, student_score in student_info:
        if student_score == PERFECT_SCORE:
            return [student_name, student_score]

    return []