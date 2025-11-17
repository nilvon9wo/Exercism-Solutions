use std::collections::HashMap;

pub fn can_construct_note(magazine: &[&str], note: &[&str]) -> bool {
    let count_by_magazine_words = count_word_occurrences(magazine);
    let count_by_note_words = count_word_occurrences(note);

    for (word, note_count) in count_by_note_words {
        match count_by_magazine_words.get(&word) {
            Some(magazine_count) if *magazine_count < note_count => return false,
            None => return false,
            _ => ()
        }
    }

    return true;
}

fn count_word_occurrences(text: &[&str]) -> HashMap::<String, i32>{
    text.join(" ")
            .split(" ")
            .collect::<Vec<&str>>()
            .iter()
            .fold(HashMap::<String, i32>::new(), |mut accumulator, word| {
                *accumulator.entry(String::from(*word))
                        .or_insert(0) += 1;
                accumulator
            })
}