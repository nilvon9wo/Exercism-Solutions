using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public class CustomSet
{
	private readonly HashSet<int> _elements;

	public CustomSet(params int[] values)
		=> _elements = new(values);

	public CustomSet Add(int value)
	{
		_ = _elements.Add(value);
		return this;
	}

	public bool Empty()
		=> _elements.Count == 0;

	public bool Contains(int value)
		=> _elements.Contains(value);

	public bool Subset(CustomSet right)
	{
		_ = right ?? throw new ArgumentNullException(nameof(right));
		return _elements.IsSubsetOf(right._elements);
	}

	public bool Disjoint(CustomSet right)
	{
		_ = right ?? throw new ArgumentNullException(nameof(right));
		return right._elements.All(element => !_elements.Contains(element));
	}

	public CustomSet Intersection(CustomSet right)
	{
		_ = right ?? throw new ArgumentNullException(nameof(right));
		HashSet<int> intersection = new(_elements);
		intersection.IntersectWith(right._elements);
		return new(intersection.ToArray());
	}

	public CustomSet Difference(CustomSet right)
	{
		_ = right ?? throw new ArgumentNullException(nameof(right));
		HashSet<int> difference = new(_elements);
		difference.ExceptWith(right._elements);
		return new(difference.ToArray());
	}

	public CustomSet Union(CustomSet right)
	{
		_ = right ?? throw new ArgumentNullException(nameof(right));
		HashSet<int> union = new(_elements);
		union.UnionWith(right._elements);
		return new(union.ToArray());
	}

	public override bool Equals(object? obj)
		=> obj is CustomSet other && _elements.SetEquals(other._elements);

	public override int GetHashCode()
		=> _elements.GetHashCode();
}