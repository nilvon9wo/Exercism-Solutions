use std::collections::HashMap;

pub trait EnumerableExtensions<T, TKey: Eq + std::hash::Hash + Clone> {
	fn find_triplet_key<F>(&self, group_key_selector: F)
	                       -> Option<TKey>
		where F: Fn(&T) -> TKey;

	fn find_group_key<F, G>(&self, group_key_selector: F, condition: G)
	                        -> Option<TKey>
		where F: Fn(&T) -> TKey,
		      G: Fn(usize) -> bool;
}

impl<T, TKey: Eq + std::hash::Hash + Clone> EnumerableExtensions<T, TKey> for Vec<T> {
	fn find_triplet_key<F>(&self, group_key_selector: F)
	                       -> Option<TKey>
		where F: Fn(&T)
			-> TKey, {
		self.find_group_key(group_key_selector, |count| count >= 3)
	}

	fn find_group_key<F, G>(&self, group_key_selector: F, condition: G)
	                        -> Option<TKey>
		where F: Fn(&T) -> TKey,
		      G: Fn(usize) -> bool, {
		let mut groups: HashMap<TKey, Vec<&T>> = HashMap::new();
		for item in self {
			groups.entry(group_key_selector(item)).or_insert_with(Vec::new).push(item);
		}

		for (key, group) in groups {
			if condition(group.len()) {
				return Some(key);
			}
		}

		None
	}
}
