using System;

[AttributeUsage(AttributeTargets.Field, AllowMultiple = false)]
internal sealed class InputCharacterAttribute(char character) : Attribute
{
	public char Character { get; } = character;
}