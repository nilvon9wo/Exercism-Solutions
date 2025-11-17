using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

// ReSharper disable once CheckNamespace

public static class Pov
{
	public static Tree FromPov(Tree tree, string from)
	{
		_ = tree ?? throw new ArgumentNullException(nameof(tree));
		Tree clone = tree.Clone();
		List<Tree> path = clone.FindPath(from)
			.ToList();

		return path.Count == 1
			? clone
			: RerootPath(path);
	}

	private static Tree RerootPath(IReadOnlyList<Tree> path)
		=> path.Skip(1)
			.Aggregate(
				path[0],
				(currentRoot, newRoot) =>
				{
					currentRoot.Children = currentRoot.Children
						.Where(child => child.Value != newRoot.Value)
						.ToList();
					newRoot.Children.Add(currentRoot);
					return newRoot;
				}
			);

	public static IEnumerable<string> PathTo(string from, string to, Tree tree)
	{
		_ = tree ?? throw new ArgumentNullException(nameof(tree));
		return tree.FindPathValues(from)
			.Merge(tree.FindPathValues(to));
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

public class Tree
{
	public readonly string Value;
	public ICollection<Tree> Children;

	public Tree(string value, params Tree[] children)
	{
		Value = value;
		Children = children.ToList();
	}

	private const bool _success = true;
	private const bool _failure = false;

	public Tree Clone()
	{
		Tree[] children = Children.Select(child => child.Clone())
			.ToArray();
		return new(Value, children);
	}

	private IEnumerable<Tree> InOrderTraversal()
	{
		yield return this;

		foreach (Tree child in Children.Sorted())
		{
			foreach (Tree tree in child.InOrderTraversal())
			{
				yield return tree;
			}
		}
	}

	// ReSharper disable once ReturnTypeCanBeEnumerable.Global
	public ICollection<Tree> FindPath(string to)
	{
		List<Tree> path = new();
		return !TryBuildPathToChild(this, to, path)
			? throw new ArgumentException($"'{to}' does not exist in this tree.")
			: path;
	}

	private static bool TryBuildPathToChild(Tree node, string to, ICollection<Tree> path)
	{
		path.Add(node);
		if (node.Value == to)
		{
			return _success;
		}

		if (CanFindPathToChild(node, to, path))
		{
			return _success;
		}

		_ = path.Remove(node);
		return _failure;
	}

	private static bool CanFindPathToChild(Tree node, string to, ICollection<Tree> path)
		=> node.Children.Any(child => TryBuildPathToChild(child, to, path));

	[SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "<Pending>")]
	public List<string> FindPathValues(string targetValue)
		=> FindPath(targetValue)
			.Select(tree => tree.Value)
			.ToList();

	public override string ToString()
	{
		IEnumerable<string> trees = InOrderTraversal()
			.Select(tree => tree.Value);
		return string.Join(" ", trees);
	}

	public override bool Equals(object? obj)
		=> obj is Tree other && (ToString() == other.ToString());

	public override int GetHashCode()
		=> ToString()
			.GetHashCode(StringComparison.InvariantCulture);
}

//=======================================================================

// ReSharper disable once CheckNamespace

public static class EnumerableExtensions
{
	public static IEnumerable<Tree> Sorted(this IEnumerable<Tree> trees)
		=> trees.OrderBy(tree => tree.Value, StringComparer.Ordinal);

	public static IEnumerable<string> Merge(
		this IEnumerable<string> fromPath,
		IEnumerable<string> toPath,
		string commonRoot
	)
	{
		List<string> mergedPath = new();
		mergedPath.AddRange(fromPath);
		mergedPath.Add(commonRoot);
		mergedPath.AddRange(toPath);
		return mergedPath;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class ListExtensions
{
	// ReSharper disable once SuggestBaseTypeForParameter
	internal static IEnumerable<string> Merge(this List<string> fromPath, List<string> toPath)
	{
		string commonRoot = ExtractCommonRoot(fromPath, toPath);
		return fromPath.AsEnumerable()
			.Reverse()
			.Merge(toPath, commonRoot);
	}

	private static string ExtractCommonRoot(IList<string> fromPath, IList<string> toPath)
	{
		string commonRoot = string.Empty;
		while (fromPath[0] == toPath[0])
		{
			commonRoot = fromPath[0];
			fromPath.RemoveAt(0);
			toPath.RemoveAt(0);

			if ((fromPath.Count == 0)
				|| (toPath.Count == 0))
			{
				break;
			}
		}

		return commonRoot;
	}
}

//=======================================================================