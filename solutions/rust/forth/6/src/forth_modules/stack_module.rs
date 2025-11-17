use crate::{Error, Forth, Value};
use crate::models::results::ForthResult;

type Result<T> = std::result::Result<T, Error>;

pub fn push(forth: &mut Forth, val: Value) {
	forth.stack.push(val);
}

pub fn pop(forth: &mut Forth) -> Result<Value> {
	if let Some(v) = forth.stack.pop() {
		Ok(v)
	} else {
		eprintln!("Stack underflow!");
		Err(Error::StackUnderflow)
	}
}

pub fn dup(forth: &mut Forth) -> ForthResult {
	let v = pop(forth)?;
	push(forth, v);
	push(forth, v);
	Ok(())
}

pub fn swap(forth: &mut Forth) -> ForthResult {
	let top = pop(forth)?;
	let bottom = pop(forth)?;
	push(forth, top);
	push(forth, bottom);
	Ok(())
}

pub fn drop(forth: &mut Forth) -> ForthResult {
	pop(forth)?;
	Ok(())
}

pub fn over(forth: &mut Forth) -> ForthResult {
	let top = pop(forth)?;
	let bottom = pop(forth)?;
	push(forth, bottom);
	push(forth, top);
	push(forth, bottom);
	Ok(())
}