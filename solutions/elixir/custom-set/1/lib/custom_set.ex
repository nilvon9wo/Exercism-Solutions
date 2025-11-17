defmodule CustomSet do
  defstruct [:map]
  @opaque t :: %__MODULE__{map: map}

  @spec new(Enum.t()) :: t
  def new([] = _enumerable),
    do: %CustomSet{}

  def new(enumerable),
    do: %CustomSet{map: Enum.uniq(enumerable)}

  @spec empty?(t) :: boolean
  def empty?(%CustomSet{map: nil}),
    do: true

  @spec empty?(t) :: boolean
  def empty?(_),
    do: false

  @spec contains?(t, any) :: boolean
  def contains?(%CustomSet{map: nil}, _element),
    do: false

  def contains?(%CustomSet{map: map}, element),
    do: Enum.member?(map, element)

  @spec subset?(t, t) :: boolean
  def subset?(
        %CustomSet{map: nil} = _custom_set_1,
        _custom_set_2
      ),
      do: true

  def subset?(
        %CustomSet{map: _map} = _custom_set_1,
        %CustomSet{map: nil} = _custom_set_2
      ),
      do: false

  def subset?(
        %CustomSet{map: map_1} = _custom_set_1,
        %CustomSet{map: map_2} = _custom_set_2
      ),
      do: Enum.all?(map_1, &Enum.member?(map_2, &1))

  @spec disjoint?(t, t) :: boolean
  def disjoint?(
        %CustomSet{} = custom_set_1,
        %CustomSet{} = custom_set_2
      ),
      do:
        empty?(custom_set_1) ||
          empty?(custom_set_2) ||
          has_no_common_elements(custom_set_1, custom_set_2)

  defp has_no_common_elements(
         %CustomSet{map: map_1} = _custom_set_1,
         %CustomSet{map: map_2} = _custom_set_2
       ),
       do: Enum.all?(map_1, &(!Enum.member?(map_2, &1)))

  @spec equal?(t, t) :: boolean
  def equal?(
        %CustomSet{map: nil} = _custom_set_1,
        %CustomSet{map: nil} = _custom_set_2
      ),
      do: true

  def equal?(
        %CustomSet{map: _map_1} = _custom_set_1,
        %CustomSet{map: nil} = _custom_set_2
      ),
      do: false

  def equal?(
        %CustomSet{map: nil} = _custom_set_1,
        %CustomSet{map: _map_2} = _custom_set_2
      ),
      do: false

  def equal?(
        %CustomSet{map: map_1} = _custom_set_1,
        %CustomSet{map: map_2} = _custom_set_2
      ),
      do: Enum.sort(map_1) == Enum.sort(map_2)

  @spec add(t, any) :: t
  def add(%CustomSet{map: nil}, element),
    do: %CustomSet{map: [element]}

  def add(%CustomSet{map: map} = custom_set, element) do
    if Enum.member?(map, element),
      do: custom_set,
      else: %CustomSet{map: [element | map]}
  end

  @spec intersection(t, t) :: t
  def intersection(
        %CustomSet{map: nil} = _custom_set_1,
        %CustomSet{} = _custom_set_2
      ),
      do: %CustomSet{map: nil}

  def intersection(
        %CustomSet{} = _custom_set_1,
        %CustomSet{map: nil} = _custom_set_2
      ),
      do: %CustomSet{map: nil}

  def intersection(
        %CustomSet{} = custom_set_1,
        %CustomSet{} = custom_set_2
      ) do
    sublist_1 = find_common_members(custom_set_1, custom_set_2)
    sublist_2 = find_common_members(custom_set_2, custom_set_1)
    new(sublist_1 ++ sublist_2)
  end

  defp find_common_members(
         %CustomSet{map: map_1} = _custom_set_1,
         %CustomSet{map: map_2} = _custom_set_2
       ),
       do: Enum.filter(map_1, &Enum.member?(map_2, &1))

  @spec difference(t, t) :: t
  def difference(
        %CustomSet{map: nil} = _custom_set_1,
        %CustomSet{} = _custom_set_2
      ),
      do: %CustomSet{map: nil}

  def difference(
        %CustomSet{} = custom_set_1,
        %CustomSet{map: nil} = _custom_set_2
      ),
      do: custom_set_1

  def difference(
        %CustomSet{map: map_1} = _custom_set_1,
        %CustomSet{map: map_2} = _custom_set_2
      ),
      do: %CustomSet{
        map: Enum.filter(map_1, &(!Enum.member?(map_2, &1)))
      }

  @spec union(t, t) :: t
  def union(
        %CustomSet{map: nil} = _custom_set_1,
        %CustomSet{} = custom_set_2
      ),
      do: custom_set_2

  def union(
        %CustomSet{} = custom_set_1,
        %CustomSet{map: nil} = _custom_set_2
      ),
      do: custom_set_1

  def union(
        %CustomSet{map: map_1} = _custom_set_1,
        %CustomSet{map: map_2} = _custom_set_2
      ),
      do: new(map_1 ++ map_2)
end
