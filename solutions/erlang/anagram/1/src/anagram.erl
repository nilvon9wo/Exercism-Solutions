-module(anagram).

-export([find_anagrams/2]).


find_anagrams(Subject, Candidates) ->
  UniqueCandidates = lists:filter(
    fun(Candidate) ->
      string:to_upper(Candidate) =/= string:to_upper(Subject)
    end,
    Candidates),
  lists:filter(
    fun(Candidate) ->
      graph(Candidate) == graph(Subject)
    end,
    UniqueCandidates).

graph(Word) ->
  UpperCaseWord = string:to_upper(Word),
  Graphemes = string:to_graphemes(UpperCaseWord),
  lists:sort(Graphemes).
