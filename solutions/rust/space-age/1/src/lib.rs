#[derive(Debug)]
pub struct Duration {
    seconds : u64
}

impl From<u64> for Duration {
    fn from(seconds: u64) -> Self {
        Duration {
            seconds
        }
    }
}

const SECONDS_PER_EARTH_YEAR: f64 = 31_557_600.00;
const EARTH_YEAR_PER_MERCURY_YEAR: f64 = 0.2408467;
const EARTH_YEAR_PER_VENUS_YEAR: f64 = 0.61519726;
const EARTH_YEAR_PER_MARS_YEAR: f64 = 1.8808158;
const EARTH_YEAR_PER_JUPITER_YEAR: f64 = 11.862615;
const EARTH_YEAR_PER_SATURN_YEAR: f64 = 29.447498;
const EARTH_YEAR_PER_URANUS_YEAR: f64 = 84.016846;
const EARTH_YEAR_PER_NEPTUNE_YEAR: f64 = 164.79132;

pub trait Planet {
    fn years_during(d: &Duration) -> f64;
}

pub struct Mercury;
pub struct Venus;
pub struct Earth;
pub struct Mars;
pub struct Jupiter;
pub struct Saturn;
pub struct Uranus;
pub struct Neptune;

impl Planet for Mercury {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_MERCURY_YEAR
    }
}

impl Planet for Venus {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_VENUS_YEAR

    }
}

impl Planet for Earth {
    fn years_during(duration: &Duration) -> f64 {
        (duration.seconds as f64) / SECONDS_PER_EARTH_YEAR
    }
}

impl Planet for Mars {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_MARS_YEAR
    }
}

impl Planet for Jupiter {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_JUPITER_YEAR
    }
}

impl Planet for Saturn {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_SATURN_YEAR
    }
}

impl Planet for Uranus {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_URANUS_YEAR
    }
}

impl Planet for Neptune {
    fn years_during(duration: &Duration) -> f64 {
        Earth::years_during(duration) / EARTH_YEAR_PER_NEPTUNE_YEAR
    }
}
