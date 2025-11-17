const RADIX: u32 = 10;

/// Check a Luhn checksum.
pub fn is_valid(code: &str) -> bool {
    code.trim().len() >= 2
            && contains_only_digits_and_white_space(code)
            && has_valid_checksum(code)
}

fn contains_only_digits_and_white_space(code: &str) -> bool {
    code.bytes()
            .all(|character|
                    character.is_ascii_digit()
                            || character.is_ascii_whitespace()
            )
}

fn has_valid_checksum(code: &str) -> bool {
    let sum: u32 =  code.chars()
            .filter(|character| character.is_digit(RADIX))
            .rev()
            .enumerate()
            .fold(0, |sum, (index, character)| sum + to_value(index, &character));

    sum % 10 == 0
}

fn to_value(index: usize, character: &char) -> u32 {
    let mut value: u32 = character.to_digit(RADIX)
            .unwrap();

    if index % 2 != 0 {
        value *= 2;
    }

    if value > 9 {
        value -= 9;
    }

    value
}