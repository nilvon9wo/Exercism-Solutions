pub trait RegexCaptureUnwrapper {
	fn unwrap_string(&self, name: &str) -> String;
}

impl RegexCaptureUnwrapper for regex::Captures<'_> {
	fn unwrap_string(&self, name: &str) -> String {
		self.name(name).map(|m| m.as_str().trim().to_string()).unwrap_or_else(String::new)
	}
}