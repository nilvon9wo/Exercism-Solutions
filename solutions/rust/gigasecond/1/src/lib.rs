use chrono::{DateTime, Utc, Duration};
use std::ops::Add;

const SECONDS_PER_GIGASECOND: i64 = 1_000_000_000;

// Returns a Utc DateTime one billion seconds after start.
pub fn after(start: DateTime<Utc>) -> DateTime<Utc> {
    let duration = Duration::seconds(SECONDS_PER_GIGASECOND);
    start.add(duration)
}
