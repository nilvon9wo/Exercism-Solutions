pub struct InputSplitter;

impl InputSplitter {
	pub fn input_split(input: &str) -> Vec<String> {
		let mut commands = Vec::new();
		let mut string_builder = String::new();

		for character in input.chars() {
			match character {
				':' => {
					Self::handle_colon(&mut commands, &mut string_builder);
				}
				';' => {
					Self::handle_semicolon(&mut commands, &mut string_builder);
				}
				_ => {
					string_builder.push(character);
				}
			}
		}

		Self::handle_final_command(&mut commands, &mut string_builder);

		commands
	}

	fn handle_colon(commands: &mut Vec<String>, string_builder: &mut String) {
		Self::handle_final_command(commands, string_builder);
		string_builder.push(':');
	}

	fn handle_semicolon(commands: &mut Vec<String>, string_builder: &mut String) {
		string_builder.push(';');
		Self::handle_final_command(commands, string_builder);
	}

	fn handle_final_command(commands: &mut Vec<String>, string_builder: &mut String) {
		let command = string_builder.clone();
		if !command.is_empty() {
			commands.push(command);
			string_builder.clear();
		}
	}
}
