using System.Collections.Generic;
using System.Linq;

internal static class EnumerableExtensions
{
	public static List<Cell> BelongingTo(this IEnumerable<Cell> cells, Player player)
		=> cells.Where(c => c.Content == player.To<BoardContent>())
			.ToList();
}
