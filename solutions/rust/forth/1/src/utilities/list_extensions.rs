use crate::Error;

pub fn require_at_least_one_value<T>(values: &mut Vec<T>) -> Result<&mut Vec<T>, Error> {
	require_values(values, |len| len >= 1)
}

pub fn require_at_least_two_values<T>(values: &mut Vec<T>) -> Result<&mut Vec<T>, Error> {
	require_values(values, |len| len >= 2)
}

pub fn require_exactly_two_values<T>(values: &mut Vec<T>) -> Result<&mut Vec<T>, Error> {
	require_values(values, |len| len == 2)
}

fn require_values<T, F>(values: &mut Vec<T>, condition: F) -> Result<&mut Vec<T>, Error>
	where F: FnOnce(usize) -> bool, {
	if condition(values.len()) {
		Ok(values)
	} else {
		Err(Error::StackUnderflow)
	}
}

pub fn shift_two<T>(values: &mut Vec<T>) -> Result<(T, T), Error> {
	let at_least_two_values = require_exactly_two_values(values)?;
	match shift(at_least_two_values) {
		Some(x) => match shift(at_least_two_values) {
			Some(y) => Ok((x, y)),
			None => Err(Error::StackUnderflow)
		},

		None => Err(Error::StackUnderflow)
	}
}

pub fn shift<T>(values: &mut Vec<T>) -> Option<T> {
	if values.is_empty() {
		None
	} else {
		Some(values.remove(0))
	}
}
