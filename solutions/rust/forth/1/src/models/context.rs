use std::collections::HashMap;

use crate::Error;
use crate::utilities::list_extensions::shift_two;

#[derive(Clone)]
pub struct Context {
	pub instructions_by_word: HashMap<String, String>,
	pub stack: Vec<i32>,
}

impl Context {
	pub fn new() -> Self {
		Context {
			instructions_by_word: HashMap::new(),
			stack: Vec::new(),
		}
	}

	pub fn define_handler(&self, group: String) -> Result<Context, Error> {
		let mut new_context = self.clone();
		let mut definition = group.split_whitespace().filter(|x| !x.is_empty()).collect::<Vec<_>>();
		if definition.len() < 2 {
			return Err(Error::StackUnderflow);
		}

		let word = definition.remove(0).to_lowercase();
		if word.parse::<i32>().is_ok() {
			return Err(Error::InvalidWord);
		}

		new_context.instructions_by_word.insert(word.clone(), new_context.create_instruction(&definition)?);
		Ok(new_context)
	}


	fn create_instruction(&self, definition: &[&str]) -> Result<String, Error> {
		let mut builder = String::new();
		for word in definition {
			let word_lower = word.to_lowercase();
			match self.instructions_by_word.get(&word_lower) {
				Some(instruction) => builder.push_str(&format!("{} ", instruction)),
				None => builder.push_str(&format!("{} ", word_lower)),
			}
		}

		Ok(builder.trim().to_string())
	}

	pub fn push_to_stack(&mut self, number: i32) -> Result<Context, Error> {
		self.with_stack(|stack: &mut Vec<i32>| {
			stack.push(number);
			Ok(())
		})
	}

	pub fn with_two_from_stack<F>(&mut self, function: F) -> Result<Context, Error>
		where F: FnOnce(&mut Vec<i32>, i32, i32) -> Result<(), Error>, {
		self.with_stack(|stack| {
			match shift_two(stack) {
				Ok((x, y)) => function(stack, x, y),
				Err(error) => Err(error),
			}
		})
	}

	pub fn with_stack<F>(&mut self, function: F) -> Result<Context, Error>
		where F: FnOnce(&mut Vec<i32>) -> Result<(), Error>, {
		function(&mut self.stack)?;
		Ok(self.clone())
	}
}
