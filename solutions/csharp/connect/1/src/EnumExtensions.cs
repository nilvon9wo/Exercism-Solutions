using System;
using System.Reflection;

public static class EnumExtensions
{
	public static T2 To<T2>(this Enum enumValue) where T2 : Enum
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		Type enumType = enumValue.GetType();
		string enumName2 = Enum.GetName(enumType, enumValue);
		return enumName2 != null
			? (T2)Enum.Parse(typeof(T2), enumName2)
			: throw new ArgumentException($"No matching enum value found in {typeof(T2)} for {enumValue}.");
	}

	public static T GetAttributeValue<T>(this Enum enumValue) where T : Attribute
	{
		_ = enumValue ?? throw new ArgumentNullException(nameof(enumValue));
		FieldInfo fieldInfo = enumValue.GetType()
			.GetField(enumValue.ToString());
		T[] attributes = (T[])fieldInfo
			.GetCustomAttributes(typeof(T), false);

		return attributes.Length switch
		{
			1
				=> attributes[0],

			> 1
				=> throw new InvalidOperationException("Enum value has multiple attributes of the specified type."),

			_
				=> throw new InvalidOperationException("Enum value does not have an attribute of the specified type.")
		};
	}
}