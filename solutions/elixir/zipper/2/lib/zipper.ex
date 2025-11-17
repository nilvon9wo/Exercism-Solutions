defmodule Zipper do
  @type t :: %Zipper{bin_tree: BinTree.t(), focus: BinTree.t(), path: t() | nil}

  defstruct [:bin_tree, :focus, :path]

  @doc """
  Get a zipper focused on the root node.
  """
  @spec from_tree(BinTree.t()) :: Zipper.t()
  def from_tree(%BinTree{} = bin_tree),
    do: %Zipper{
      bin_tree: bin_tree,
      focus: bin_tree,
      path: []
    }

  @doc """
  Get the complete tree from a zipper.
  """
  @spec to_tree(Zipper.t()) :: BinTree.t()
  def to_tree(%Zipper{bin_tree: bin_tree}),
    do: bin_tree

  @doc """
  Get the value of the focus node.
  """
  @spec value(Zipper.t()) :: any
  def value(%Zipper{
        focus: %BinTree{value: value}
      }),
      do: value

  @doc """
  Get the left child of the focus node, if any.
  """
  @spec left(Zipper.t()) :: Zipper.t() | nil
  def left(%Zipper{focus: %BinTree{left: nil}}),
    do: nil

  def left(%Zipper{
        bin_tree: bin_tree,
        focus: %BinTree{left: left},
        path: path
      }),
      do: %Zipper{
        bin_tree: bin_tree,
        focus: left,
        path: [:left | path]
      }

  @doc """
  Get the right child of the focus node, if any.
  """
  @spec right(Zipper.t()) :: Zipper.t() | nil
  def right(%Zipper{focus: %BinTree{right: nil}}),
    do: nil

  def right(%Zipper{
        bin_tree: bin_tree,
        focus: %BinTree{right: right},
        path: path
      }),
      do: %Zipper{
        bin_tree: bin_tree,
        focus: right,
        path: [:right | path]
      }

  @doc """
  Get the parent of the focus node, if any.
  """
  @spec up(Zipper.t()) :: Zipper.t() | nil
  def up(%Zipper{path: []}),
    do: nil

  def up(%Zipper{bin_tree: bin_tree, path: [_ | path_tail]}),
    do: %Zipper{
      bin_tree: bin_tree,
      focus:
        path_tail
        |> Enum.reverse()
        |> refocus(bin_tree),
      path: path_tail
    }

  defp refocus([], focus),
    do: focus

  defp refocus([:left | path_tail], focus),
    do: refocus(path_tail, focus, &left/1)

  defp refocus([:right | path_tail], focus),
    do: refocus(path_tail, focus, &right/1)

  defp refocus(path_tail, focus, focus_function) do
    %{focus: new_focus} =
      focus
      |> from_tree()
      |> focus_function.()

    refocus(path_tail, new_focus)
  end

  @doc """
  Set the value of the focus node.
  """
  @spec set_value(Zipper.t(), any) :: Zipper.t()
  def set_value(
        %Zipper{focus: old_focus, path: path} = zipper,
        new_value
      ),
      do: update_bin_tree(zipper, %BinTree{old_focus | value: new_value}, path)

  defp update_bin_tree(_zipper, %BinTree{} = new_focus, [] = path),
    do: %Zipper{bin_tree: new_focus, focus: new_focus, path: path}

  defp update_bin_tree(%Zipper{bin_tree: bin_tree} = zipper, new_focus, path),
    do: %Zipper{zipper | bin_tree: update_bin_tree(bin_tree, new_focus, path), focus: new_focus}

  defp update_bin_tree(%BinTree{} = bin_tree, new_focus, path),
    do: modify_bin_tree(%BinTree{} = bin_tree, new_focus, Enum.reverse(path))

  defp modify_bin_tree(%BinTree{} = _bin_tree, new_focus, [] = _path),
    do: new_focus

  defp modify_bin_tree(%BinTree{} = bin_tree, new_focus, [path_head | path_tail]),
    do:
      Map.put(
        bin_tree,
        path_head,
        modify_bin_tree(Map.fetch!(bin_tree, path_head), new_focus, path_tail)
      )

  @doc """
  Replace the left child tree of the focus node.
  """
  @spec set_left(Zipper.t(), BinTree.t() | nil) :: Zipper.t()
  def set_left(%Zipper{focus: focus, path: path} = zipper, nil),
    do: update_bin_tree(zipper, %{focus | left: nil}, path)

  def set_left(
        %Zipper{focus: focus, path: [_ | path_tail]} = zipper,
        %BinTree{value: _value, left: nil, right: nil} = leaf
      ),
      do: update_bin_tree(zipper, %{focus | left: leaf}, [:left | path_tail])

  def set_left(
        %Zipper{bin_tree: bin_tree} = zipper,
        %BinTree{value: _value, left: _left, right: _right} = subtree
      ),
      do: %Zipper{
        zipper
        | bin_tree: %BinTree{bin_tree | left: subtree},
          focus: subtree
      }

  @doc """
  Replace the right child tree of the focus node.
  """
  @spec set_right(Zipper.t(), BinTree.t() | nil) :: Zipper.t()
  def set_right(%Zipper{focus: focus, path: path} = zipper, nil),
    do: update_bin_tree(zipper, %{focus | right: nil}, path)

  def set_right(
        %Zipper{focus: focus, path: [_ | path_tail]} = zipper,
        %BinTree{value: _value, left: nil, right: nil} = leaf
      ),
      do: update_bin_tree(zipper, %{focus | right: leaf}, [:right | path_tail])

  def set_right(%Zipper{bin_tree: bin_tree} = zipper, right),
    do: %Zipper{zipper | bin_tree: %BinTree{bin_tree | right: right}, focus: right}
end
