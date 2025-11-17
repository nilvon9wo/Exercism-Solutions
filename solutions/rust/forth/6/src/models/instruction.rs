use crate::{Error, Forth, Value};
use crate::forth_modules::math_module::math;
use crate::forth_modules::stack_module::{drop, dup, over, push, swap};
use crate::models::results::ForthResult;

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


pub fn evaluate_instruction(forth: &mut Forth, instr: Instruction) -> ForthResult {
	match instr {
		Instruction::Add => math(forth, |a, b| Ok(a + b)),
		Instruction::Sub => math(forth, |a, b| Ok(a - b)),
		Instruction::Mul => math(forth, |a, b| Ok(a * b)),
		Instruction::Div => math(forth, |a, b| if b == 0 {
			eprintln!("Division by zero");
			Err(Error::DivisionByZero)
		} else {
			Ok(a / b)
		}),
		Instruction::Dup => dup(forth),
		Instruction::Swap => swap(forth),
		Instruction::Drop => drop(forth),
		Instruction::Over => over(forth),
		Instruction::Number(n) => {
			push(forth, n);
			Ok(())
		}
		Instruction::Call(idx) => forth.call(idx),
	}
}
