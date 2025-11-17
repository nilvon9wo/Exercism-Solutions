using System;

// ReSharper disable once CheckNamespace
public class Deque<T>
{
	private Node<T>? _head;

	private Node<T>? _tail;

	public void Push(T value)
	{
		Node<T> newNode = new(value);

		if (_tail == null)
		{
			_head = newNode;
			_tail = newNode;
		}
		else
		{
			newNode.Previous = _tail;
			_tail.Next = newNode;
			_tail = newNode;
		}
	}

	public T Pop()
	{
		if (_tail == null)
		{
			throw new InvalidOperationException("Deque is empty.");
		}

		T value = _tail.Value;

		if (_tail.Previous == null)
		{
			_head = null;
			_tail = null;
		}
		else
		{
			_tail = _tail.Previous;
			_tail.Next = null;
		}

		return value;
	}

	public void Unshift(T value)
	{
		Node<T> newNode = new(value);

		if (_head == null)
		{
			_head = newNode;
			_tail = newNode;
		}
		else
		{
			newNode.Next = _head;
			_head.Previous = newNode;
			_head = newNode;
		}
	}

	public T Shift()
	{
		if (_head == null)
		{
			throw new InvalidOperationException("Deque is empty.");
		}

		T value = _head.Value;

		if (_head.Next == null)
		{
			_head = null;
			_tail = null;
		}
		else
		{
			_head = _head.Next;
			_head.Previous = null;
		}

		return value;
	}
}

// ReSharper disable once CheckNamespace

internal sealed class Node<T>
{
	public T Value { get; }
	public Node<T>? Next { get; set; }
	public Node<T>? Previous { get; set; }

	public Node(T value)
		=> Value = value;
}