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

TOLERANCES = {
    "grey": "0.05",
    "violet": "0.1",
    "blue": "0.25",
    "green": "0.5",
    "brown": "1",
    "red": "2",
    "gold": "5",
    "silver": "10",
}

OHMS_PER_KILOOHM = 1_000
OHMS_PER_MEGAOHM = 1_000_000

def get_color_value(color):
    return RESISTOR_COLORS.index(color)

def get_resistance_value(colors):
    if len(colors) == 1:
        return 0

    significant_value = _get_significant_value(colors)
    multiplier = _get_multiplier(colors)
    return significant_value * 10 ** multiplier

def _get_significant_value(colors):
    if len(colors) == 4:
        return (
            get_color_value(colors[0]) * 10
            + get_color_value(colors[1])
        )

    return (
        get_color_value(colors[0]) * 100
        + get_color_value(colors[1]) * 10
        + get_color_value(colors[2])
    )

def _get_multiplier(colors):
    if len(colors) == 4:
        multiplier_color = colors[2]
    else:
        multiplier_color = colors[3]

    return get_color_value(multiplier_color)

def format_resistance(resistance):
    if resistance >= OHMS_PER_MEGAOHM:
        value = resistance / OHMS_PER_MEGAOHM
        return f"{value:g} megaohms"

    if resistance >= OHMS_PER_KILOOHM:
        value = resistance / OHMS_PER_KILOOHM
        return f"{value:g} kiloohms"

    return f"{resistance} ohms"

def format_tolerance(colors):
    if len(colors) == 4:
        tolerance_color = colors[3]
    else:
        tolerance_color = colors[4]

    tolerance = TOLERANCES[tolerance_color]
    return f"±{tolerance}%"

def resistor_label(colors):
    resistance = get_resistance_value(colors)
    resistance_label = format_resistance(resistance)
    if len(colors) == 1:
        return resistance_label

    tolerance_label = format_tolerance(colors)
    return f"{resistance_label} {tolerance_label}"