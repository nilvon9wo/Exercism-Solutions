object FlowerField:
    def annotate(garden: List[String]): List[String] =
        Board(garden.toVector)
            .render