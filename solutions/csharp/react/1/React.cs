using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

// ReSharper disable once CheckNamespace
public class Reactor
{
	// ReSharper disable once MemberCanBeMadeStatic.Global
	[SuppressMessage("Performance", "CA1822:Mark members as static", Justification = "Required by tests.")]
	public InputCell CreateInputCell(int value)
		=> new() { Value = value };

	// ReSharper disable once MemberCanBeMadeStatic.Global
	[SuppressMessage("Performance", "CA1822:Mark members as static", Justification = "Required by tests.")]
	public ComputeCell CreateComputeCell(IEnumerable<Cell> producers, Func<int[], int> compute)
	{
		Cell[] producersArray = producers as Cell[] ?? producers.ToArray();
		return new(
			producers as Cell[] ?? producersArray.ToArray(),
			_ =>
			{
				int[] intValues = producersArray.Select(producer => producer.Value.GetValueOrDefault())
					.ToArray();
				return compute(intValues);
			}
		);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public abstract class Cell
{
	public abstract int? Value { get; internal set; }
	public Action<object, object>? Changed { get; internal set; }
	public ICollection<ComputeCell> Dependents { get; } = new List<ComputeCell>();
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class ComputeCell : Cell
{
	private readonly IEnumerable<Cell> _producers;
	private readonly Func<int[], int?> _compute;
	private int? _value;

	public ComputeCell(IEnumerable<Cell> producers, Func<int[], int?> compute)
	{
		_producers = producers as Cell[] ?? producers.ToArray();
		_compute = compute;

		foreach (Cell producer in _producers)
		{
			producer.Changed += UpdateValue;
			producer.Dependents.Add(this);
		}

		UpdateValue(null, null);
	}

	public override int? Value
	{
		get
			=> _value;
		internal set
		{
			if (_value != value)
			{
				_value = value;
				Changed?.Invoke(this, (int)Value!);
			}
		}
	}

	public new EventHandler<int>? Changed
	{
		get;
		internal set;
	}

	private void UpdateValue(object? sender, object? value)
	{
		int[] producerValues = _producers
			.Where(producer => producer.Value != null)
			.Select(producer => producer.Value!.Value)
			.ToArray();

		int? newValue = _compute(producerValues);

		if (newValue != Value)
		{
			Value = newValue;
			foreach (ComputeCell dependent in Dependents)
			{
				dependent.UpdateValue(this, Value);
			}
		}
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

// ReSharper disable once ClassNeverInstantiated.Global

public class InputCell : Cell
{
	private int? _value;

	public override int? Value
	{
		get
			=> _value;
		internal set
		{
			if (_value != value)
			{
				_value = value;
				Changed?.Invoke(this, value!);
			}
		}
	}
}

//=======================================================================