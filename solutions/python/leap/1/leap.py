DIVISIBILITY_BY_4 = 4
DIVISIBILITY_BY_100 = 100
DIVISIBILITY_BY_400 = 400

def leap_year(year):
    return (
        year % DIVISIBILITY_BY_4 == 0
        and (
            year % DIVISIBILITY_BY_100 != 0
            or year % DIVISIBILITY_BY_400 == 0
        )
    )