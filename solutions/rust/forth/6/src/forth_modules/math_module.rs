use crate::{Error, Forth, Value};
use crate::forth_modules::stack_module::{pop, push};
use crate::models::results::ForthResult;

type Result<T> = std::result::Result<T, Error>;

pub fn math<F: FnOnce(Value, Value) -> Result<Value>>(forth: &mut Forth, op: F) -> ForthResult {
	let rhs = pop(forth)?;
	let lhs = pop(forth)?;
	push(forth, op(lhs, rhs)?);
	Ok(())
}
