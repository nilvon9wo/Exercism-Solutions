use std::collections::HashMap;
use std::result;
use std::sync::Mutex;

use once_cell::sync::Lazy;
use regex::Regex;

use crate::handlers::default_handlers::get_default_handlers;
use crate::handlers::handler_function::FunctionHandler;
use crate::models::context::Context;
use crate::utilities::input_splitter::InputSplitter;
use crate::utilities::regex_capture_unwrapper::RegexCaptureUnwrapper;

mod models;
mod utilities;
mod handlers;

pub struct Forth {
	context: Context,
}

static NEW_DEFINITION_PATTERN: Lazy<Mutex<Regex>> = Lazy::new(|| {
	Mutex::new(Regex::new(r"^:(?P<new_definition>.*?)(?P<semicolon>;?)$").unwrap())
});


impl Forth {
	pub fn new() -> Forth {
		Forth {
			context: Context::new(),
		}
	}

	pub fn stack(&self) -> &[Value] {
		&self.context.stack
	}

	pub fn eval(&mut self, input: &str) -> Result {
		let mut context = self.context.clone();
		match Self::evaluate(&mut context, input) {
			Ok(new_context) => {
				self.context = new_context;
				Ok(self.context.stack.clone())
			}
			Err(error) => Err(error)
		}
	}

	pub fn evaluate(context: &mut Context, input: &str) -> result::Result<Context, Error> {
		let regex = &*NEW_DEFINITION_PATTERN.lock().unwrap();
		let mut new_context = context.clone();
		let parts: Vec<String> = InputSplitter::input_split(input);
		for part in parts.iter() {
			if let Some(captures) = regex.captures(&part) {
				new_context = Forth::handle_definition(&mut new_context, captures)?;
			} else {
				new_context = Forth::follow_instruction(new_context, &part)?;
			}
		}

		Ok(new_context)
	}

	fn handle_definition(context: &mut Context, captures: regex::Captures<'_>) -> result::Result<Context, Error> {
		let new_definition = captures.unwrap_string("new_definition");
		let semicolon = captures.unwrap_string("semicolon");
		if new_definition.is_empty() || semicolon.is_empty() {
			return Err(Error::InvalidWord);
		}
		context.define_handler(new_definition)
	}

	fn follow_instruction(mut context: Context, instruction: &str) -> result::Result<Context, Error> {
		let tokens: Vec<&str> = instruction.split_whitespace().collect();
		let mut index = 0;
		while index < tokens.len() {
			let next_operation = tokens[index];
			index += 1;
			match Forth::do_operation(&mut context, next_operation) {
				Ok(new_context) => context = new_context,
				Err(error) => return Err(error)
			};
		}

		Ok(context)
	}

	pub fn do_operation(context: &mut Context, operation: &str) -> result::Result<Context, Error> {
		if operation.is_empty() {
			Ok(context.clone())
		} else if let Ok(number) = operation.parse::<i32>() {
			context.push_to_stack(number)
		} else {
			let operation_key = operation.to_lowercase().clone();
			Forth::handle_word_operation(context, operation_key)
		}
	}

	fn handle_word_operation(context: &mut Context, operation_key: String) -> result::Result<Context, Error> {
		if let Some(instruction) = context.instructions_by_word.get(&operation_key) {
			Forth::follow_instruction(context.clone(), instruction)
		} else {
			match get_default_handlers() {
				Ok(handler) => Forth::handle_default_handler(context, &operation_key, &handler),
				Err(error) => Err(error),
			}
		}
	}

	fn handle_default_handler(context: &mut Context, operation_key: &String, handler: &HashMap<&'static str, FunctionHandler>) -> result::Result<Context, Error> {
		match handler.get(operation_key.as_str()) {
			Some(handler) => Forth::handle_function_handler(handler, context),
			None => Err(Error::UnknownWord),
		}
	}

	fn handle_function_handler(handler: &FunctionHandler, context: &mut Context) -> result::Result<Context, Error> {
		match handler {
			FunctionHandler::Handle(function) => function(context),
		}
	}
}

pub type Value = i32;
pub type Result = result::Result<Vec<i32>, Error>;

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
	DivisionByZero,
	StackUnderflow,
	UnknownWord,
	InvalidWord,
}