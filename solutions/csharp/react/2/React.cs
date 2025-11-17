using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

// ReSharper disable once CheckNamespace
public sealed class Reactor
{
	[SuppressMessage("Performance", "CA1822:Mark members as static", Justification = "Required by test.")]
	// ReSharper disable once MemberCanBeMadeStatic.Global
	public InputCell CreateInputCell(int value)
		=> new(value);

	[SuppressMessage("Performance", "CA1822:Mark members as static", Justification = "Required by test.")]
	// ReSharper disable once MemberCanBeMadeStatic.Global
	public ComputeCell CreateComputeCell(IEnumerable<Cell> producers, Func<int[], int>? compute)
		=> new(producers.ToArray(), compute);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public abstract class Cell
{
	private int _value;

	public int Value
	{
		get
			=> _value;
		set
		{
			if (_value != value)
			{
				_value = value;
				OnChanged(value);
			}
		}
	}

	protected Cell(int value)
		=> _value = value;

	public EventHandler<int>? Changed { get; set; }

	private void OnChanged(int newValue)
		=> Changed?.Invoke(this, newValue);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public sealed class ComputeCell : Cell
{
	private readonly Cell[] _producers;
	private readonly Func<int[], int>? _compute;

	public ComputeCell(Cell[] producers, Func<int[], int>? compute)
		: base(ComputeValue(producers, compute))
	{
		_ = producers ?? throw new ArgumentNullException(nameof(producers));

		_producers = SetupProducerChangeCallbacks(producers);
		_compute = compute;

		InitializeEventHandler();
	}

	private void InitializeEventHandler()
	{
		SetupCascadingProducersChangedEvents();
		SetupRecomputation();
	}

	private EventHandler<int>? _producersChanged { get; set; }

	private void OnProducersChanged(int val)
		=> _producersChanged?.Invoke(this, val);

	private void RecomputeValue()
		=> Value = ComputeValue(_producers, _compute);

	private static int ComputeValue(IEnumerable<Cell> producers, Func<int[], int>? compute)
		=> compute?.Invoke(ProduceValues(producers))
		   ?? throw new ArgumentNullException(nameof(compute));

	private static int[] ProduceValues(IEnumerable<Cell> producers)
		=> producers.Select(each => each.Value)
			.ToArray();

	private Cell[] SetupProducerChangeCallbacks(Cell[] producers)
	{
		foreach (Cell producer in producers)
		{
			producer.Changed += (_, value)
				=> OnProducersChanged(value);
		}

		return producers;
	}

	private void SetupCascadingProducersChangedEvents()
		=> _producersChanged += (_, value)
			=>
		{
			foreach (Cell producer in _producers)
			{
				if (producer is ComputeCell computingProducer)
				{
					computingProducer.OnProducersChanged(value);
				}
			}
		};

	private void SetupRecomputation()
		=> _producersChanged += (_, _)
			=> RecomputeValue();
}

//=======================================================================

// ReSharper disable once CheckNamespace
public sealed class InputCell : Cell
{
	public InputCell(int value)
		: base(value)
	{
	}
}

//=======================================================================