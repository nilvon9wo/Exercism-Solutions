defmodule IsbnNumber do
  @enforce [:digits, :check]
  defstruct [:digits, :check]

  def new({digits, check})
      when is_list(check) and
             length(digits) === 9 and
             length(check) === 1,
      do: new({to_numbers(digits), hd(check)})

  def new({digits, "X"})
      when length(digits) === 9,
      do: new({digits, 10})

  def new({digits, check})
      when length(digits) === 9 and
             is_number(check),
      do:
        {:ok,
         %IsbnNumber{
           digits: digits,
           check: check
         }}

  def new({digits, check})
      when length(digits) === 9 do
    try do
      new({digits, String.to_integer(check)})
    rescue
      _ ->
        {:error, "Bad input"}
    end
  end

  def new(_),
    do: {:error, "Bad input"}

  defp to_numbers(digits) do
    try do
      Enum.map(digits, &String.to_integer/1)
    rescue
      _ ->
        []
    end
  end

  def valid?(%IsbnNumber{digits: digits, check: check}) do
    checksum =
      [check | Enum.reverse(digits)]
      |> Enum.with_index()
      |> Enum.map(&calculate/1)
      |> Enum.sum()

    rem(checksum, 11) === 0
  end

  defp calculate({value, index}),
    do: value * (index + 1)
end

defmodule IsbnVerifier do
  alias IsbnNumber

  @doc """
    Checks if a string is a valid ISBN-10 identifier

    ## Examples

      iex> ISBNVerifier.isbn?("3-598-21507-X")
      true

      iex> ISBNVerifier.isbn?("3-598-2K507-0")
      false

  """
  @spec isbn?(String.t()) :: boolean
  def isbn?(input),
    do:
      input
      |> String.replace("-", "")
      |> String.graphemes()
      |> Enum.split(-1)
      |> IsbnNumber.new()
      |> check_isbn?()

  defp check_isbn?({:ok, %IsbnNumber{} = isbn}),
    do: IsbnNumber.valid?(isbn)

  defp check_isbn?({:error, _}),
    do: false
end
