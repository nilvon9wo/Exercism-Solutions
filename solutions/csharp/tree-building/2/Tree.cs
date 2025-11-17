using System;
using System.Collections.Generic;

public record Tree
{
	public Tree(TreeBuildingRecord record, ref int previousRecordId)
	{
		Id = record.RecordId;
		ParentId = record.ParentId;

		if ((Id == 0 && ParentId != 0) ||
			(Id != 0 && ParentId >= Id) ||
			(Id != 0 && Id != previousRecordId + 1))
		{
			throw new ArgumentException("Invalid record!", nameof(record));
		}

		++previousRecordId;
	}

	public int Id { get; init; }

	public int ParentId { get; init; }

	public List<Tree> Children { get; init; } = new();

	public bool IsLeaf =>
		Children.Count == 0;
}
