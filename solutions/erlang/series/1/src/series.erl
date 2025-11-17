-module(series).

-export([slices/2]).

slices(SliceLength, Series, SliceRange) ->
  lists:map(
    create_slice(SliceLength, Series),
    SliceRange
  ).

create_slice(SliceLength, Series) ->
  fun(X) ->
    string:slice(Series, X, SliceLength)
  end.

slices(SliceLength, _Series)
  when SliceLength < 1 ->
  erlang:error("fail");

slices(SliceLength, Series) ->
  StringLength = string:len(Series),
  if
    StringLength >= SliceLength ->
      SliceRange = lists:seq(0, StringLength - SliceLength),
      slices(SliceLength, Series, SliceRange);

    true ->
      erlang:error("fail")
  end
.


