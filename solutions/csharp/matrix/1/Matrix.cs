using System.Linq;

public class Matrix
{
	private readonly int[][] _matrix;

	public Matrix(string matrix) =>
		_matrix = matrix.Split('\n')
			.Select(ExtractColumns)
			.ToArray();

	private static int[] ExtractColumns(string columnString) =>
			columnString.Split(' ')
				.Select(int.Parse)
				.ToArray();

	public int[] Row(int row) =>
		_matrix[row - 1];

	public int[] Column(int column) =>
		_matrix.Select(row => row[column - 1])
			.ToArray();
}