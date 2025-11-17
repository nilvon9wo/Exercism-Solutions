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
		=> _nodes
			.OrderBy(x => x.Id)
			.ToArray();

	public IEnumerable<Edge> Edges
		=> _edges.ToArray();

	public IEnumerable<Attr> Attrs
		=> _attributes.ToArray();

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

	public bool Equals(Graph other)
		=> (other != null) &&
		   Nodes.SequenceEqual(other.Nodes) &&
		   Edges.SequenceEqual(other.Edges) &&
		   Attrs.SequenceEqual(other.Attrs);

	public override bool Equals(object obj)
		=> (obj != null) && (GetType() == obj.GetType()) && Equals(obj as Graph);

	public override int GetHashCode()
	{
		unchecked
		{
			int hash = Nodes.Aggregate(17, (current, node) => (current * 23) + node.GetHashCode());
			hash = Edges.Aggregate(hash, (current, edge) => (current * 23) + edge.GetHashCode());
			return Attrs.Aggregate(hash, (current, attr) => (current * 23) + attr.GetHashCode());
		}
	}

	#endregion

	public override string ToString()
	{
		string nodeString = string.Join(", ", Nodes.Select(n => n.ToString()));
		string edgeString = string.Join(", ", Edges.Select(e => e.ToString()));
		string attrString = string.Join(", ", Attrs.Select(a => a.ToString()));
		return $"Graph (Nodes: {nodeString}, Edges: {edgeString}, Attributes: {attrString})";
	}
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

	public bool Equals(Attr other)
		=> (other != null) && (_key == other._key) && (_value == other._value);

	public override bool Equals(object obj)
		=> Equals(obj as Attr);

	public override int GetHashCode()
		=> HashCode.Combine(_key, _value);

	#endregion

	public override string ToString()
		=> $"Attr (Key: {_key}, Value: {_value})";
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

	private const StringComparison _invariantCulture = StringComparison.InvariantCulture;

	public IEnumerator<Edge> GetEnumerator()
	{
		yield break;
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();

	public bool Equals(Edge other)
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
				   dictionary2.TryGetValue(kvp.Key, out string value) && (value == kvp.Value)
		   );

	public override bool Equals(object obj)
		=> obj is Edge other && Equals(other);

	public override int GetHashCode()
	{
		unchecked
		{
			int hash = 17;
			hash = (hash * 23) + _source.GetHashCode(_invariantCulture);
			hash = (hash * 23) + _target.GetHashCode(_invariantCulture);
			hash = _attributes.Aggregate(
				hash,
				(currentHash, kvp) =>
				{
					int keyHash = kvp.Key.GetHashCode(_invariantCulture);
					int valueHash = kvp.Value.GetHashCode(_invariantCulture);
					return (currentHash * 23) + (keyHash * 23) + valueHash;
				}
			);
			return hash;
		}
	}

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

	public override string ToString()
	{
		string attributesString = string.Join(", ", _attributes.Select(kv => $"{kv.Key}=\"{kv.Value}\""));
		return $"Edge (Source: {_source}, Target: {_target}, Attributes: {attributesString})";
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Node
	: IEnumerable<Node>,
		IEquatable<Node>
{
	internal string Id { get; }

	private Dictionary<string, string> _attributes { get; } = new();

	public Node(string id)
		=> Id = id;

	private const StringComparison _invariantCulture = StringComparison.InvariantCulture;

	public IEnumerator<Node> GetEnumerator()
	{
		yield break;
	}

	IEnumerator IEnumerable.GetEnumerator()
		=> GetEnumerator();

	#region Equality members

	public bool Equals(Node other)
		=> (other != null) && (Id == other.Id) && DictionaryEquals(_attributes, other._attributes);

	private static bool DictionaryEquals(
		IReadOnlyDictionary<string, string> dictionary1,
		IReadOnlyDictionary<string, string> dictionary2
	)
		=> (dictionary1.Count == dictionary2.Count) &&
		   dictionary1.All(
			   kvp =>
				   dictionary2.TryGetValue(kvp.Key, out string value) && (value == kvp.Value)
		   );

	public override bool Equals(object obj)
		=> obj is Node other && Equals(other);

	public override int GetHashCode()
	{
		unchecked
		{
			int hash = 17;
			hash = (hash * 23) + Id.GetHashCode(_invariantCulture);
			hash = _attributes.Aggregate(
				hash,
				(currentHash, kvp) =>
				{
					int keyHash = kvp.Key.GetHashCode(_invariantCulture);
					int valueHash = kvp.Value.GetHashCode(_invariantCulture);
					return (currentHash * 23) + (keyHash * 23) + valueHash;
				}
			);
			return hash;
		}
	}

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

	public override string ToString()
		=> _attributes?.Any() == true
			? $"Node({Id} {{ {string.Join(", ", _attributes.Select(kv => $"{kv.Key}=\"{kv.Value}\""))} }})"
			: $"Node({Id})";
}

//=======================================================================