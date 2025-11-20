#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
public class RelativeDistance(Dictionary<string, string[]> familyTree)
{
    private Dictionary<string, List<string>>? _adjacencyList;
    private Dictionary<string, List<string>> AdjacencyList
    {
        get
        {
            _adjacencyList ??= familyTree
                .SelectMany(GetAllRelationshipsForFamily)
                .SelectMany(x => x)
                .GroupBy(x => x.Parent)
                .ToDictionary(
                    grouping => grouping.Key,
                    grouping => grouping.Select(x => x.Child)
                        .ToList()
                );

            return _adjacencyList;
        }
    }

    private static IEnumerable<FamilyRelation>[] GetAllRelationshipsForFamily(KeyValuePair<string, string[]> kvp)
        =>
        [
            GetParentChildRelationships(kvp.Key, kvp.Value),
            GetChildParentRelationships(kvp.Key, kvp.Value),
            GetSiblingRelationships(kvp.Value),
            GetReverseSiblingRelationships(kvp.Value)
        ];

    private static IEnumerable<FamilyRelation> GetParentChildRelationships(string parent, string[] children)
        => children.Select(child => new FamilyRelation(parent, child));

    private static IEnumerable<FamilyRelation> GetChildParentRelationships(string parent, string[] children)
        => children.Select(child => new FamilyRelation(child, parent));

    private static IEnumerable<FamilyRelation> GetSiblingRelationships(string[] children)
        => children.SelectMany((child1, i) =>
            children.Skip(i + 1)
                .Select(child2 => new FamilyRelation(child1, child2)));

    private static IEnumerable<FamilyRelation> GetReverseSiblingRelationships(string[] children)
        => children.SelectMany((child1, i) =>
            children.Skip(i + 1)
                .Select(child2 => new FamilyRelation(child2, child1)));

    public int DegreeOfSeparation(string personA, string personB)
        => personA == personB
            ? 0
            : BfsSearcher.FindShortestPath(AdjacencyList, personA, personB);
}

#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
internal static class BfsSearcher
{
    private const int NotFound = -1;
    private const int ContinueSearch = -2;

    public static int FindShortestPath(
            Dictionary<string, List<string>> adjacencyList,
            string start,
            string target
        )
    {
        (Queue<Relation> queue, HashSet<string> visited) = InitializeBfsState(start);
        return Enumerable.Range(0, int.MaxValue)
            .TakeWhile(_ => queue.Count > 0)
            .Select(_ => ProcessNextRelation(queue, visited, target, adjacencyList))
            .FirstOrDefault(result => result != ContinueSearch, NotFound);
    }

    private static (Queue<Relation> queue, HashSet<string> visited) InitializeBfsState(string start)
    {
        Queue<Relation> queue = new();
        HashSet<string> visited = [];

        queue.Enqueue(new(start, 0));
        visited.Add(start);

        return (queue, visited);
    }

    private static int ProcessNextRelation(
        Queue<Relation> queue,
        HashSet<string> visited,
        string target,
        Dictionary<string, List<string>> adjacencyList)
    {
        Relation current = queue.Dequeue();
        if (IsTargetFound(current, target))
        {
            return current.Degree;
        }

        EnqueueUnvisitedNeighbors(current, visited, queue, adjacencyList);
        return ContinueSearch;
    }

    private static bool IsTargetFound(Relation current, string target)
        => current.Person == target;

    private static void EnqueueUnvisitedNeighbors(
        Relation current,
        HashSet<string> visited,
        Queue<Relation> queue,
        Dictionary<string, List<string>> adjacencyList)
        => GetUnvisitedNeighbors(current.Person, visited, adjacencyList)
            .ToList()
            .ForEach(neighbor =>
            {
                visited.Add(neighbor);
                queue.Enqueue(new(neighbor, current.Degree + 1));
            });

    private static IEnumerable<string> GetUnvisitedNeighbors(
        string person,
        HashSet<string> visited,
        Dictionary<string, List<string>> adjacencyList)
        => adjacencyList
            .Where(kvp => kvp.Key == person)
            .SelectMany(kvp => kvp.Value)
            .Where(neighbor => !visited.Contains(neighbor));
}

#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
internal readonly struct Relation(string person, int degree)
{
    public string Person { get; init; } = person;
    public int Degree { get; init; } = degree;
}

#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
internal readonly struct FamilyRelation(string parent, string child)
{
    public string Parent { get; init; } = parent;
    public string Child { get; init; } = child;
}