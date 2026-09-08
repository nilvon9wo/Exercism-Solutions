"""Functions for controlling a nuclear reactor."""


def is_criticality_balanced(temperature, neutrons_emitted):
    """Return whether the reactor is in balanced criticality."""
    return (
            temperature < 800
            and neutrons_emitted > 500
            and temperature * neutrons_emitted < 500000
    )


def reactor_efficiency(voltage, current, theoretical_max_power):
    """Return the reactor efficiency band."""
    generated_power = voltage * current
    efficiency = generated_power / theoretical_max_power * 100

    if efficiency >= 80:
        return "green"
    if efficiency >= 60:
        return "orange"
    if efficiency >= 30:
        return "red"
    return "black"


def fail_safe(temperature, neutrons_produced_per_second, threshold):
    """Return the reactor fail-safe status."""
    criticality = temperature * neutrons_produced_per_second

    if criticality < threshold * 0.9:
        return "LOW"
    if criticality <= threshold * 1.1:
        return "NORMAL"
    return "DANGER"