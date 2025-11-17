using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public class Graph
	: IEnumerable<Graph>,
		IEquatable<Graph>
{
	private readonly List<Node> _nodes = new();
	private readonly List<Edge> _edges = new();
	private readonly List<Attr> _attributes = new();

	public IEnumerable<Node> Nodes
		=> _nodes;

	public IEnumerable<Edge> Edges
		=> _edges;

	public IEnumerable<Attr> Attrs
		=> _attributes;

	public IEnumerator<Graph> GetEnumerator()
	{
		yield return this;
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();

	#region Equality members

	public Graph Add(params object[] objects)
	{
		if (objects is null)
		{
			throw new ArgumentNullException(nameof(objects));
		}

		for (int i = 0; i < objects.Length; i++)
		{
			object obj = objects[i];
			switch (obj)
			{
				case Node node:
					_nodes.Add(node);
					break;
				case Edge edge:
					_edges.Add(edge);
					break;
				case Attr attr:
					_attributes.Add(attr);
					break;
				case string key when ((i + 1) < objects.Length) && objects[i + 1] is string value:
					_attributes.Add(new(key, value));
					i++;
					break;
				default:
					throw new ArgumentException($"Invalid object type: {obj.GetType()}");
			}
		}

		return this;
	}

	public bool Equals(Graph? other)
		=> (other != null) &&
		   (ReferenceEquals(this, other) ||
			(Nodes.SequenceEqual(other.Nodes) && Edges.SequenceEqual(other.Edges) && Attrs.SequenceEqual(other.Attrs)));

	public override bool Equals(object? obj)
		=> obj is Graph other && Equals(other);

	public override int GetHashCode()
		=> HashCode.Combine(Nodes, Edges, Attrs);

	#endregion
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Attr
	: IEnumerable<Attr>,
		IEquatable<Attr>
{
	private string _key { get; }

	private string _value { get; }

	public Attr(string key, string value)
	{
		_key = key;
		_value = value;
	}

	public IEnumerator<Attr> GetEnumerator()
	{
		yield break;
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();

	#region Equality members

	public bool Equals(Attr? other)
		=> (other != null) &&
		   (_key == other._key) &&
		   (_value == other._value);

	public override bool Equals(object? obj)
		=> obj is Attr other && Equals(other);

	public override int GetHashCode()
		=> HashCode.Combine(_key, _value);

	#endregion
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Edge
	: IEnumerable<Edge>,
		IEquatable<Edge>
{
	private string _source { get; }

	private string _target { get; }

	private Dictionary<string, string> _attributes { get; } = new();

	public Edge(string source, string target)
	{
		_source = source;
		_target = target;
	}

	public IEnumerator<Edge> GetEnumerator()
	{
		yield break;
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();

	public bool Equals(Edge? other)
		=> (other != null) &&
		   (_source == other._source) &&
		   (_target == other._target) &&
		   DictionaryEquals(_attributes, other._attributes);

	private static bool DictionaryEquals(
		IReadOnlyDictionary<string, string> dictionary1,
		IReadOnlyDictionary<string, string> dictionary2
	)
		=> (dictionary1.Count == dictionary2.Count) &&
		   dictionary1.All(
			   kvp =>
				   dictionary2.TryGetValue(kvp.Key, out string? value) && (value == kvp.Value)
		   );

	public override bool Equals(object? obj)
		=> obj is Edge other && Equals(other);

	public override int GetHashCode()
		=> HashCode.Combine(_source, _target, _attributes);

	public Edge Add(params string[] values)
	{
		if (values is null)
		{
			throw new ArgumentNullException(nameof(values));
		}

		if ((values.Length % 2) != 0)
		{
			throw new ArgumentException("The number of values must be even.");
		}

		for (int i = 0; i < values.Length; i += 2)
		{
			string key = values[i];
			_attributes[key] = values[i + 1];
		}

		return this;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Node
	: IEnumerable<Node>,
		IEquatable<Node>
{
	private string _id { get; }

	private Dictionary<string, string> _attributes { get; } = new();

	public Node(string id)
		=> _id = id;

	public IEnumerator<Node> GetEnumerator()
	{
		yield break;
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();

	#region Equality members

	public bool Equals(Node? other)
		=> (other != null) && (_id == other._id) && DictionaryEquals(_attributes, other._attributes);

	private static bool DictionaryEquals(
		IReadOnlyDictionary<string, string> dictionary1,
		IReadOnlyDictionary<string, string> dictionary2
	)
		=> (dictionary1.Count == dictionary2.Count) &&
		   dictionary1.All(
			   kvp =>
				   dictionary2.TryGetValue(kvp.Key, out string? value) && (value == kvp.Value)
		   );

	public override bool Equals(object? obj)
		=> obj is Node other && Equals(other);

	public override int GetHashCode()
		=> HashCode.Combine(_id, _attributes);

	#endregion

	public Node Add(params string[] values)
	{
		if (values is null)
		{
			throw new ArgumentNullException(nameof(values));
		}

		if ((values.Length % 2) != 0)
		{
			throw new ArgumentException("The number of values must be even.");
		}

		for (int i = 0; i < values.Length; i += 2)
		{
			string key = values[i];
			_attributes[key] = values[i + 1];
		}

		return this;
	}
}

//=======================================================================