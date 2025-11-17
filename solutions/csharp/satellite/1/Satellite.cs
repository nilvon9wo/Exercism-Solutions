#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050

public static class Satellite
{
    public static Tree? TreeFromTraversals(char[] preOrder, char[] inOrder) 
        => AreInconsistent(preOrder, inOrder)
            ? throw new ArgumentException("Traversals must be consistent.")
            : preOrder.HasDuplicates() 
                ? throw new ArgumentException("Traversals must not contain repeated items.")
                : BuildTree(preOrder, inOrder);

    private static bool AreInconsistent(char[] preOrder, char[] inOrder) 
        => !preOrder.OrderBy(c => c)
            .SequenceEqual(inOrder.OrderBy(c => c));

    private static Tree? BuildTree(char[] preOrder, char[] inOrder)
    {
        if (preOrder.Length == 0 || inOrder.Length == 0)
        {
            return null;
        }

        char rootValue = preOrder[0];
        int rootIndex = Array.IndexOf(inOrder, rootValue);

        char[] leftInOrder = inOrder[..rootIndex];
        char[] rightInOrder = inOrder[(rootIndex + 1)..];

        char[] leftPreOrder = preOrder[1..(1 + leftInOrder.Length)];
        char[] rightPreOrder = preOrder[(1 + leftInOrder.Length)..];

        return new(
                rootValue, 
                BuildTree(leftPreOrder, leftInOrder), 
                BuildTree(rightPreOrder, rightInOrder)
            );
    }
}

public record Tree(char Value, Tree? Left, Tree? Right);

internal static class CharArrayExtensions
{
    internal static bool HasDuplicates(this char[] chars)
        => chars.DistinctCount() != chars.Length;

    private static int DistinctCount(this char[] chars)
        => chars.Distinct()
            .Count();
}