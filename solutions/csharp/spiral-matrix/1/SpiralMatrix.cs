using System;

public class SpiralMatrix
{
	public static int[,] GetMatrix(int size)
	{
		double maxValue = Math.Pow(size, 2);
		int[,] result = new int[size, size];
		if (size == 0)
		{
			return result;
		}

		Coordinate coordinate = new();
		Direction direction = Direction.Right;
		int i = 1;
		do
		{
			result[coordinate.X, coordinate.Y] = i;
			_ = coordinate.Next(result, ref direction);
			i++;
		}
		while (i <= maxValue);

		return result;
	}
}

public class Coordinate
{
	public int X = 0;
	public int Y = 0;

	public Coordinate()
	{
	}
	public Coordinate(int x, int y)
	{
		X = x;
		Y = y;
	}

	public Coordinate Next(int[,] result, ref Direction direction)
	{
		switch (direction)
		{
			case Direction.Right:
				Y++;
				if (CantBeUsed(result))
				{
					Y--;
					X++;
					direction = direction.Next();
				}

				return this;

			case Direction.Down:
				X++;
				if (CantBeUsed(result))
				{
					X--;
					Y--;
					direction = direction.Next();
				}

				return this;

			case Direction.Left:
				Y--;
				if (CantBeUsed(result))
				{
					Y++;
					X--;
					direction = direction.Next();
				}

				return this;

			case Direction.Up:
				X--;
				if (CantBeUsed(result))
				{
					X++;
					Y++;
					direction = direction.Next();
				}

				return this;

			default:
				throw new ArgumentException("Invalid direction", nameof(direction));
		}
	}

	private bool CantBeUsed(int[,] result) =>
		X < 0
			|| Y < 0
			|| X >= result.GetLength(1)
			|| Y >= result.GetLength(0)
			|| result[X, Y] != 0;
}

public enum Direction
{
	Right,
	Down,
	Left,
	Up,
}

public static class DirectionExtensions
{
	public static Direction Next(this Direction direction) =>
		direction switch
		{
			Direction.Right => Direction.Down,
			Direction.Down => Direction.Left,
			Direction.Left => Direction.Up,
			Direction.Up => Direction.Right,
			_ => throw new NotImplementedException(),
		};
}