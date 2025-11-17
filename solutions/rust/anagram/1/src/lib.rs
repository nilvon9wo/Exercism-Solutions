use std::collections::HashSet;

pub fn anagrams_for<'a>(word: &str, possible_anagrams: &[&'a str]) -> HashSet<&'a str> {
    let sorted_word_characters = sort_characters(word);

    let values = possible_anagrams.into_iter()
            .filter(|x| is_not_same_word(word, x)
                    && sort_characters(x) == sorted_word_characters
            )
            .cloned();

    HashSet::from_iter(values)
}

fn is_not_same_word(target: &str, other: &str) -> bool {
    target.to_lowercase() != other.to_lowercase()
}

fn sort_characters(word: &str) -> String {
    let mut characters: Vec<char> = word.to_lowercase()
            .chars()
            .collect();
    characters.sort_by(|a, b| b.cmp(a));
    characters.into_iter()
            .collect::<String>()
}