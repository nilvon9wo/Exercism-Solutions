const MINE: char = '*';
const BLANK: char = ' ';

pub fn annotate(minefield: &[&str]) -> Vec<String> {
    let mut result: Vec<String> = Vec::new();
    for (row_index, row) in minefield.to_vec().iter().enumerate() {
        let mut result_row = "".to_string();
        for (column_index, column) in row.chars().enumerate() {
            if column == MINE {
                result_row.push(MINE);
            } else {
                let mine_count = count_surrounding_mines(minefield, row_index, column_index);
                let mine_count_character = to_character(mine_count);
                result_row.push(mine_count_character);
            }
        }
        result.push(result_row.to_string());
    }
    result
}

fn count_surrounding_mines(minefield: &[&str], row_index: usize, column_index: usize) -> usize {
    count_mines_directly_above(minefield, row_index, column_index)
            + count_from(minefield[row_index], column_index)
            + count_mines_directly_below(minefield, row_index, column_index)
}

fn count_mines_directly_above(minefield: &[&str], row_index: usize, column_index: usize) -> usize {
    if row_index > 0 {
        count_from(minefield[row_index - 1], column_index)
    } else {
        0
    }
}

fn count_mines_directly_below(minefield: &[&str], row_index: usize, column_index: usize) -> usize {
    if row_index < minefield.len() - 1 {
        count_from(minefield[row_index + 1], column_index)
    } else {
        0
    }
}

fn count_from(current_row: &str, current_index: usize) -> usize {
    let start = if current_index == 0 {
        0
    } else {
        current_index - 1
    };

    let end = if current_index + 1 < current_row.len() {
        current_index + 2
    } else {
        current_row.len()
    };

    current_row[start..end]
            .matches(MINE)
            .count()
}

fn to_character(mine_count: usize) -> char {
    if mine_count > 0 {
        char::from_digit(mine_count as u32, 10)
                .unwrap()
    } else {
        BLANK
    }
}




