using System;
using System.Collections.Generic;
using System.Linq;

public static class TreeBuilder
{
	public static Tree BuildTree(IEnumerable<TreeBuildingRecord> records) =>
		!records.Any()
			? throw new ArgumentException("Trees must have at least one record.", nameof(records))
			: records.CreateTrees()
				.BuildRelationships();

	private static IEnumerable<Tree> CreateTrees(this IEnumerable<TreeBuildingRecord> records)
	{
		int previousRecordId = -1;
		return records
			.OrderBy(record => record.RecordId)
			.Select(record => new Tree(record, ref previousRecordId))
			.ToList();
	}

	private static Tree BuildRelationships(this IEnumerable<Tree> trees)
	{
		Dictionary<int, Tree> treesById = trees.ToDictionary(x => x.Id, x => x);
		foreach (Tree tree in trees)
		{
			if (
					tree.Id != tree.ParentId
					&& treesById.TryGetValue(tree.ParentId, out Tree parent)
				)
			{
				parent.Children.Add(tree);
			}
		}

		return treesById[0];
	}
}

public record TreeBuildingRecord()
{
	public int ParentId { get; init; }
	public int RecordId { get; init; }

	public bool IsRoot =>
		RecordId == 0;

	public bool IsNotRoot =>
		!IsRoot;
}

public record Tree
{
	public Tree(TreeBuildingRecord record, ref int previousRecordId)
	{
		Id = record.RecordId;
		ParentId = record.ParentId;

		if (
			IsRootButHasOtherParent(record)
				|| HasInvalidParent(record)
				|| HasUnexpectedId(record, previousRecordId)
			)
		{
			throw new ArgumentException("Invalid record!", nameof(record));
		}

		++previousRecordId;
	}

	private static bool IsRootButHasOtherParent(TreeBuildingRecord record) =>
		record.IsRoot && record.ParentId != 0;

	private bool HasInvalidParent(TreeBuildingRecord record) =>
		record.IsNotRoot
			&& record.ParentId >= Id;
	private static bool HasUnexpectedId(TreeBuildingRecord record, int previousRecordId) =>
		record.IsNotRoot
			&& record.RecordId != previousRecordId + 1;

	public int Id { get; init; }

	public int ParentId { get; init; }

	public List<Tree> Children { get; init; } = new();

	public bool IsLeaf =>
		Children.Count == 0;
}
