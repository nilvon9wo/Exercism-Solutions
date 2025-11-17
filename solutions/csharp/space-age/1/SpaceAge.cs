using System.Collections.Generic;

public enum Planet
{
    Mercury,
    Venus,
    Earth,
    Mars,
    Jupiter,
    Saturn,
    Uranus,
    Nepture
}

public class SpaceAge
{
    private const double EarthOrbitalPeriodInSeconds = 31_557_600;
    private static readonly Dictionary<Planet, double> OrbitalPeriodInEarthYearsByPlanets = new Dictionary<Planet, double>
    {
        {Planet.Mercury, 0.2408467},
        {Planet.Venus, 0.61519726},
        {Planet.Earth, 1},
        {Planet.Mars, 1.8808158},
        {Planet.Jupiter, 11.862615},
        {Planet.Saturn, 29.447498},
        {Planet.Uranus, 84.016846},
        {Planet.Nepture, 164.79132}
    };



    private readonly int Seconds;
    public SpaceAge(int seconds) =>
        Seconds = seconds;

    public double OnEarth() =>
        AgeOn(Planet.Earth);

    public double OnMercury() =>
        AgeOn(Planet.Mercury);

    public double OnVenus() =>
        AgeOn(Planet.Venus);

    public double OnMars() =>
        AgeOn(Planet.Mars);

    public double OnJupiter() =>
        AgeOn(Planet.Jupiter);

    public double OnSaturn() =>
        AgeOn(Planet.Saturn);

    public double OnUranus() =>
        AgeOn(Planet.Uranus);

    public double OnNeptune() =>
        AgeOn(Planet.Nepture);

    private double AgeOn(Planet planet) =>
        Seconds / EarthOrbitalPeriodInSeconds / OrbitalPeriodInEarthYearsByPlanets.GetValueOrDefault(planet, 1);
}