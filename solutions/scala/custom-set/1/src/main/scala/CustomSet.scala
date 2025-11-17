class CustomSet(val list: List[Int])

object CustomSet {
  def union(set1: CustomSet, set2: CustomSet): CustomSet =
    new CustomSet((set1.list ::: set2.list).distinct)

  def difference(set1: CustomSet, set2: CustomSet): CustomSet =
  new CustomSet(set1.list.filterNot(set2.list.contains))

  def intersection(set1: CustomSet, set2: CustomSet): CustomSet =
    new CustomSet(set1.list.filter(set2.list.contains))

  def insert(set: CustomSet, i: Int): CustomSet =
    new CustomSet((set.list ::: List(i)).distinct)

  def isEqual(set1: CustomSet, set2: CustomSet): Boolean =
    set1.list.sorted == set2.list.sorted

  def isDisjointFrom(set1: CustomSet, set2: CustomSet): Boolean =
    set1.list.forall(!set2.list.contains(_))

  def isSubsetOf(set1: CustomSet, set2: CustomSet): Boolean =
    set1.list.forall(set2.list.contains)

  def member(set: CustomSet, i: Int): Boolean =
    set.list.contains(i)

  def empty(set: CustomSet): Boolean =
    set.list.isEmpty

  def fromList(list: List[Int]): CustomSet =
    new CustomSet(list.distinct)
}

