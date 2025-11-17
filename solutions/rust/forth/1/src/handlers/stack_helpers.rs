use crate::Error;
use crate::models::context::Context;
use crate::utilities::list_extensions::{require_at_least_one_value, require_at_least_two_values};

pub fn duplicate_last(context: &mut Context) -> Result<Context, Error> {
	context.with_stack(|stack| {
		let values = require_at_least_one_value(stack)?;
		let last = values.last().copied().unwrap_or_default();
		stack.push(last);
		Ok(())
	})
}

pub fn drop_last(context: &mut Context) -> Result<Context, Error> {
	context.with_stack(|stack| {
		let _values = require_at_least_one_value(stack)?;
		stack.pop();
		Ok(())
	})
}

pub fn swap_last(context: &mut Context) -> Result<Context, Error> {
	context.with_stack(|stack| {
		let values = require_at_least_two_values(stack)?;
		let last_value = values.pop();
		let penultimate_value = stack.pop().unwrap();
		stack.push(last_value.unwrap());
		stack.push(penultimate_value);
		Ok(())
	})
}

pub fn penultimate_value_copy(context: &mut Context) -> Result<Context, Error> {
	context.with_stack(|stack| {
		let values = require_at_least_two_values(stack)?;
		if let Some(&penultimate_value) = values.get(values.len() - 2) {
			stack.push(penultimate_value);
		}
		Ok(())
	})
}
