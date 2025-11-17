use std::collections::HashMap;
use std::result;
use std::sync::Mutex;
use once_cell::sync::Lazy;
use regex::Regex;

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


//=======================================================================



static mut DEFAULT_HANDLERS: Option<HashMap<&'static str, FunctionHandler>> = None;

pub fn get_default_handlers() -> result::Result<&'static HashMap<&'static str, FunctionHandler>, Error> {
	unsafe {
		if DEFAULT_HANDLERS.is_none() {
			DEFAULT_HANDLERS = Some(initialize_default_handlers());
		}

		match DEFAULT_HANDLERS.as_ref() {
			Some(handler) => Ok(handler),
			None => Err(Error::InvalidWord)
		}
	}
}

fn initialize_default_handlers() -> HashMap<&'static str, FunctionHandler> {
	return HashMap::from([
		("+", FunctionHandler::Handle(add)),
		("-", FunctionHandler::Handle(subtract)),
		("*", FunctionHandler::Handle(multiply)),
		("/", FunctionHandler::Handle(divide)),
		("dup", FunctionHandler::Handle(duplicate_last)),
		("drop", FunctionHandler::Handle(drop_last)),
		("swap", FunctionHandler::Handle(swap_last)),
		("over", FunctionHandler::Handle(penultimate_value_copy)),
	]);
}




//=======================================================================



pub enum FunctionHandler {
	Handle(for<'a> fn(&'a mut Context) -> result::Result<Context, Error>),
}


//=======================================================================



pub fn add(context: &mut Context) -> result::Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		let result = x + y;
		stack.push(result);
		Ok(())
	})
}

pub fn subtract(context: &mut Context) -> result::Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		let result = x - y;
		stack.push(result);
		Ok(())
	})
}

pub fn multiply(context: &mut Context) -> result::Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		let result = x * y;
		stack.push(result);
		Ok(())
	})
}

pub fn divide(context: &mut Context) -> result::Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		if y == 0 {
			Err(Error::DivisionByZero)
		} else {
			let result = x / y;
			stack.push(result);
			Ok(())
		}
	})
}



//=======================================================================





//=======================================================================



pub fn duplicate_last(context: &mut Context) -> result::Result<Context, Error> {
	context.with_stack(|stack| {
		let values = require_at_least_one_value(stack)?;
		let last = values.last().copied().unwrap_or_default();
		stack.push(last);
		Ok(())
	})
}

pub fn drop_last(context: &mut Context) -> result::Result<Context, Error> {
	context.with_stack(|stack| {
		let _values = require_at_least_one_value(stack)?;
		stack.pop();
		Ok(())
	})
}

pub fn swap_last(context: &mut Context) -> result::Result<Context, Error> {
	context.with_stack(|stack| {
		let values = require_at_least_two_values(stack)?;
		let last_value = values.pop();
		let penultimate_value = stack.pop().unwrap();
		stack.push(last_value.unwrap());
		stack.push(penultimate_value);
		Ok(())
	})
}

pub fn penultimate_value_copy(context: &mut Context) -> result::Result<Context, Error> {
	context.with_stack(|stack| {
		let values = require_at_least_two_values(stack)?;
		if let Some(&penultimate_value) = values.get(values.len() - 2) {
			stack.push(penultimate_value);
		}
		Ok(())
	})
}



//=======================================================================



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

	pub fn define_handler(&self, group: String) -> result::Result<Context, Error> {
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


	fn create_instruction(&self, definition: &[&str]) -> result::Result<String, Error> {
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

	pub fn push_to_stack(&mut self, number: i32) -> result::Result<Context, Error> {
		self.with_stack(|stack: &mut Vec<i32>| {
			stack.push(number);
			Ok(())
		})
	}

	pub fn with_two_from_stack<F>(&mut self, function: F) -> result::Result<Context, Error>
		where F: FnOnce(&mut Vec<i32>, i32, i32) -> result::Result<(), Error>, {
		self.with_stack(|stack| {
			match shift_two(stack) {
				Ok((x, y)) => function(stack, x, y),
				Err(error) => Err(error),
			}
		})
	}

	pub fn with_stack<F>(&mut self, function: F) -> result::Result<Context, Error>
		where F: FnOnce(&mut Vec<i32>) -> result::Result<(), Error>, {
		function(&mut self.stack)?;
		Ok(self.clone())
	}
}



//=======================================================================




//=======================================================================


pub struct InputSplitter;

impl InputSplitter {
	pub fn input_split(input: &str) -> Vec<String> {
		let mut commands = Vec::new();
		let mut string_builder = String::new();

		for character in input.chars() {
			match character {
				':' => {
					Self::handle_colon(&mut commands, &mut string_builder);
				}
				';' => {
					Self::handle_semicolon(&mut commands, &mut string_builder);
				}
				_ => {
					string_builder.push(character);
				}
			}
		}

		Self::handle_final_command(&mut commands, &mut string_builder);

		commands
	}

	fn handle_colon(commands: &mut Vec<String>, string_builder: &mut String) {
		Self::handle_final_command(commands, string_builder);
		string_builder.push(':');
	}

	fn handle_semicolon(commands: &mut Vec<String>, string_builder: &mut String) {
		string_builder.push(';');
		Self::handle_final_command(commands, string_builder);
	}

	fn handle_final_command(commands: &mut Vec<String>, string_builder: &mut String) {
		let command = string_builder.clone();
		if !command.is_empty() {
			commands.push(command);
			string_builder.clear();
		}
	}
}



//=======================================================================



pub fn require_at_least_one_value<T>(values: &mut Vec<T>) -> result::Result<&mut Vec<T>, Error> {
	require_values(values, |len| len >= 1)
}

pub fn require_at_least_two_values<T>(values: &mut Vec<T>) -> result::Result<&mut Vec<T>, Error> {
	require_values(values, |len| len >= 2)
}

pub fn require_exactly_two_values<T>(values: &mut Vec<T>) -> result::Result<&mut Vec<T>, Error> {
	require_values(values, |len| len == 2)
}

fn require_values<T, F>(values: &mut Vec<T>, condition: F) -> result::Result<&mut Vec<T>, Error>
	where F: FnOnce(usize) -> bool, {
	if condition(values.len()) {
		Ok(values)
	} else {
		Err(Error::StackUnderflow)
	}
}

pub fn shift_two<T>(values: &mut Vec<T>) -> result::Result<(T, T), Error> {
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



//=======================================================================





//=======================================================================


pub trait RegexCaptureUnwrapper {
	fn unwrap_string(&self, name: &str) -> String;
}

impl RegexCaptureUnwrapper for regex::Captures<'_> {
	fn unwrap_string(&self, name: &str) -> String {
		self.name(name).map(|m| m.as_str().trim().to_string()).unwrap_or_else(String::new)
	}
}


//=======================================================================


