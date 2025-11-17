const MINUTES_PER_HOUR: u32 = 60;
const CARS_PRODUCED_PER_HOUR_PER_SPEED: u8 = 221;
const SUCCESS_RATE_SAFE: f64 = 1.0;
const SUCCESS_RATE_MODERATE_RISK: f64 = 0.9;
const SUCCESS_RATE_HIGH_RISK: f64 = 0.77;

pub fn production_rate_per_hour(speed: u8) -> f64 {
    let cars_per_hour = CARS_PRODUCED_PER_HOUR_PER_SPEED * speed;
    let success_rate = calculate_success_percentage(speed);
    (cars_per_hour as f64) * success_rate
}

fn calculate_success_percentage(speed: u8) -> f64 {
    match speed {
        x if x <= 4 => SUCCESS_RATE_SAFE,
        x if x <= 8 => SUCCESS_RATE_MODERATE_RISK,
        _ => SUCCESS_RATE_HIGH_RISK
    }
}

pub fn working_items_per_minute(speed: u8) -> u32 {
    let rate_per_hour = production_rate_per_hour(speed);
    rate_per_hour as u32 / MINUTES_PER_HOUR
}
