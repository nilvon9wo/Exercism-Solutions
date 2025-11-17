using System;

[AttributeUsage(AttributeTargets.Field, AllowMultiple = false)]
internal sealed class InputCharacterAttribute : Attribute
{
	public InputCharacterAttribute(char character)
		=> Character = character;

	public char Character { get; }
}