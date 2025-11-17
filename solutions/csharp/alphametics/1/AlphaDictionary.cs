using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Text;

public class AlphaDictionary : IDictionary<char, int>
{
	private readonly HashSet<char> _cannotBeZeroSet;

	public AlphaDictionary(HashSet<char> nonZeroLetters) =>
		_cannotBeZeroSet = nonZeroLetters;

	private readonly Dictionary<char, int> _dictionary = new();

	public Exception Exception { get; set; }

	public const char Placeholder = ' ';

	public int this[char key]
	{
		get =>
			key == Placeholder
				? 0
				: _dictionary[key];

		set
		{
			if (value == 0 && _cannotBeZeroSet.Contains(key))
			{
				throw new InvalidOperationException($"Cannot assign 0 to {key}.");
			}

			bool isKeyAssigned = _dictionary.TryGetValue(key, out int digit);
			if (isKeyAssigned && digit != value)
			{
				throw new InvalidOperationException($"{key} already assigned to {digit}; cannot reassign to {value}.");
			}
			else if (_dictionary.Values.Contains(value) && digit != value)
			{
				throw new InvalidOperationException($"{value} already assigned; cannot assign to second key.");
			}

			if (key != Placeholder)
			{
				_dictionary[key] = value;
			}
		}
	}

	public ICollection<char> Keys =>
		_dictionary.Keys;

	public ICollection<int> Values =>
		_dictionary.Values;

	public int Count =>
		_dictionary.Count;

	int ICollection<KeyValuePair<char, int>>.Count =>
			_dictionary.Count;

	public void Add(char key, int value) =>
		_dictionary.Add(key, value);

	public void Add(KeyValuePair<char, int> item) =>
		_dictionary.Add(item.Key, item.Value);

	public void Clear() =>
		_dictionary.Clear();

	public bool Contains(KeyValuePair<char, int> item) =>
		_dictionary.Contains(item);

	public bool ContainsKey(char key) =>
		_dictionary.ContainsKey(key);
	IEnumerator IEnumerable.GetEnumerator() =>
		_dictionary.GetEnumerator();

	public IEnumerator<KeyValuePair<char, int>> GetEnumerator() =>
		_dictionary.GetEnumerator();

	public bool Remove(char key) =>
		_dictionary.Remove(key);

	public bool Remove(KeyValuePair<char, int> item) =>
		_dictionary.Remove(item.Key);

	public bool TryGetValue(char key, [MaybeNullWhen(false)] out int value) =>
		_dictionary.TryGetValue(key, out value);

	public UInt128 Convert(string word)
	{
		StringBuilder builder = new();
		for (int i = 0; i < word.Length; i++)
		{
			builder = builder.Append(_dictionary[word[i]]);
		}

		return UInt128.Parse(builder.ToString());
	}

	public Dictionary<char, int> ToDictionary() =>
		_dictionary;

	public void CopyTo(KeyValuePair<char, int>[] array, int arrayIndex) =>
		array = _dictionary.Select(x => x)
			.ToArray();

	public bool IsReadOnly =>
		throw new NotImplementedException();
}
