defmodule Strain do
  @doc """
  Given a `list` of items and a function `fun`, return the list of items where
  `fun` returns true.

  Do not use `Enum.filter`.
  """
  @keep_matches true
  @discard_matches false

  @spec keep(list :: list(any), fun :: (any -> boolean)) :: list(any)
  def keep(list, function),
    do:
      find(%{
        with_matches: @keep_matches,
        list: list,
        function: function,
        accumulated: []
      })

  @doc """
  Given a `list` of items and a function `fun`, return the list of items where
  `fun` returns false.

  Do not use `Enum.reject`.
  """
  @spec discard(list :: list(any), fun :: (any -> boolean)) :: list(any)
  def discard(list, function),
    do:
      find(%{
        with_matches: @discard_matches,
        list: list,
        function: function,
        accumulated: []
      })

  defp find(%{
         with_matches: with_matches,
         result: true,
         list: [first_value | remaining_list],
         function: function,
         accumulated: accumulated
       }),
       do:
         find(%{
           with_matches: with_matches,
           list: remaining_list,
           function: function,
           accumulated: [first_value | accumulated]
         })

  defp find(%{
         with_matches: with_matches,
         result: false,
         list: [_first_value | remaining_list],
         function: function,
         accumulated: accumulated
       }),
       do:
         find(%{
           with_matches: with_matches,
           list: remaining_list,
           function: function,
           accumulated: accumulated
         })

  defp find(%{
         with_matches: _with_matches,
         list: remaining_list,
         function: function,
         accumulated: accumulated
       })
       when length(remaining_list) === 0,
       do: Enum.reverse(accumulated)

  defp find(%{
         with_matches: with_matches,
         list: [first_value | _] = list,
         function: function,
         accumulated: accumulated
       }),
       do:
         find(%{
           with_matches: with_matches,
           result: with_matches === function.(first_value),
           list: list,
           function: function,
           accumulated: accumulated
         })
end
