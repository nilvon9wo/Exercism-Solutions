package models

class SgfTree(val data: SgfNode, val children: List[SgfTree]) {
  def this(data: SgfNode) = {
    this(data, List.empty)
  }
}
