public class Context
{
    public Dictionary<string, string> InstructionsByWord { get; init; } = new(StringComparer.OrdinalIgnoreCase);

    private static readonly CultureInfo _invariantCulture = CultureInfo.InvariantCulture;

    [SuppressMessage("Design", "CA1002:Do not expose generic lists", Justification = "<Pending>")]
    public List<int> Stack { get; init; } = new();

    public override string ToString()
        => string.Join(' ', Stack);

    public Context DefineHandler(Group group)
    {
        _ = group ?? throw new ArgumentNullException(nameof(group));
        List<string> definition = group.Value.Split(' ')
            .Where(x => !string.IsNullOrEmpty(x))
            .ToList()
            .RequireAtLeastTwoValues();

        string word = definition.Shift();
        if (int.TryParse(word, out int _))
        {
            throw new InvalidOperationException("Redefining numbers not allowed.");
        }

        InstructionsByWord[word] = CreateInstruction(definition);
        return this;
    }

    private string CreateInstruction(List<string> definition)
    {
        StringBuilder builder = new();
        foreach (string word in definition)
        {
            _ = InstructionsByWord.TryGetValue(word, out string instruction)
                ? builder.Append(_invariantCulture, $"{instruction} ")
                : builder.Append(_invariantCulture, $"{word} ");
        }

        return builder.ToString()
            .Trim();
    }

    public Context WithStack(Action<List<int>> function)
    {
        _ = function ?? throw new ArgumentNullException(nameof(function));
        function(Stack);
        return this;
    }
}
