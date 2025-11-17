using System;
using System.Collections.Generic;
using System.Linq;

internal static class EnumUtilities
{
	public static Dictionary<TKey, TEnum> ToEnumByAttributeValue<TEnum, TAttribute, TKey>()
		where TEnum : Enum
		where TAttribute : Attribute
		where TKey : struct
		=> Enum.GetValues(typeof(TEnum))
			.Cast<TEnum>()
			.ToDictionary(
				enumValue
					=> enumValue.GetAttributeValue<TAttribute>()
						.GetValue<TAttribute, TKey>(),

				enumValue
					=> enumValue
			);
}
