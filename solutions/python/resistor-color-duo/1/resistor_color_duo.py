RESISTOR_COLORS = [
    "black",
    "brown",
    "red",
    "orange",
    "yellow",
    "green",
    "blue",
    "violet",
    "grey",
    "white",
]

def value(colors):
    first_color = RESISTOR_COLORS.index(colors[0])
    second_color = RESISTOR_COLORS.index(colors[1])
    return first_color * 10 + second_color