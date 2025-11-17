use std::{convert::TryInto, str::FromStr};

use crate::models::definition::{Definition, find_definition, parse_definition};
use crate::models::instruction::{evaluate_instruction, Instruction};
use crate::models::results::ForthResult;

mod models;
mod forth_modules;

type Result<T> = std::result::Result<T, Error>;
pub type Value = i32;

#[derive(Default)]
pub struct Forth {
	dict: Vec<Definition>,
	stack: Vec<Value>,
}

impl Forth {
	pub fn new() -> Forth {
		Default::default()
	}

	pub fn stack(&self) -> Vec<Value> {
		self.stack.clone()
	}

	pub fn eval(&mut self, input: &str) -> ForthResult {
		let mut iter = input.split_ascii_whitespace();
		while let Some(word) = iter.next() {
			self.parse_word(word, &mut iter)?;
		}
		Ok(())
	}

	fn parse_word<'a>(&mut self, word: &'a str, remaining_input: &mut impl Iterator<Item=&'a str>) -> ForthResult {
		if word == ":" {
			parse_definition(self, remaining_input)
		} else {
			let instruction = self.parse_normal_word(word)?;
			evaluate_instruction(self, instruction)
		}
	}

	fn parse_normal_word(&mut self, word: &str) -> Result<Instruction> {
		if word == ":" || word == ";" {
			Err(Error::InvalidWord)
		} else {
			let canonical = word.to_ascii_uppercase();
			if let Some(call) = find_definition(self, &canonical) {
				Ok(call)
			} else {
				parse_builtin(&canonical)
			}
		}
	}

	fn call(&mut self, idx: Value) -> ForthResult {
		let idx = idx.try_into().unwrap();
		if self.dict.len() <= idx {
			eprintln!("call {} but dict is only of length {}", idx, self.dict.len());
			Err(Error::UnknownWord)
		} else {
			let def = self.dict[idx].body.clone();
			for instr in def {
				evaluate_instruction(self, instr)?;
			}
			Ok(())
		}
	}
}


fn parse_builtin(word: &str) -> Result<Instruction> {
	match word {
		"+" => Ok(Instruction::Add),
		"-" => Ok(Instruction::Sub),
		"*" => Ok(Instruction::Mul),
		"/" => Ok(Instruction::Div),
		"DUP" => Ok(Instruction::Dup),
		"SWAP" => Ok(Instruction::Swap),
		"DROP" => Ok(Instruction::Drop),
		"OVER" => Ok(Instruction::Over),
		_ => if let Ok(num) = Value::from_str(word) {
			Ok(Instruction::Number(num))
		} else {
			eprintln!("parse_builtin failed");
			Err(Error::UnknownWord)
		}
	}
}

#[derive(Debug, PartialEq)]
pub enum Error {
	DivisionByZero,
	StackUnderflow,
	UnknownWord,
	InvalidWord,
}
