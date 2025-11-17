using System;
using System.Diagnostics.CodeAnalysis;

// ReSharper disable once CheckNamespace
public static class ErrorHandling
{
	[SuppressMessage("Usage", "CA2201:Do not raise reserved exception types", Justification = "Required by tests.")]
	public static void HandleErrorByThrowingException()
		=> throw new("This is a custom exception.");

	public static int? HandleErrorByReturningNullableType(string input)
		=> int.TryParse(input, out int result)
			? result
			: null;

	[SuppressMessage("Design", "CA1021:Avoid out parameters", Justification = "Required by tests.")]
	public static bool HandleErrorWithOutParam(string input, out int result)
	{
		if (int.TryParse(input, out result))
		{
			return true;
		}

		result = 0;
		return false;
	}

	[SuppressMessage("Usage", "CA2201:Do not raise reserved exception types", Justification = "Required by tests.")]
	public static void DisposableResourcesAreDisposedWhenExceptionIsThrown(IDisposable disposableObject)
	{
		if (disposableObject is null)
		{
			throw new ArgumentNullException(nameof(disposableObject));
		}

		try
		{
			throw new("An exception occurred.");
		}
		finally
		{
			disposableObject.Dispose();
		}
	}
}