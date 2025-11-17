using System;
using System.Collections;
using System.Collections.Generic;

// ReSharper disable once CheckNamespace

public class SimpleLinkedList<T> : IEnumerable<T>
{
	private LinkedListNode<T>? _head;

	public int Count { get; private set; }

	public SimpleLinkedList()
		=> Count = 0;

	public SimpleLinkedList(IEnumerable<T> values)
	{
		if (values is null)
		{
			throw new ArgumentNullException(nameof(values));
		}

		foreach (T? value in values)
		{
			Push(value);
		}
	}

	public void Push(T value)
	{
		_head = new(value) { Next = _head };
		Count++;
	}

	public T Pop()
	{
		if (Count == 0)
		{
			throw new InvalidOperationException("The list is empty.");
		}

		T? value = _head!.Value;
		_head = _head.Next;
		Count--;
		return value;
	}

	public IEnumerator<T> GetEnumerator()
	{
		LinkedListNode<T>? current = _head;
		while (current != null)
		{
			yield return current.Value;
			current = current.Next;
		}
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();
}

public class LinkedListNode<T>
{
	public T Value { get; }
	public LinkedListNode<T>? Next { get; init; }

	public LinkedListNode(T value)
		=> Value = value;
}