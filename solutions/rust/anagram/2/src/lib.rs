use std::collections::HashSet;

pub fn anagrams_for<'a>(word: &str, possible_anagrams: &[&'a str]) -> HashSet<&'a str> {
    let lowercase_word = word.to_lowercase();
    let sorted_word_characters = sort_characters(&lowercase_word);

    let values = possible_anagrams.into_iter()
            .filter(|x| {
                let lowercase_x = x.to_lowercase();

                x.to_lowercase() != lowercase_word
                        && sort_characters(&lowercase_x) == sorted_word_characters
            }
            );

    HashSet::from_iter(values.cloned())
}

fn sort_characters(word: &str) -> String {
    let mut characters: Vec<char> = word.chars()
            .collect();
    characters.sort();
    characters.into_iter()
            .collect::<String>()
}