type callback_id = int

type 'value_type cell_base = {
  mutable current_value: 'value_type;
  equality_fn: 'value_type -> 'value_type -> bool;
  mutable registered_callbacks: (callback_id * ('value_type -> unit)) list;
  mutable next_callback_id: int;
  mutable dependent_cells: 'value_type cell list;
}
and 'value_type cell =
  | Input of 'value_type cell_base
  | Compute of {
      base: 'value_type cell_base;
      compute_fn: unit -> 'value_type;
    }

let get_base = function
     | Input base -> base
     | Compute { base; _ } -> base

let value_of cell =
    let base = get_base cell in
    base.current_value

let add_dependent_cell cell new_cell =
  let base = get_base cell in
  base.dependent_cells <- new_cell :: base.dependent_cells

let create_input_cell ~value ~eq =
  Input {
    current_value = value;
    equality_fn = eq;
    registered_callbacks = [];
    next_callback_id = 0;
    dependent_cells = []
  }

let create_base initial_value eq = {
    current_value = initial_value;
    equality_fn = eq;
    registered_callbacks = [];
    next_callback_id = 0;
    dependent_cells = []
}

let set_value input_cell new_value =
  match input_cell with
  | Input base ->
      if base.equality_fn new_value base.current_value then ()
      else
        begin
          base.current_value <- new_value;
          let bfs_queue = Queue.create () in
          let visited_cells = Hashtbl.create 16 in
          List.iter (fun dep -> Queue.add dep bfs_queue) base.dependent_cells;
          while not (Queue.is_empty bfs_queue) do
            let current_cell = Queue.pop bfs_queue in
            if not (Hashtbl.mem visited_cells current_cell) then begin
              Hashtbl.add visited_cells current_cell ();
              match current_cell with
              | Compute { base = dep_base; compute_fn } ->
                  let new_val = compute_fn () in
                  if not (dep_base.equality_fn new_val dep_base.current_value) then begin
                    dep_base.current_value <- new_val;
                    List.iter (fun (_, callback_fn) -> callback_fn new_val) dep_base.registered_callbacks;
                    List.iter (fun dep -> Queue.add dep bfs_queue) dep_base.dependent_cells
                  end
              | Input _ -> ()
            end
          done
        end
  | Compute _ -> failwith "Cannot set value of compute cell"

let create_compute_cell_1 dependency_cell ~f ~eq =
  let initial_value = f (value_of dependency_cell) in
  let base = create_base initial_value eq in
  let new_cell = Compute { base; compute_fn = (fun () -> f (value_of dependency_cell)) } in
  let _ = add_dependent_cell dependency_cell new_cell in
  new_cell

let create_compute_cell_2 dependency_cell_1 dependency_cell_2 ~f ~eq =
  let initial_value = f (value_of dependency_cell_1) (value_of dependency_cell_2) in
  let base = create_base initial_value eq in
  let new_cell = Compute { base; compute_fn = (fun () -> f (value_of dependency_cell_1) (value_of dependency_cell_2)) } in
  let _ = add_dependent_cell dependency_cell_1 new_cell in
  let _ = add_dependent_cell dependency_cell_2 new_cell in
  new_cell

let add_callback cell ~k =
  let base = get_base cell in
  let id = base.next_callback_id in
  base.next_callback_id <- id + 1;
  base.registered_callbacks <- (id, k) :: base.registered_callbacks;
  id

let remove_callback cell callback_id =
  let base = get_base cell in
  base.registered_callbacks <- List.filter (fun (id, _) -> id <> callback_id) base.registered_callbacks