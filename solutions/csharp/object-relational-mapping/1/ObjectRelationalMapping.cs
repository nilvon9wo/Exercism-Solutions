using System;

public class Orm : IDisposable
{
	private readonly Database _database;

	public Orm(Database database) =>
		_database = database;

	public void Begin() =>
		_database.BeginTransaction();

	public void Write(string data)
	{
		try
		{
			_database.Write(data);
		}
		catch (InvalidOperationException)
		{
			Dispose();
		}
	}

	public void Commit()
	{
		try
		{
			_database.EndTransaction();
		}
		catch (InvalidOperationException)
		{
			Dispose();
		}
	}

	public void Dispose() =>
		_database.Dispose();
}
