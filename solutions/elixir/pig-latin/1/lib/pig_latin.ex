defmodule PigLatin do
  @doc """
  Given a `phrase`, translate it a word at a time to Pig Latin.

  Words beginning with consonants should have the consonant moved to the end of
  the word, followed by "ay".

  Words beginning with vowels (aeiou) should have "ay" added to the end of the
  word.

  Some groups of letters are treated like consonants, including "ch", "qu",
  "squ", "th", "thr", and "sch".

  Some groups are treated like vowels, including "yt" and "xr".
  """
  @vowel_sounds ~r/^a|^e|^i|^o|^u/
  @consonant_sounds ~r/(?<consonants>^[b|c|d|f|g|h|j|k|l|m|n|p|q|r|s|t|v|w|x|z]*)/
  @sometimes_vowel_like ~r/^x|^y/
  @suffix "ay"

  @erlang_largest_64_bit_integer 2_305_843_009_213_693_951
  @arbitrary_word_end @erlang_largest_64_bit_integer

  @spec translate(phrase :: String.t()) :: String.t()
  def translate(phrase) do
    phrase
    |> String.split(" ")
    |> Enum.map(&translate_word/1)
    |> Enum.join(" ")
  end

  defp translate_word(word) do
    cond do
      does_word_begin_with_sometimes_vowel_like_letter?(word) ->
        handle_word_beginning_with_sometimes_vowel_like_letter(word)

      does_word_begin_with_vowel?(word) ->
        handle_word_beginning_with_vowel(word)

      does_word_begin_with_consonant?(word) ->
        handle_word_beginning_with_consonant(word)
    end
  end

  defp handle_word_beginning_with_sometimes_vowel_like_letter(word) do
    first_letter = String.first(word)
    ending = String.slice(word, 1..@arbitrary_word_end)

    capture = Regex.named_captures(@consonant_sounds, String.first(ending))
    immediate_consonants = capture["consonants"]

    if immediate_consonants !== "",
      do: add_suffix(word),
      else: add_suffix(ending <> first_letter)
  end

  def handle_word_beginning_with_vowel(word),
    do: add_suffix(word)

  defp handle_word_beginning_with_consonant(word) do
    %{"consonants" => start} = Regex.named_captures(@consonant_sounds, word)
    length = String.length(start)
    ending = String.slice(word, length..@arbitrary_word_end)

    if String.last(start) === "q" and
         String.first(ending) === "u",
       do: add_suffix(String.slice(ending, 1..@arbitrary_word_end) <> start <> "u"),
       else: add_suffix(ending <> start)
  end

  defp add_suffix(word),
    do: word <> @suffix

  defp does_word_begin_with_vowel?(word),
    do: Regex.match?(@vowel_sounds, word)

  defp does_word_begin_with_consonant?(word),
    do: Regex.match?(@consonant_sounds, word)

  defp does_word_begin_with_sometimes_vowel_like_letter?(word),
    do: Regex.match?(@sometimes_vowel_like, word)
end
