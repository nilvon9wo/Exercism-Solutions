using System;
using System.Collections.Generic;
using System.Linq;

public static class TreeBuilder
{
	public static Tree BuildTree(IEnumerable<TreeBuildingRecord> records)
	{
		if (!records.Any())
		{
			throw new ArgumentException("Trees must have at least one record.", nameof(records));
		}

		List<Tree> trees = CreateTrees(records);
		return BuildRelationships(trees)
			.First(tree => tree.Id == 0);
	}

	private static List<Tree> CreateTrees(IEnumerable<TreeBuildingRecord> records)
	{
		int previousRecordId = -1;
		List<Tree> trees = records
			.OrderBy(record => record.RecordId)
			.Select(record => new Tree(record, ref previousRecordId))
			.OrderBy(x => x.Id)
			.ToList();
		return trees;
	}

	private static List<Tree> BuildRelationships(List<Tree> trees)
	{
		for (int i = 1; i < trees.Count; i++)
		{
			Tree tree = trees.First(x => x.Id == i);
			trees.First(x => x.Id == tree.ParentId)
				.Children
				.Add(tree);
		}

		return trees;
	}
}