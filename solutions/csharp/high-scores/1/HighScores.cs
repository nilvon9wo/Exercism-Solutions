using System.Collections.Generic;
using System.Linq;

public class HighScores
{
	private readonly List<int> _list;

	public HighScores(List<int> list) =>
		_list = list;

	public List<int> Scores() =>
		_list;

	public int Latest() =>
		_list.Last();

	public int PersonalBest() =>
		_list.Max();

	public List<int> PersonalTopThree()
	{
		List<int> listCopy = new(_list);
		listCopy.Sort((a, b) => b.CompareTo(a));
		return listCopy.Take(3)
			.ToList();
	}
}