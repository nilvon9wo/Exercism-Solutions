package models

class SgfGroup(nodes: List[SgfNodeFamily]) {
  def toTree: SgfTree = {
    val parentNodeFamily: SgfNodeFamily = nodes.head
    if (parentNodeFamily.children.nonEmpty) {
      parentNodeFamily.toTree
    } else {
      val children: List[SgfTree] = nodes
        .tail
        .map(node => node.toTree)
      new SgfTree(parentNodeFamily.parent, children)
    }
  }
}
