defmodule LinkedList do
  defstruct [:head, :tail]
  @opaque t :: tuple()

  @doc """
  Construct a new LinkedList
  """
  @spec new() :: t
  def new(),
    do: %LinkedList{}

  @doc """
  Push an item onto a LinkedList
  """
  @spec push(t, any()) :: t
  def push(%LinkedList{} = list, elem),
    do: %LinkedList{head: elem, tail: list}

  @doc """
  Calculate the length of a LinkedList
  """
  @spec length(t) :: non_neg_integer()
  def length(%LinkedList{head: nil, tail: nil}),
    do: 0

  def length(%LinkedList{} = linked_list),
    do: length(linked_list, _accumulator = 0)

  def length(%LinkedList{head: nil, tail: nil} = _linked_list, accumulator),
    do: accumulator

  def length(%LinkedList{head: _head, tail: tail} = _linked_list, accumulator),
    do: length(tail, accumulator + 1)

  @doc """
  Determine if a LinkedList is empty
  """
  @spec empty?(t) :: boolean()
  def empty?(%LinkedList{head: nil, tail: nil}),
    do: true

  def empty?(%LinkedList{}),
    do: false

  @doc """
  Get the value of a head of the LinkedList
  """
  @spec peek(t) :: {:ok, any()} | {:error, :empty_list}
  def peek(%LinkedList{head: nil, tail: nil} = _linked_list),
    do: {:error, :empty_list}

  def peek(%LinkedList{head: head} = _linked_list),
    do: {:ok, head}

  @doc """
  Get tail of a LinkedList
  """
  @spec tail(t) :: {:ok, t} | {:error, :empty_list}
  def tail(%LinkedList{head: nil, tail: nil} = _linked_list),
    do: {:error, :empty_list}

  def tail(%LinkedList{tail: tail} = _linked_list),
    do: {:ok, tail}

  @doc """
  Remove the head from a LinkedList
  """
  @spec pop(t) :: {:ok, any(), t} | {:error, :empty_list}
  def pop(%LinkedList{head: nil, tail: nil} = _linked_list),
    do: {:error, :empty_list}

  def pop(%LinkedList{head: head, tail: tail} = _linked_list),
    do: {:ok, head, tail}

  @doc """
  Construct a LinkedList from a stdlib List
  """
  @spec from_list(list()) :: t
  def from_list(nil),
    do: nil

  def from_list([]),
    do: new()

  def from_list(list) do
    from_list(list, new())
  end

  defp from_list([head | tail] = _list, %LinkedList{} = linked_list) do
    from_list(tail, push(linked_list, head))
  end

  defp from_list([] = _list, %LinkedList{} = linked_list),
    do: reverse(linked_list).tail

  @doc """
  Construct a stdlib List LinkedList from a LinkedList
  """
  @spec to_list(t) :: list()
  def to_list(%LinkedList{} = linked_list),
    do: to_list(reverse(linked_list), _accumulator = [])

  def to_list(%LinkedList{head: nil, tail: nil} = linked_list, accumulator),
    do: accumulator

  def to_list(%LinkedList{head: nil, tail: tail} = linked_list, accumulator),
    do: to_list(tail, accumulator)

  def to_list(%LinkedList{head: head, tail: tail} = linked_list, accumulator),
    do: to_list(tail, [head | accumulator])

  @doc """
  Reverse a LinkedList
  """
  @spec reverse(t) :: t
  def reverse(%LinkedList{} = linked_list),
    do: reverse(linked_list, _accumulator = new())

  defp reverse(%LinkedList{head: head, tail: tail} = linked_list, accumulator),
    do: reverse(tail, push(accumulator, head))

  defp reverse(nil = _linked_list, accumulator),
    do: accumulator
end
