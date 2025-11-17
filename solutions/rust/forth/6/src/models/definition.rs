use std::str::FromStr;

use crate::{Error, Forth, Value};
use crate::models::instruction::Instruction;
use crate::models::results::ForthResult;

pub struct Definition {
	pub(crate) name: String,
	pub(crate) body: Vec<Instruction>,
}


pub fn find_definition(forth: &Forth, word: &str) -> Option<Instruction> {
	for (index, definition) in forth.dict.iter().enumerate().rev() {
		if definition.name == word {
			return Some(Instruction::Call(index.try_into().unwrap()));
		}
	}
	None
}

pub fn parse_definition<'a>(forth: &mut Forth, iterator: &mut impl Iterator<Item=&'a str>) -> ForthResult {
	if let Some(new_word) = iterator.next() {
		if let Ok(_) = Value::from_str(new_word) {
			return Err(Error::InvalidWord);
		}
		let name = new_word.to_ascii_uppercase();
		let mut body = Vec::new();
		for word in iterator {
			if word == ";" {
				forth.dict.push(Definition { name, body });
				return Ok(());
			} else {
				body.push(forth.parse_normal_word(word)?)
			}
		}
	}
	eprintln!("parse_definition failed");
	Err(Error::InvalidWord)
}