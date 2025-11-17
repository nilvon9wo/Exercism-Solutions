use crate::Error;
use crate::models::context::Context;

pub fn add(context: &mut Context) -> Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		let result = x + y;
		stack.push(result);
		Ok(())
	})
}

pub fn subtract(context: &mut Context) -> Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		let result = x - y;
		stack.push(result);
		Ok(())
	})
}

pub fn multiply(context: &mut Context) -> Result<Context, Error> {
	context.with_two_from_stack(|stack, x, y| {
		let result = x * y;
		stack.push(result);
		Ok(())
	})
}

pub fn divide(context: &mut Context) -> Result<Context, Error> {
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
