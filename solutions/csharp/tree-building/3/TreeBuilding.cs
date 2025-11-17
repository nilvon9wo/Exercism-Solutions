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