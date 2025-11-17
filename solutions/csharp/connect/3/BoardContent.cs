internal enum BoardContent
{
	[InputCharacter(' ')]
	Ignored,

	[InputCharacter('.')]
	Empty,

	[InputCharacter('X')]
	Black,

	[InputCharacter('O')]
	White
}