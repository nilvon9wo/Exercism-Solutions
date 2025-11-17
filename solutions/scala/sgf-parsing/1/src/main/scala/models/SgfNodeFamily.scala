package models

class SgfNodeFamily(val parent: SgfNode, val children: List[SgfGroup] = List.empty) {
  def toTree: SgfTree = {
    if (children.isEmpty) {
      new SgfTree(parent)
    }
    else {
      new SgfTree(parent, children.map(_.toTree))
    }
  }
}
