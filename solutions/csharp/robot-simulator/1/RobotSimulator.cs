using System;

// ReSharper disable once CheckNamespace
public enum Direction
{
	North,
	East,
	South,
	West,
}

// ReSharper disable once CheckNamespace
public class RobotSimulator
{
	internal Direction Direction { get; private set; }
	internal int X { get; private set; }
	internal int Y { get; private set; }

	public RobotSimulator(Direction direction, int x, int y)
	{
		Direction = direction;
		X = x;
		Y = y;
	}

	public void Move(string instructions)
	{
		if (string.IsNullOrWhiteSpace(instructions))
		{
			throw new ArgumentException(
				$"'{nameof(instructions)}' cannot be null or whitespace.",
				nameof(instructions)
			);
		}

		foreach (char instruction in instructions)
		{
			switch (instruction)
			{
				case 'R':
					TurnRight();
					break;
				case 'L':
					TurnLeft();
					break;
				case 'A':
					Advance();
					break;
				default:
					throw new ArgumentException($"Invalid instruction: {instruction}");
			}
		}
	}

	private void TurnRight()
		=> Direction = Direction switch
		{
			Direction.North => Direction.East,
			Direction.East => Direction.South,
			Direction.South => Direction.West,
			Direction.West => Direction.North,
			_ => Direction,
		};

	private void TurnLeft()
		=> Direction = Direction switch
		{
			Direction.North => Direction.West,
			Direction.West => Direction.South,
			Direction.South => Direction.East,
			Direction.East => Direction.North,
			_ => Direction,
		};

	private void Advance()
	{
		switch (Direction)
		{
			case Direction.North:
				Y++;
				break;
			case Direction.East:
				X++;
				break;
			case Direction.South:
				Y--;
				break;
			case Direction.West:
				X--;
				break;
			default:
				throw new ArgumentOutOfRangeException();
		}
	}
}