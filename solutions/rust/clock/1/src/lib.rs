use std::fmt::{Display, Formatter};

#[derive(Debug)]
pub struct Clock {
    hours: i32,
    minutes: i32,
}

impl Display for Clock {
    fn fmt(&self, formatter: &mut Formatter<'_>) -> std::fmt::Result {
        write!(formatter, "{:0>2}:{:0>2}", self.hours, self.minutes)
    }
}

impl PartialEq<Self> for Clock {
    fn eq(&self, other: &Self) -> bool {
        let normalized_self = normalize(self);
        let normalized_other = normalize(other);

        normalized_self.hours == normalized_other.hours
        && normalized_self.minutes == normalized_other.minutes
    }
}

const MINUTES_PER_HOUR: i32 = 60;
const HOURS_PER_DAY: i32 = 24;

impl Clock {
    pub fn new(hours: i32, minutes: i32) -> Self {
        normalize(&Clock { hours, minutes })
    }

    pub fn add_minutes(self, minutes: i32) -> Self {
        normalize(
            &Clock {
                minutes : self.minutes + minutes,
                        ..self
            }
        )
    }
}

fn normalize(input: &Clock) -> Clock {
    Clock {
        hours : normalize_hours(&input),
        minutes: normalize_minutes(input.minutes)
    }
}

fn normalize_hours(input: &Clock) -> i32 {
    if input.hours >= 0 && input.minutes >= 0 {
        ((input.hours * MINUTES_PER_HOUR + input.minutes) / MINUTES_PER_HOUR) % HOURS_PER_DAY
    }
    else {
        if input.hours < 0 {
            normalize_hours(&Clock{
                hours: (input.hours % HOURS_PER_DAY) + HOURS_PER_DAY,
                ..*input
            })
        }
        else {
            let hour_offset = if input.minutes % MINUTES_PER_HOUR != 0 {
                1
            }
            else {
                0
            };

            normalize_hours(&Clock {
                hours: input.hours + (input.minutes / MINUTES_PER_HOUR) - hour_offset,

                // We need to change the minutes here to non-negative number or we get an infinite loop.
                // This value no longer matters.
                minutes: 0,
                ..*input
            })
        }
    }
}


fn normalize_minutes(minutes: i32) -> i32 {
    let absolute_minutes = if minutes >= 0 {
        minutes % MINUTES_PER_HOUR
    }
    else {
        (minutes % MINUTES_PER_HOUR) + MINUTES_PER_HOUR
    };
    if absolute_minutes != 60 {
        absolute_minutes
    }
    else {
        0
    }
}