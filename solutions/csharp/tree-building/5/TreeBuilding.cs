using System;
using System.Collections.Generic;
using System.Linq;

public static class TreeBuilder
{
	public static Tree BuildTree(IEnumerable<TreeBuildingRecord> records) =>
		!records.Any()
			? throw new ArgumentException("Trees must have at least one record.", nameof(records))
			: records.CreateTree();

	private static Tree CreateTree(this IEnumerable<TreeBuildingRecord> records)
	{
		int previousRecordId = -1;
		return records
			.OrderBy(record => record.RecordId)
			.Aggregate(
				new Dictionary<int, Tree>(),
				(treeByIds, record) =>
				{
					Tree tree = new(record, ref previousRecordId);
					treeByIds[tree.Id] = tree;
					if (
							record.IsNotRoot
							&& treeByIds.TryGetValue(tree.ParentId, out Tree parent)
						)
					{
						parent.Children.Add(tree);
					}

					return treeByIds;
				})
			[TreeBuildingRecord.RootId];
	}
}

public record TreeBuildingRecord()
{
	public const int RootId = 0;

	public int ParentId { get; init; }
	public int RecordId { get; init; }

	public bool IsRoot =>
		RecordId == RootId;

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
