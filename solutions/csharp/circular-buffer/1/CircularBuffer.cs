using System;
using System.Collections.Generic;
using System.Linq;

public class CircularBuffer<T>
{
	private readonly int _capacity;

	public CircularBuffer(int capacity) =>
		_capacity = capacity;

	private Dictionary<DateTime, T> _tByTimestamp = new();

	private DateTime _earliestKey =>
		_tByTimestamp.Keys.Min();

	private bool _isFull =>
		_tByTimestamp.Count >= _capacity;

	public T Read()
	{
		if (!_tByTimestamp.Any())
		{
			throw new InvalidOperationException("No data.");
		}
		else
		{
			DateTime earliestTimestamp = _earliestKey;
			T earliestValue = _tByTimestamp[earliestTimestamp];
			_ = _tByTimestamp.Remove(earliestTimestamp);
			return earliestValue;
		}
	}

	public void Write(T value) =>
		_tByTimestamp[DateTime.UtcNow] = _isFull
			? throw new InvalidOperationException("Full.")
			: value;

	public void Overwrite(T value)
	{
		if (_isFull)
		{
			_ = _tByTimestamp.Remove(_earliestKey);
		}

		Write(value);
	}

	public void Clear() =>
		_tByTimestamp = new();
}