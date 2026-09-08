EARTH_YEAR_SECONDS = 31_557_600

MERCURY_ORBITAL_PERIOD = 0.2408467
VENUS_ORBITAL_PERIOD = 0.61519726
EARTH_ORBITAL_PERIOD = 1.0
MARS_ORBITAL_PERIOD = 1.8808158
JUPITER_ORBITAL_PERIOD = 11.862615
SATURN_ORBITAL_PERIOD = 29.447498
URANUS_ORBITAL_PERIOD = 84.016846
NEPTUNE_ORBITAL_PERIOD = 164.79132

class SpaceAge:
    def __init__(self, seconds):
        self.seconds = seconds

    def _age_on_planet(self, orbital_period):
        earth_years = self.seconds / EARTH_YEAR_SECONDS
        return round(earth_years / orbital_period, 2)

    def on_mercury(self):
        return self._age_on_planet(MERCURY_ORBITAL_PERIOD)

    def on_venus(self):
        return self._age_on_planet(VENUS_ORBITAL_PERIOD)

    def on_earth(self):
        return self._age_on_planet(EARTH_ORBITAL_PERIOD)

    def on_mars(self):
        return self._age_on_planet(MARS_ORBITAL_PERIOD)

    def on_jupiter(self):
        return self._age_on_planet(JUPITER_ORBITAL_PERIOD)

    def on_saturn(self):
        return self._age_on_planet(SATURN_ORBITAL_PERIOD)

    def on_uranus(self):
        return self._age_on_planet(URANUS_ORBITAL_PERIOD)

    def on_neptune(self):
        return self._age_on_planet(NEPTUNE_ORBITAL_PERIOD)