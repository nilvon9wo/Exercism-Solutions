import Sgf.{Node, SgfNode, Tree}

object SgfTreeConverter {
  def convertToSgfTree(sgfTree: models.SgfTree): Tree[SgfNode] = {
    val rootNode = convertNode(sgfTree)
    Sgf
      .Node(
        rootNode
          .rootLabel, rootNode
          .subForest)
  }

  private def convertNode(node: models.SgfTree): Node[SgfNode] = {
    val convertedData = node.data.view.mapValues(_.toList)
                            .toMap
    val childrenNodes = node.children.map(convertNode)
    Sgf.Node(convertedData, childrenNodes.toList)
  }
}
