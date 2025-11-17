defmodule SecretHandshake do
  @doc """
  Determine the actions of a secret handshake based on the binary
  representation of the given `code`.

  If the following bits are set, include the corresponding action in your list
  of commands, in order from lowest to highest.

  1 = wink
  10 = double blink
  100 = close your eyes
  1000 = jump

  10000 = Reverse the order of the operations in the secret handshake
  """

  @wink 1
  @double_blink 2
  @close_your_eyes 4
  @jump 8
  @reverse_list 16

  @actionByBitValue %{
    @wink => "wink",
    @double_blink => "double blink",
    @close_your_eyes => "close your eyes",
    @jump => "jump"
  }

  @spec commands(code :: integer) :: list(String.t())
  def commands(code),
    do: commands(code, _accumulated = [])

  defp commands(code, accumulated) when code >= @reverse_list,
    do: Enum.reverse(commands(code - @reverse_list, accumulated))

  defp commands(code, accumulated) when code >= @jump,
    do: add_command(code, @jump, accumulated)

  defp commands(code, accumulated) when code >= @close_your_eyes,
    do: add_command(code, @close_your_eyes, accumulated)

  defp commands(code, accumulated) when code >= @double_blink,
    do: add_command(code, @double_blink, accumulated)

  defp commands(code, accumulated) when code >= @wink,
    do: add_command(code, @wink, accumulated)

  defp commands(_code, accumulated),
    do: accumulated

  defp add_command(remaining_code, current_code, accumulated) do
    commands(remaining_code - current_code, [@actionByBitValue[current_code] | accumulated])
  end
end
