using System.Collections.Generic;
using System.Linq;

public class GradeSchool
{
	private readonly Dictionary<string, int> gradeByNames = new();

	public bool Add(string name, int grade)
	{
		if (!gradeByNames.ContainsKey(name))
		{
			gradeByNames[name] = grade;
			return true;
		}
		else
		{
			return false;
		}
	}

	public IEnumerable<string> Roster() =>
		gradeByNames
			.OrderBy(KeyValuePair =>
			{
				(string _, int grade) = KeyValuePair;
				return grade;
			})
			.ThenBy(KeyValuePair =>
			{
				(string name, int _) = KeyValuePair;
				return name;
			})
			.Select(KeyValuePair =>
			{
				(string name, int _) = KeyValuePair;
				return name;
			});

	public IEnumerable<string> Grade(int grade) =>
		gradeByNames
			.Where(KeyValuePair =>
			{
				(string _, int studentGrade) = KeyValuePair;
				return studentGrade == grade;
			})
			.Select(KeyValuePair =>
			{
				(string name, int _) = KeyValuePair;
				return name;
			})
			.Order();
}