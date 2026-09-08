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

OHMS_PER_KILOOHM = 1_000
OHMS_PER_MEGAOHM = 1_000_000
OHMS_PER_GIGOHM = 1_000_000_000

UNIT_OHMS = "ohms"
UNIT_KILOOHMS = "kiloohms"
UNIT_MEGAOHMS = "megaohms"
UNIT_GIGOHMS = "gigaohms"

def get_color_value(color):
    return RESISTOR_COLORS.index(color)

def get_resistance_value(colors):
    first_value = get_color_value(colors[0])
    second_value = get_color_value(colors[1])
    multiplier = get_color_value(colors[2])

    return (first_value * 10 + second_value) * 10 ** multiplier

def get_unit_and_value(resistance):
    if resistance >= OHMS_PER_GIGOHM:
        return resistance // OHMS_PER_GIGOHM, UNIT_GIGOHMS

    if resistance >= OHMS_PER_MEGAOHM:
        return resistance // OHMS_PER_MEGAOHM, UNIT_MEGAOHMS

    if resistance >= OHMS_PER_KILOOHM:
        return resistance // OHMS_PER_KILOOHM, UNIT_KILOOHMS

    return resistance, UNIT_OHMS

def label(colors):
    resistance = get_resistance_value(colors)
    value, unit = get_unit_and_value(resistance)
    return f"{value} {unit}"