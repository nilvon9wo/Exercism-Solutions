import scala.annotation.tailrec

case class BinTree[A](value: A, left: Option[BinTree[A]], right: Option[BinTree[A]])

final case class ZipperState[A](
                                   focus: BinTree[A],
                                   parent: Option[ZipperState[A]],
                                   isLeftChild: Boolean
                               ) {

    def moveFocus(newFocus: BinTree[A]): ZipperState[A] =
        val updatedParent = updateParent(newFocus, parent, isLeftChild)
        ZipperState(newFocus, updatedParent, isLeftChild)

    @tailrec
    private def updateParent(
                                   newChild: BinTree[A],
                                   parent: Option[ZipperState[A]],
                                   isLeftChild: Boolean
                               ): Option[ZipperState[A]] = {

        parent match {
            case None => None

            case Some(pattern) =>
                val newParentFocus =
                    if (isLeftChild)
                        BinTree(pattern.focus.value, Some(newChild), pattern.focus.right)
                    else
                        BinTree(pattern.focus.value, pattern.focus.left, Some(newChild))

                val rebuiltParent =
                    ZipperState(
                        newParentFocus,
                        pattern.parent,
                        pattern.isLeftChild
                    )

                pattern.parent match
                    case None => Some(rebuiltParent)
                    case Some(_) =>
                        updateParent(newParentFocus, pattern.parent, pattern.isLeftChild)
        }
    }
}

object Zipper:
    def fromTree[A](bt: BinTree[A]): ZipperState[A] =
        ZipperState(bt, None, isLeftChild = false)
    def value[A](zipper: ZipperState[A]): A =
        zipper.focus.value

    def setValue[A](value: A, zipper: ZipperState[A]): ZipperState[A] = {
        val newFocus = BinTree(value, zipper.focus.left, zipper.focus.right)
        zipper.moveFocus(newFocus)
    }

    def setLeft[A](left: Option[BinTree[A]], zipper: ZipperState[A]): ZipperState[A] = {
        val newFocus = BinTree(zipper.focus.value, left, zipper.focus.right)
        zipper.moveFocus(newFocus)
    }

    def setRight[A](right: Option[BinTree[A]], zipper: ZipperState[A]): ZipperState[A] = {
        val newFocus = BinTree(zipper.focus.value, zipper.focus.left, right)
        zipper.moveFocus(newFocus)
    }

    private def move[A](
                           zipper: ZipperState[A],
                           nextFocus: ZipperState[A] => Option[BinTree[A]],
                           isLeftChild: Boolean
                       ): Option[ZipperState[A]] =
        nextFocus(zipper).map { child =>
            ZipperState(child, Some(zipper), isLeftChild)
        }

    def left[A](zipper: ZipperState[A]): Option[ZipperState[A]] =
        move(zipper, _.focus.left, isLeftChild = true)

    def right[A](zipper: ZipperState[A]): Option[ZipperState[A]] =
        move(zipper, _.focus.right, isLeftChild = false)

    def up[A](zipper: ZipperState[A]): Option[ZipperState[A]] =
        zipper.parent

    @tailrec
    def toTree[A](zipper: ZipperState[A]): BinTree[A] =
        zipper.parent match
            case None => zipper.focus
            case Some(p) => toTree(p)


