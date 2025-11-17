defmodule Bob do
  @response_by_prompt_type %{
    :question => "Sure.",
    :yell => "Whoa, chill out!",
    :yelled_question => "Calm down, I know what I'm doing!",
    :blank => "Fine. Be that way!",
    :other => "Whatever."
  }

  def hey(input),
    do: Map.get(@response_by_prompt_type, find_prompt_type(input))

  defp find_prompt_type(input) do
    is_all_capitals? = is_all_capitals?(input)
    is_question? = is_question?(input)

    cond do
      is_blank?(input) ->
        :blank

      is_all_capitals? and is_question? ->
        :yelled_question

      is_all_capitals? ->
        :yell

      is_question? ->
        :question

      true ->
        :other
    end
  end

  defp is_question?(input),
    do: String.last(input) === "?"

  defp is_blank?(input),
    do: "" === String.trim(input)

  defp is_all_capitals?(input),
    do:
      input === String.upcase(input) and
        has_uppercase?(input)

  defp has_uppercase?(input),
    do: String.match?(input, ~r/\p{Lu}/)
end
