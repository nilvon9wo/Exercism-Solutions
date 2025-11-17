using System;
using System.Linq;
using System.Reflection;

internal static class AttributeExtensions
{
	public static TValue GetValue<TAttribute, TValue>(this TAttribute attribute)
		where TAttribute : Attribute
		where TValue : struct
	{
		_ = attribute ?? throw new ArgumentNullException(nameof(attribute));
		PropertyInfo[] properties = attribute.GetType()
			.GetProperties()
			.Where(property => property.PropertyType == typeof(TValue))
			.ToArray();

		return properties.Length switch
		{
			1
				=> (TValue)properties[0]
					.GetValue(attribute),

			> 1
				=> throw new InvalidOperationException("Attribute value has multiple attributes of the specified type."),

			_
				=> throw new InvalidOperationException("Attribute value does not have an attribute of the specified type.")
		};
	}
}