open Base

type robot =
    { mutable current_name : string option }

let new_robot () =
    { current_name = None }

let assign_name robot =
    let name = Robot_name_generator.generate () in
    robot.current_name <- Some name;
    name

let name robot =
    match robot.current_name with
    | Some name -> name
    | None -> assign_name robot

let release_name robot =
    match robot.current_name with
    | None -> ()
    | Some name ->
        Robot_name_generator.release name;
        robot.current_name <- None

let reset robot =
    release_name robot
