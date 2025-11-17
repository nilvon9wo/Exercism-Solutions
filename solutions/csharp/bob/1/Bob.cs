using System.Linq;

public static class Bob
{
	public static string Response(string statement)
	{
		statement = statement.Trim();
		if (string.IsNullOrWhiteSpace(statement))
		{
			return "Fine. Be that way!";
		}

		bool wasYelled = IsYelled(statement);
		bool wasQuestion = IsQuestion(statement);
		bool wasYelledQuestion = wasYelled && wasQuestion;

		return statement switch
		{
			_ when wasYelledQuestion
				=> "Calm down, I know what I'm doing!",

			_ when wasYelled
				=> "Whoa, chill out!",

			_ when wasQuestion
				=> "Sure.",

			_ => "Whatever.",
		};
	}

	private static bool IsYelled(string statement)
		=> statement.Any(char.IsLetter)
			&& statement == statement.ToUpperInvariant();

	private static bool IsQuestion(string statement)
		=> '?' == statement.Last();
}