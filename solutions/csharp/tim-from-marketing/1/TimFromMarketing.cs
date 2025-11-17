using System.Collections.Generic;
using System.Linq;

internal static class Badge
{
	private const string _defaultDepartment = "OWNER";

	public static string Print(int? id, string name, string? department)
	{
		string idPart = (id != null)
			? $"[{id}]"
			: string.Empty;
		string namePart = (!string.IsNullOrEmpty(name))
			? name
			: string.Empty;
		string departmentPart = (!string.IsNullOrEmpty(department))
			? department.ToUpperInvariant()
			: _defaultDepartment;
		IEnumerable<string> parts = new List<string> { idPart, namePart, departmentPart }
			.Where(x => !string.IsNullOrEmpty(x));
		return string.Join(" - ", parts);
	}
}
