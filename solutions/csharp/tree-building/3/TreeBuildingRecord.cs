public record TreeBuildingRecord()
{
	public int ParentId { get; init; }
	public int RecordId { get; init; }

	public bool IsRoot =>
		RecordId == 0;

	public bool IsNotRoot =>
		!IsRoot;
}
