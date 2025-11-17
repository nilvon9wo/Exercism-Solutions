use std::collections::HashMap;

use crate::Error;
use crate::handlers::handler_function::FunctionHandler;
use crate::handlers::math_helpers::{*};
use crate::handlers::stack_helpers::{*};

static mut DEFAULT_HANDLERS: Option<HashMap<&'static str, FunctionHandler>> = None;

pub fn get_default_handlers() -> Result<&'static HashMap<&'static str, FunctionHandler>, Error> {
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

