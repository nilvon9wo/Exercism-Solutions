defmodule SimpleCipher do
  @doc """
  Given a `plaintext` and `key`, encode each character of the `plaintext` by
  shifting it by the corresponding letter in the alphabet shifted by the number
  of letters represented by the `key` character, repeating the `key` if it is
  shorter than the `plaintext`.

  For example, for the letter 'd', the alphabet is rotated to become:

  defghijklmnopqrstuvwxyzabc

  You would encode the `plaintext` by taking the current letter and mapping it
  to the letter in the same position in this rotated alphabet.

  abcdefghijklmnopqrstuvwxyz
  defghijklmnopqrstuvwxyzabc

  "a" becomes "d", "t" becomes "w", etc...

  Each letter in the `plaintext` will be encoded with the alphabet of the `key`
  character in the same position. If the `key` is shorter than the `plaintext`,
  repeat the `key`.

  Example:

  plaintext = "testing"
  key = "abc"

  The key should repeat to become the same length as the text, becoming
  "abcabca". If the key is longer than the text, only use as many letters of it
  as are necessary.
  """
  def encode(plaintext, encoding_key),
    do: transform(plaintext, encoding_key, &encode_function/2)

  def transform(plaintext, encoding_key, transform_function),
    do:
      plaintext
      |> String.graphemes()
      |> Enum.zip(reshape_encoding_key(plaintext, encoding_key))
      |> Enum.map(&transform(&1, transform_function))
      |> List.to_string()

  defp reshape_encoding_key(plaintext, encoding_key) do
    plain_length = String.length(plaintext)
    encoding_length = String.length(encoding_key)

    cond do
      plain_length < encoding_length ->
        encoding_key
        |> String.split_at(plain_length)
        |> elem(0)

      plain_length == encoding_length ->
        encoding_key

      plain_length > encoding_length ->
        String.pad_trailing(encoding_key, plain_length, encoding_key)
    end
    |> String.graphemes()
  end

  defp transform({plaintext, encoding_key}, transform_function),
    do:
      plaintext
      |> String.graphemes()
      |> Enum.map(&to_char_list/1)
      |> Enum.map(&hd/1)
      |> Enum.map(&transform_function.(&1, encoding_key))

  @doc """
  Given a `ciphertext` and `key`, decode each character of the `ciphertext` by
  finding the corresponding letter in the alphabet shifted by the number of
  letters represented by the `key` character, repeating the `key` if it is
  shorter than the `ciphertext`.

  The same rules for key length and shifted alphabets apply as in `encode/2`,
  but you will go the opposite way, so "d" becomes "a", "w" becomes "t",
  etc..., depending on how much you shift the alphabet.
  """
  def decode(ciphertext, encoding_key),
    do: transform(ciphertext, encoding_key, &decode_function/2)

  defp encode_function(character_value, encoding_key),
    do: transform_letters(character_value, encoding_key, &add_key_value/2)

  defp add_key_value(character_value, encoding_key) do
    encoded = character_value + calculate_key_value(character_value, encoding_key)

    encoded =
      if encoded <= ?z,
        do: encoded,
        else: encoded - 26
  end

  defp decode_function(character_value, encoding_key),
    do: transform_letters(character_value, encoding_key, &subtract_key_value/2)

  defp subtract_key_value(character_value, encoding_key) do
    decoded = character_value - calculate_key_value(character_value, encoding_key)

    if decoded >= ?a,
      do: decoded,
      else: decoded + 26
  end

  defp transform_letters(character_value, encoding_key, transform_function) do
    if character_value >= ?a && character_value <= ?z,
      do: transform_function.(character_value, encoding_key),
      else: character_value
  end

  defp calculate_key_value(character_value, encoding_key) do
    key_value =
      encoding_key
      |> to_char_list()
      |> hd()

    key_value = key_value - ?a
  end
end
