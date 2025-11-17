defmodule Bob do
  @response_by_prompt_type %{
    :blank => "Fine. Be that way!",
    :question => "Sure.",
    :yell => "Whoa, chill out!",
    :yelled_question => "Calm down, I know what I'm doing!",
    :other => "Whatever."
  }

  def hey(input),
    do: Map.get(@response_by_prompt_type, find_prompt_type(input))

  defp find_prompt_type(input) do
    all_capitals? = all_capitals?(input)
    question? = question?(input)

    cond do
      blank?(input) ->
        :blank

      all_capitals? and question? ->
        :yelled_question

      all_capitals? ->
        :yell

      question? ->
        :question

      true ->
        :other
    end
  end

  defp blank?(input),
    do: "" === String.trim(input)

  defp question?(input),
    do: String.ends_with?(input, "?")

  defp all_capitals?(input),
    do:
      input === String.upcase(input) and
        has_uppercase?(input)

  defp has_uppercase?(input),
    do: String.match?(input, ~r/\p{Lu}/)
end
