using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Text;

using Node = System.Collections.Generic.IDictionary<string, string[]>;

//=======================================================================

// ReSharper disable once CheckNamespace
public class SgfTree
{
    public SgfTree(IDictionary<string, string[]> data, params SgfTree[] children)
    {
        Data = data;
        Children = children;
    }

    public IDictionary<string, string[]> Data { get; }

    public SgfTree[] Children { get; }
}

// ReSharper disable once CheckNamespace
public static class SgfParser
{
    public static SgfTree ParseTree(string inputString)
    {
        _ = inputString ?? throw new ArgumentNullException(nameof(inputString));

        Input input = new(inputString);
        _ = input.MoveNext();

        var root = GroupParser.Parse(input);
        return root.IsValid
            ? root.Value.ToTree()
            : throw root.Exception ?? new ArgumentException("Something went wrong.");
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class Symbols
{
    internal const char GroupStart = '(';
    internal const char GroupEnd = ')';
    internal const char OptionStart = '[';
    internal const char OptionEnd = ']';
    internal const char PropertySeparator = ';';
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class GroupParser
{
    internal static Result<Group> Parse(Input input)
    {
        if (input.Current != Symbols.GroupStart)
        {
            return Result<Group>.From(new ArgumentException("Input is missing group."));
        }

        if (!input.MoveNext())
        {
            return Result<Group>.From(new ArgumentException("Group is missing parent node."));
        }

        List<NodeFamily> nodes = new();
        while (input.Current != Symbols.GroupEnd)
        {
            var node = NodeFamilyParser.Parse(input);
            if (node.IsValid)
            {
                nodes.Add(node.Value);
            }
            else
            {
                return Result<Group>.From(node.Exception);
            }
        }

        return nodes.Any()
            ? Result<Group>.From(new Group(nodes))
            : Result<Group>.From(new ArgumentException("Input has no nodes."));
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class KeyedOptionsParser
{
    public static Result<KeyedOptions> Parse(Input input)
    {
        var key = KeyParser.Parse(input);
        if (!key.IsValid)
        {
            return Result<KeyedOptions>.From(key.Exception);
        }

        List<string> options = new();
        while (input.Current == Symbols.OptionStart)
        {
            var option = OptionParser.Parse(input);
            if (!option.IsValid)
            {
                return Result<KeyedOptions>.From(option.Exception);
            }

            options.Add(option.Value);
            _ = input.MoveNext();
        }

        return Result<KeyedOptions>.From(new KeyedOptions() { Key = key.Value, Options = options.ToArray() });
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class KeyParser
{
    public static Result<string> Parse(Input input)
    {
        var keyResult = input.TakeUntil(Symbols.OptionStart, "Key");
        return keyResult.IsValid && keyResult.Value.ContainsLowerCase()
            ? Result<string>.From(new ArgumentException($"Key {keyResult.Value} is contains disallowed lowercase."))
            : keyResult;
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class NodeFamilyParser
{
    private static readonly HashSet<char> _optionEnding
        = new() { Symbols.GroupStart, Symbols.GroupEnd, Symbols.PropertySeparator };

    public static Result<NodeFamily> Parse(Input input)
    {
        if (input.Current != Symbols.PropertySeparator)
        {
            return Result<NodeFamily>.From(
                new ArgumentException("Each nodeFamily must begin with a separator.")
            );
        }

        var parentResult = ParseParent(input);
        if (!parentResult.IsValid)
        {
            return Result<NodeFamily>.From(parentResult.Exception);
        }

        var childrenResults = ParseChildren(input);
        if (!childrenResults.IsValid)
        {
            return Result<NodeFamily>.From(childrenResults.Exception);
        }

        var parent = parentResult.Value;
        var children = childrenResults.Value;
        var nodeFamily = !children.Any()
            ? new NodeFamily { Parent = parent }
            : new NodeFamily { Parent = parent, Children = children };

        return Result<NodeFamily>.From(nodeFamily);
    }

    private static Result<Node> ParseParent(Input input)
    {
        Node parent = new Dictionary<string, string[]>();
        if (!input.MoveNext())
        {
            return Result<Node>.From(new ArgumentException("Unexpected parent ending."));
        }

        while (!IsEndOfOptions(input))
        {
            var keyedOptions = KeyedOptionsParser.Parse(input);
            if (!keyedOptions.IsValid)
            {
                return Result<Node>.From(keyedOptions.Exception);
            }

            var keyedOptionsValue = keyedOptions.Value;
            parent[keyedOptionsValue.Key] = keyedOptionsValue.Options;
        }

        return Result<Node>.From(parent);
    }

    private static Result<ReadOnlyCollection<Group>> ParseChildren(Input input)
    {
        List<Group> children = new();
        while (input.Current == Symbols.GroupStart)
        {
            var childrenResult = GroupParser.Parse(input);
            if (!childrenResult.IsValid)
            {
                return Result<ReadOnlyCollection<Group>>.From(childrenResult.Exception);
            }

            children.Add(childrenResult.Value);
            if (!input.MoveNext())
            {
                return Result<ReadOnlyCollection<Group>>.From(new ArgumentException("Child Group ended unexpectedly."));
            }
        }

        return Result<ReadOnlyCollection<Group>>.From(new ReadOnlyCollection<Group>(children));
    }

    private static bool IsEndOfOptions(Input input) => _optionEnding.Contains(input.Current);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class OptionParser
{
    public static Result<string> Parse(Input input) => input.Current != Symbols.OptionStart
        ? Result<string>.From(new ArgumentException("Input is missing option."))
        : !input.MoveNext()
            ? Result<string>.From(new ArgumentException("Option is missing contents."))
            : input.TakeUntil(Symbols.OptionEnd, "Option");
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Group
{
    public Group(IEnumerable<NodeFamily> nodes) => _nodes = new ReadOnlyCollection<NodeFamily>(nodes.ToArray());

    private ReadOnlyCollection<NodeFamily> _nodes { get; init; }

    public SgfTree ToTree()
    {
        var parentNodeFamily = _nodes[0];
        if (parentNodeFamily.Children?.Any() == true)
        {
            return parentNodeFamily.ToTree();
        }

        var children = _nodes
            .Skip(1)
            .Select(node => node.ToTree())
            .ToArray();
        return new SgfTree(parentNodeFamily.Parent, children);
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class Input
{
    private readonly string _originalValue;
    private int _currentIndex = -1;

    public Input(string inputString)
    {
        if (string.IsNullOrEmpty(inputString))
        {
            throw new ArgumentException("Input cannot be null or empty.");
        }

        _originalValue = inputString.Trim();
    }

    public char Current
        => _currentIndex >= 0
           && _currentIndex < _originalValue.Length
            ? _originalValue[_currentIndex]
            : throw new InvalidOperationException();

    public bool MoveNext()
    {
        _currentIndex++;
        return _currentIndex < _originalValue.Length;
    }

    public Result<string> TakeUntil(char terminator, string description)
    {
        StringBuilder stringBuilder = new();
        while (Current != terminator)
        {
            stringBuilder = stringBuilder.Append(Current);

            if (!MoveNext())
            {
                return Result<string>.From(
                    new ArgumentException($"{description} is missing terminator: {stringBuilder}.")
                );
            }
        }

        var value = stringBuilder.ToString();
        return string.IsNullOrWhiteSpace(value)
            ? Result<string>.From(new ArgumentException($"{description} is missing value."))
            : Result<string>.From(value);
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal sealed class KeyedOptions
{
    public required string Key { get; init; }
    public required string[] Options { get; init; }
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class NodeFamily
{
    public required Node Parent { get; init; }
    public ReadOnlyCollection<Group> Children { get; init; }

    public SgfTree ToTree()
    {
        if (Children == null)
        {
            return new SgfTree(Parent);
        }

        var children = Children
            .Select(child => child.ToTree())
            .ToArray();
        return new SgfTree(Parent, children);
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class Result<T>
{
    private T _value;

    public T Value
        => _value != null
            ? _value
            : throw new InvalidOperationException("Value is Invalid");

    private Exception _exception;

    public Exception Exception
        => _exception ?? throw new InvalidOperationException("Exception is null.");

    public bool IsValid
        => _value != null
           && _exception == null;

    public static Result<T> From([DisallowNull] T value) => new() { _value = value };

    public static Result<T> From(Exception exception) => new() { _exception = exception };
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class ListExtensions
{
    public static T Shift<T>(this List<T> list)
    {
        if (list == null)
        {
            throw new ArgumentNullException(nameof(list));
        }

        if (list.Count == 0)
        {
            throw new InvalidOperationException("The list is empty.");
        }

        var shiftedItem = list[0];
        list.RemoveAt(0);
        return shiftedItem;
    }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class StringExtensions
{
    internal static bool ContainsLowerCase(this string value) => value.Any(char.IsLower);
}

//=======================================================================