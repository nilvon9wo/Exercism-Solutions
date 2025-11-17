use crate::Error;
use crate::models::context::Context;

pub enum FunctionHandler {
	Handle(for<'a> fn(&'a mut Context) -> Result<Context, Error>),
}