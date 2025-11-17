use std::cmp::min;
use std::collections::HashMap;
use std::str::Chars;
use std::thread;
use std::thread::JoinHandle;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    let combined_input = input.join("");
    if combined_input.is_empty() {
        return HashMap::<char, usize>::new();
    }

    let counters = create_counters(&combined_input, worker_count);
    merge_results_from(counters)
}

fn create_counters(input: &str, worker_count: usize) -> Vec<JoinHandle<HashMap<char, usize>>> {
    let input_length = input.len();
    let real_worker_count = min(input_length, worker_count);
    let work_length = calculate_work_length(input_length, real_worker_count);

    let input_characters: &mut Chars<'_> = &mut input
            .chars();
    (0..real_worker_count)
            .map(|_| create_counter(input_characters, work_length))
            .collect()
}

fn create_counter(input_characters: &mut Chars<'_>, work_length: usize) -> JoinHandle<HashMap<char, usize>> {
    let chunk = input_characters.by_ref()
            .take(work_length)
            .collect::<String>();

    thread::spawn(move || {
        count_characters(&chunk)
    })
}

fn calculate_work_length(input_length: usize, real_worker_count: usize) -> usize {
    let work_length = (input_length / real_worker_count)
            .max(1);

    if work_length * real_worker_count < input_length {
        work_length + 1
    } else {
        work_length
    }
}

fn count_characters(input: &str) -> HashMap<char, usize> {
    input.to_lowercase()
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

fn merge_results_from(counters: Vec<JoinHandle<HashMap<char, usize>>>) -> HashMap<char, usize> {
    counters.into_iter()
            .map(|counter| counter.join().unwrap())
            .fold(HashMap::new(), |mut accumulator, count_by_character| {
                for (key, val) in count_by_character.iter() {
                    *accumulator.entry(*key).or_default() += val;
                }
                accumulator
            })
}
