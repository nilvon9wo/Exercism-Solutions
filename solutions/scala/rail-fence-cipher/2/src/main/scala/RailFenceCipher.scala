import Direction.Direction

case class Fence(codedMessage: String, rails: Int) {
  val posts: List[Post] = Post.from(codedMessage, rails)
}

case class Post(height: Int, sortOrder: Int, character: String = null) {
  def this(post: Post, character: String) {
    this(post.height, post.sortOrder, character)
  }
}

object Post {
  def from(message: String, rails: Int): List[Post] =
    this.createEmptyPosts(1 to message.length, rails)
      .sortWith(this.byHeightBeforeOrder)
      .zip(message.split(""))
      .map(this.putCharacterOnPost)

  private def byHeightBeforeOrder(post1: Post, post2: Post): Boolean =
    if (post1.height != post2.height) {
      post1.height < post2.height
    }
    else {
      post1.sortOrder < post2.sortOrder
    }

  private def putCharacterOnPost(postWithCharacter: (Post, String)): Post = {
    val (post, character) = postWithCharacter
    new Post(post, character)
  }

  @scala.annotation.tailrec
  private def createEmptyPosts(
                                messageRange: Range,
                                rails: Int,
                                currentDirection: Direction = Direction.Down,
                                currentRail: Int = 1,
                                accumulator: List[Post] = List()
                              ): List[Post] =
    if (messageRange.isEmpty) {
      accumulator
    }
    else {
      val (nextDirection, nextRail) = Direction.decideWhatsNext(currentDirection, currentRail, rails)
      this.createEmptyPosts(
        messageRange.tail,
        rails,
        nextDirection,
        nextRail,
        new Post(currentRail, messageRange.head) :: accumulator
      )
    }
}

object RailFenceCipher {

  import Direction._

  @scala.annotation.tailrec
  def encode(
              message: String,
              rails: Int,
              currentDirection: Direction = Down,
              currentRail: Int = 1,
              oldEncryptedMap: Map[Int, String] = Map[Int, String]()
            ): String =
    if (message.isBlank) {
      this.toEncryptedString(oldEncryptedMap)
    }
    else {
      val (first, rest) = message.splitAt(1)
      val newEncryptedMap = oldEncryptedMap.updated(
        currentRail,
        oldEncryptedMap.getOrElse(currentRail, "") + first
      )
      val (nextDirection, nextRail) = Direction.decideWhatsNext(currentDirection, currentRail, rails)
      this.encode(rest, rails, nextDirection, nextRail, newEncryptedMap)
    }

  def decode(message: String, rails: Int): String =
    Fence(message, rails)
      .posts
      .sortBy(_.sortOrder)
      .map(_.character)
      .mkString("")


  @scala.annotation.tailrec
  private def toEncryptedString(
                                 oldEncryptedMap: Map[Int, String],
                                 oldEncryptedMessage: String = ""
                               ): String =
    if (oldEncryptedMap.isEmpty) {
      oldEncryptedMessage
    }
    else {
      val rail = oldEncryptedMap.keys.min
      val newEncryptedMessage = oldEncryptedMessage + oldEncryptedMap(rail)
      val newEncryptedMap = oldEncryptedMap - rail
      this.toEncryptedString(newEncryptedMap, newEncryptedMessage)
    }
}

object Direction extends Enumeration {
  type Direction = Value
  val Up, Down = Value

  def decideWhatsNext(
                       currentDirection: Direction,
                       currentRail: Int,
                       rails: Int
                     ): (Direction, Int) =
    currentDirection match {
      case Up ⇒
        if (currentRail == rails) {
          (Down, currentRail - 1)
        }
        else {
          (Up, currentRail + 1)
        }

      case Down ⇒
        if (currentRail == 1) {
          (Up, currentRail + 1)
        }
        else {
          (Down, currentRail - 1)
        }
    }
}