using System.Collections.Generic;
using System.Linq;

public static class HashSetExtensions
{
	public static T Shift<T>(this HashSet<T> values)
	{
		T value = values.First();
		_ = values.Remove(value);
		return value;
	}
}
