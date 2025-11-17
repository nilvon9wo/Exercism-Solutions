using System;
using System.Collections.Generic;

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
