using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public class BinarySearchTree : IEnumerable<int>
{
	public BinarySearchTree(int value)
		=> Value = value;

	public BinarySearchTree(IEnumerable<int> values)
	{
		_ = values ?? throw new ArgumentNullException(nameof(values));
		List<int> valueList = values.ToList();
		Value = valueList.FirstOrDefault();
		foreach (int value in valueList.Skip(1))
		{
			_ = Add(value);
		}
	}

	public int Value { get; }

	public BinarySearchTree? Left { get; private set; }

	public BinarySearchTree? Right { get; private set; }

	private BinarySearchTree Add(int value)
	{
		if (value <= Value)
		{
			Left = Left == null
				? new BinarySearchTree(value)
				: Left.Add(value);
		}
		else
		{
			Right = Right == null
				? new BinarySearchTree(value)
				: Right.Add(value);
		}

		return this;
	}

	public IEnumerator<int> GetEnumerator()
	{
		if (Left != null)
		{
			foreach (int leftValue in Left)
			{
				yield return leftValue;
			}
		}

		yield return Value;

		if (Right != null)
		{
			foreach (int rightValue in Right)
			{
				yield return rightValue;
			}
		}
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();
}