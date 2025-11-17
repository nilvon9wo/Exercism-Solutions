-module(nucleotide_count).

-export([count/2, nucleotide_counts/1]).

count(Strand, Nucleotide) ->
  NucleotideCount = count_nucleotides(Strand, create_nucleotides_map()),
  maps:get(Nucleotide, NucleotideCount).

nucleotide_counts(Strand) ->
  to_list(count_nucleotides(Strand)).

count_nucleotides(Strand) ->
  count_nucleotides(Strand, create_nucleotides_map()).

count_nucleotides(Strand, NucleotideMap)
  when (length(Strand) == 0) ->
  NucleotideMap;

count_nucleotides(Strand, NucleotideMap) ->
  [Head | Tail] = Strand,
  Nucleotide = unicode:characters_to_list([Head]),
  CurrentCount = maps:get(Nucleotide, NucleotideMap) + 1,
  RevisedNucleotideMap = maps:merge(NucleotideMap, #{Nucleotide => CurrentCount}),
  count_nucleotides(Tail, RevisedNucleotideMap).

create_nucleotides_map() ->
  #{
    "A" => 0,
    "C" => 0,
    "G" => 0,
    "T" => 0
  }.

to_list(NucleotideMap) ->
  Nucleotides = maps:keys(NucleotideMap),
  lists:foldl(to_tuple_list(NucleotideMap), _Accumulator = [], Nucleotides).

to_tuple_list(NucleotidesMap) ->
  fun(Key, Accumulator) ->
    [{Key, maps:get(Key, NucleotidesMap)} | Accumulator]
  end.

