using System;

public class Orm
{
	private readonly Database _database;

	public Orm(Database database)
	{
		_database = database;
	}

	public void Write(string data)
	{
		_database.BeginTransaction();
		try
		{
			_database.Write(data);
			_database.EndTransaction();
		}
		catch (InvalidOperationException)
		{
			_database.Dispose();
			throw;
		}
	}

	public bool WriteSafely(string data)
	{
		try
		{
			Write(data);
			return true;
		}
		catch (InvalidOperationException)
		{
			return false;
		}
	}
}
