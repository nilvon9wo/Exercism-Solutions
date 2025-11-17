defmodule Atbash do
  @cipher_by_plain %{
    "a" => "z",
    "b" => "y",
    "c" => "x",
    "d" => "w",
    "e" => "v",
    "f" => "u",
    "g" => "t",
    "h" => "s",
    "i" => "r",
    "j" => "q",
    "k" => "p",
    "l" => "o",
    "m" => "n",
    "n" => "m",
    "o" => "l",
    "p" => "k",
    "q" => "j",
    "r" => "i",
    "s" => "h",
    "t" => "g",
    "u" => "f",
    "v" => "e",
    "w" => "d",
    "x" => "c",
    "y" => "b",
    "z" => "a",
    "0" => "0",
    "1" => "1",
    "2" => "2",
    "3" => "3",
    "4" => "4",
    "5" => "5",
    "6" => "6",
    "7" => "7",
    "8" => "8",
    "9" => "9"
  }

  @doc """
  Encode a given plaintext to the corresponding ciphertext

  ## Examples

  iex> Atbash.encode("completely insecure")
  "xlnko vgvob rmhvx fiv"
  """
  @spec encode(String.t()) :: String.t()
  def encode(plaintext),
    do:
      plaintext
      |> String.downcase()
      |> String.graphemes()
      |> Enum.map(&Map.get(@cipher_by_plain, &1, " "))
      |> Enum.reject(&(&1 == " "))
      |> insert_spaces()
      |> Enum.join()
      |> String.trim()

  defp insert_spaces(encoding),
    do: insert_spaces(encoding, _spaced_encoding = [])

  defp insert_spaces([] = encoding, spaced_encoding),
    do: spaced_encoding

  defp insert_spaces(encoding, spaced_encoding),
    do:
      insert_spaces(
        Enum.drop(encoding, 5),
        spaced_encoding ++ Enum.take(encoding, 5) ++ [' ']
      )

  @spec decode(String.t()) :: String.t()
  def decode(cipher),
    do:
      cipher
      |> encode()
      |> String.replace(" ", "")
end
