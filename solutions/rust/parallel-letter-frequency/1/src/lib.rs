use std::collections::HashMap;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    match worker_count {
        1 => count_characters(&input.join("")),

        // TODO: Distribute strings between workers
        // TODO: Combine worker responses
        _ => count_characters(&input.join(""))
    }
}

fn count_characters(input: &str) -> HashMap<char, usize> {
    input
            .to_lowercase()
            .chars()
            .fold(HashMap::new(), |mut accumulator, character| {
                if character.is_alphabetic() {
                    let counter = accumulator.entry(character)
                            .or_insert(0);
                    *counter += 1;
                }
                accumulator
            })
}
