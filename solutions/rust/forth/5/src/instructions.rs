use crate::{Value};

#[derive(Clone, Debug)]
pub enum Instruction {
	Add,
	Sub,
	Mul,
	Div,
	Dup,
	Swap,
	Drop,
	Over,
	Number(Value),
	Call(Value),
}


pub struct Definition {
	pub(crate) name: String,
	pub(crate) body: Vec<Instruction>,
}