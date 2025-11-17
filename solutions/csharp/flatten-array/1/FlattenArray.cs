using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class FlattenArray
{
	public static IEnumerable Flatten(IEnumerable input)
	{
		_ = input ?? throw new ArgumentNullException(nameof(input));
		foreach (object? item in FlattenInternal(input))
		{
			if (item != null)
			{
				yield return item;
			}
		}
	}

	private static IEnumerable FlattenInternal(IEnumerable input)
		=> input.Cast<object?>()
			.SelectMany(
				item => (IEnumerable<object?>)FlattenInternal(item!)
			);

	private static IEnumerable FlattenInternal(object item)
	{
		if (item is IEnumerable subList and not string)
		{
			foreach (object? subItem in FlattenInternal(subList))
			{
				yield return subItem;
			}
		}
		else
		{
			yield return item;
		}
	}
}