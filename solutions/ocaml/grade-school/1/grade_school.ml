open Base

module IntMap = Map.M(Int)

type school = string list IntMap.t

let empty_school : school = Map.empty (module Int)

let get_students_in_grade grade school =
  grade
  |> Map.find school
  |> Option.value ~default:[]

let update_student_list new_student grade school =
  let students = (get_students_in_grade grade school) in
  [new_student]
  |> List.append students
  |> List.sort ~compare:String.compare

let add student grade school =
  let students = (update_student_list student grade school) in
  school
  |> Map.set ~key:grade ~data:students

(* Get all students in a grade *)
let grade grade school =
  grade
  |> Map.find school
  |> Option.value ~default:[]

let compare_grade_entries = fun (grade1, _) (grade2, _) ->
  Int.compare grade1 grade2

let sort_names_in_grade = fun (_, names) ->
    List.sort names ~compare:String.compare

(* Get a sorted list of all students in the school *)
let roster school =
  school
  |> Map.to_alist
  |> List.sort ~compare:compare_grade_entries
  |> List.concat_map ~f:sort_names_in_grade

let sort_grade_entry_names (grade, names) =
  let sorted_students = sort_names_in_grade (grade, names) in
  (grade, sorted_students)

let map_sort_grade_entries = List.map ~f:sort_grade_entry_names

let sorted school =
  school
  |> Map.to_alist
  |> map_sort_grade_entries
  |> Map.of_alist_exn (module Int)
