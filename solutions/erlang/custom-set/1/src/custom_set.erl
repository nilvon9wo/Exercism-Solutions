-module(custom_set).
-record(custom_set, {
  members = []
}).

-export([add/2, contains/2, difference/2, disjoint/2, empty/1, equal/2, from_list/1, intersection/2, subset/2,
  union/2]).


add(Element, Set) ->
  from_lists(Set#custom_set.members, [Element]).

contains(Element, Set) ->
  lists:member(Element, Set#custom_set.members).

difference(Set1, Set2) ->
  from_list(lists:filter(
    not_contained_in(Set2),
    Set1#custom_set.members
  )).

disjoint(Set1, Set2) ->
  empty(Set1)
    or empty(Set2)
    or has_no_common_elements(Set1, Set2).

empty(Set) ->
  length(Set#custom_set.members) == 0.

equal(Set1, Set2) ->
  Set1#custom_set.members == Set2#custom_set.members.

from_list(List) ->
  #custom_set{members = lists:usort(List)}.

from_lists(SetMembers1, SetMembers2) ->
  from_list(lists:append([SetMembers1, SetMembers2])).

intersection(Set1, Set2) ->
  Subslist1 = find_common_members(Set1, Set2),
  Subslist2 = find_common_members(Set2, Set1),
  from_lists(Subslist1, Subslist2).

find_common_members(Set1, Set2) ->
  lists:filter(contained_in(Set2), Set1#custom_set.members).

subset(Set1, Set2) ->
  lists:all(contained_in(Set2), Set1#custom_set.members).

union(Set1, Set2) ->
  from_lists(Set1#custom_set.members, Set2#custom_set.members).

contained_in(Set) ->
  fun(Element) ->
    contains(Element, Set)
  end.

not_contained_in(Set) ->
  fun(Element) ->
    not(contains(Element, Set))
  end.

has_no_common_elements(Set1, Set2) ->
  has_no_members(Set1, Set2)
    and has_no_members(Set2, Set1).

has_no_members(Set1, Set2) ->
  not(has_any_members(Set1, Set2)).

has_any_members(Set1, Set2) ->
  SetMembers1 = Set1#custom_set.members,
  lists:any(contained_in(Set2), SetMembers1).
