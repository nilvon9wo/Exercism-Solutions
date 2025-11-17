using System;
using System.Collections.Generic;
using System.Linq;

public class Connect(string[] inputString)
{
	private readonly Board _board = Board.From(inputString);

	private static readonly Dictionary<Player, Func<Board, Player, bool>> _winningConditionByPlayer
		= new()
		{
			{ Player.White, IsVerticallyConnected },
			{ Player.Black, IsHorizontallyConnected }
		};

	public ConnectWinner Result()
	{
		foreach ((Player player, Func<Board, Player, bool> victoryCondition) in _winningConditionByPlayer)
		{
			if (victoryCondition(_board, player))
			{
				return player.To<ConnectWinner>();
			}
		}

		return ConnectWinner.None;
	}

	private static bool IsVerticallyConnected(Board board, Player player)
	{
		List<Cell> bottomEdge = board.GetBottomEdge();
		List<Cell> topEdge = board.GetTopEdge();
		return IsConnected(board, player, bottomEdge, topEdge);
	}

	private static bool IsHorizontallyConnected(Board board, Player player)
	{
		List<Cell> leftEdge = board.GetLeftEdge();
		List<Cell> rightEdge = board.GetRightEdge();
		return IsConnected(board, player, leftEdge, rightEdge);
	}

	private static bool IsConnected(
			Board board,
			Player player,
			List<Cell> startEdge,
			List<Cell> endEdge
		)
	{
		List<Cell> endCells = endEdge.BelongingTo(player);
		HashSet<HexCoordinates> visited = new();
		return startEdge.BelongingTo(player)
			.Any(startCell =>
				DepthFirstSearch(
					board,
					player,
					startCell.Coordinates,
					endCells,
					visited
				));
	}

	private static bool DepthFirstSearch(
			Board board,
			Player player,
			HexCoordinates currentCoordinates,
			List<Cell> endCells,
			HashSet<HexCoordinates> visited
		)
		=> endCells.Any(cell => cell.Coordinates.Equals(currentCoordinates))
			|| (visited.Add(currentCoordinates) && board.GetNeighbors(currentCoordinates)
				.Any(neighbor =>
					!visited.Contains(neighbor.Coordinates)
					&& neighbor.BelongsTo(player)
					&& DepthFirstSearch(board, player, neighbor.Coordinates, endCells, visited)
				));
}