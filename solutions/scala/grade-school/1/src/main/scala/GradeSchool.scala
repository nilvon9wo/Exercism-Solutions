class School {
  private type StudentsByGrade = Map[Int, Seq[String]]
  private var studentsByGrade: StudentsByGrade = Map[Int, Seq[String]]()

  def add(name: String, gradeLevel: Int): Unit = {
    val gradeStudents: Seq[String] = grade(gradeLevel) :+ name
    studentsByGrade = studentsByGrade + {
      gradeLevel -> gradeStudents
    }
  }

  def grade(gradeLevel: Int): Seq[String] =
    studentsByGrade.get(gradeLevel) match {
      case Some(students) => students
      case _              => Seq.empty
    }

  def db: StudentsByGrade = studentsByGrade

  def sorted: StudentsByGrade =
    studentsByGrade.toSeq
                   .sortBy {
                             case (gradeLevel, _) => gradeLevel
                           }
                   .map {
                          case (gradeLevel, students) => gradeLevel -> students.sorted
                        }
                   .toMap
}

