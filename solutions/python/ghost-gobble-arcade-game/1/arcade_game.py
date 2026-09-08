"""Functions for implementing the Ghost Gobble Arcade Game."""

def eat_ghost(has_power_pellet, touching_ghost):
    """Return whether Pac-Man can eat the ghost."""
    return has_power_pellet and touching_ghost


def score(touching_power_pellet, touching_dot):
    """Return whether Pac-Man scores."""
    return touching_power_pellet or touching_dot


def lose(has_power_pellet, touching_ghost):
    """Return whether Pac-Man loses."""
    return touching_ghost and not has_power_pellet


def win(has_eaten_all_dots, has_power_pellet, touching_ghost):
    """Return whether Pac-Man wins."""
    return has_eaten_all_dots and not lose(has_power_pellet, touching_ghost)