using System;

// ReSharper disable once CheckNamespace
public class BinTree
{
	public BinTree(int value, BinTree? left, BinTree? right)
	{
		Value = value;
		Left = left;
		Right = right;
	}

	public int Value { get; }
	public BinTree? Left { get; }
	public BinTree? Right { get; }

	public override bool Equals(object? obj)
		=> obj is BinTree other
		   && (Value == other.Value)
		   && Equals(Left, other.Left)
		   && Equals(Right, other.Right);

	public override int GetHashCode()
		=> HashCode.Combine(Value, Left, Right);
}

// ReSharper disable once CheckNamespace
public sealed class Zipper
{
	private readonly BinTree _focus;
	private readonly Zipper? _parent;
	private readonly bool _isLeftChild;

	private Zipper(BinTree focus, Zipper parent, bool isLeftChild)
	{
		_focus = focus;
		_parent = parent;
		_isLeftChild = isLeftChild;
	}

	public int Value()
		=> _focus.Value;

	public Zipper SetValue(int newValue)
	{
		BinTree newFocus = new(newValue, _focus.Left, _focus.Right);
		return new(newFocus, UpdateParent(newFocus)!, _isLeftChild);
	}

	public Zipper SetLeft(BinTree binTree)
	{
		BinTree newFocus = new(_focus.Value, binTree, _focus.Right);
		return new(newFocus, UpdateParent(newFocus)!, _isLeftChild);
	}

	public Zipper SetRight(BinTree binTree)
	{
		BinTree newFocus = new(_focus.Value, _focus.Left, binTree);
		return new(newFocus, UpdateParent(newFocus)!, _isLeftChild);
	}

	public Zipper? Left()
		=> _focus.Left == null
			? null
			: new Zipper(_focus.Left, this, true);

	public Zipper? Right()
		=> _focus.Right == null
			? null
			: new Zipper(_focus.Right, this, false);

	public Zipper Up()
		=> _parent!;

	public BinTree ToTree()
		=> _parent == null
			? _focus
			: _parent.ToTree();

	public static Zipper FromTree(BinTree tree)
		=> new(tree, null!, false);

	private Zipper? UpdateParent(BinTree newChild)
	{
		if (_parent == null)
		{
			return null;
		}

		BinTree newParentTree = _isLeftChild
			? new(_parent._focus.Value, newChild, _parent._focus.Right)
			: new BinTree(_parent._focus.Value, _parent._focus.Left, newChild);

		Zipper newParentZipper = new(newParentTree, _parent._parent!, _parent._isLeftChild);

		return _parent._parent == null
			? newParentZipper
			: newParentZipper.UpdateParent(newParentTree);
	}

	public override bool Equals(object? obj)
		=> obj is Zipper other
		   && Equals(_focus, other._focus)
		   && Equals(_parent, other._parent)
		   && (_isLeftChild == other._isLeftChild);

	public override int GetHashCode()
		=> HashCode.Combine(_focus, _parent, _isLeftChild);
}