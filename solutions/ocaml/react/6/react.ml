type callback_id = int

type 'value_type cell =
  | Input of {
      mutable value: 'value_type;
      eq: 'value_type -> 'value_type -> bool;
      mutable registered_callbacks: (callback_id * ('value_type -> unit)) list;
      mutable next_callback_id: int;
      mutable dependent_cells: 'value_type cell list;
    }
  | Compute of {
      mutable value: 'value_type;
      eq: 'value_type -> 'value_type -> bool;
      compute_fn: unit -> 'value_type;
      mutable registered_callbacks: (callback_id * ('value_type -> unit)) list;
      mutable next_callback_id: int;
      mutable dependent_cells: 'value_type cell list;
    }

let value_of = function
  | Input input_cell -> input_cell.value
  | Compute compute_cell -> compute_cell.value

let create_input_cell ~value ~eq =
  Input { value; eq; registered_callbacks = []; next_callback_id = 0; dependent_cells = [] }

let set_value input_cell new_value =
  match input_cell with
  | Input input ->
      if input.eq new_value input.value then ()
      else begin
        input.value <- new_value;
        let bfs_queue = Queue.create () in
        let visited_cells = Hashtbl.create 16 in
        List.iter (fun dependent_cell -> Queue.add dependent_cell bfs_queue) input.dependent_cells;
        while not (Queue.is_empty bfs_queue) do
          let current_cell = Queue.pop bfs_queue in
          if not (Hashtbl.mem visited_cells current_cell) then begin
            Hashtbl.add visited_cells current_cell ();
            match current_cell with
            | Compute compute_cell ->
                let updated_value = compute_cell.compute_fn () in
                if not (compute_cell.eq updated_value compute_cell.value) then begin
                  compute_cell.value <- updated_value;
                  List.iter (fun (_, callback_fn) -> callback_fn updated_value) compute_cell.registered_callbacks;
                  List.iter (fun dependent_cell -> Queue.add dependent_cell bfs_queue) compute_cell.dependent_cells
                end
            | Input _ -> ()
          end
        done
      end
  | Compute _ -> failwith "Cannot set value of a compute cell"

let create_compute_cell_1 input_cell ~f ~eq =
  let initial_value = f (value_of input_cell) in
  let compute_cell = Compute {
      value = initial_value;
      eq;
      compute_fn = (fun () -> f (value_of input_cell));
      registered_callbacks = [];
      next_callback_id = 0;
      dependent_cells = [];
    } in
  begin match input_cell with
  | Input input -> input.dependent_cells <- compute_cell :: input.dependent_cells
  | Compute compute_input -> compute_input.dependent_cells <- compute_cell :: compute_input.dependent_cells
  end;
  compute_cell

let create_compute_cell_2 input_cell_1 input_cell_2 ~f ~eq =
  let initial_value = f (value_of input_cell_1) (value_of input_cell_2) in
  let compute_cell = Compute {
      value = initial_value;
      eq;
      compute_fn = (fun () -> f (value_of input_cell_1) (value_of input_cell_2));
      registered_callbacks = [];
      next_callback_id = 0;
      dependent_cells = [];
    } in
  begin match input_cell_1 with
  | Input input -> input.dependent_cells <- compute_cell :: input.dependent_cells
  | Compute compute_input -> compute_input.dependent_cells <- compute_cell :: compute_input.dependent_cells
  end;
  begin match input_cell_2 with
  | Input input -> input.dependent_cells <- compute_cell :: input.dependent_cells
  | Compute compute_input -> compute_input.dependent_cells <- compute_cell :: compute_input.dependent_cells
  end;
  compute_cell

let add_callback cell ~k =
  match cell with
  | Input input_cell ->
      let assigned_id = input_cell.next_callback_id in
      input_cell.next_callback_id <- input_cell.next_callback_id + 1;
      input_cell.registered_callbacks <- (assigned_id, k) :: input_cell.registered_callbacks;
      assigned_id
  | Compute compute_cell ->
      let assigned_id = compute_cell.next_callback_id in
      compute_cell.next_callback_id <- compute_cell.next_callback_id + 1;
      compute_cell.registered_callbacks <- (assigned_id, k) :: compute_cell.registered_callbacks;
      assigned_id

let remove_callback cell callback_id =
  match cell with
  | Input input_cell ->
      input_cell.registered_callbacks <- List.filter (fun (existing_id, _) -> existing_id <> callback_id) input_cell.registered_callbacks
  | Compute compute_cell ->
      compute_cell.registered_callbacks <- List.filter (fun (existing_id, _) -> existing_id <> callback_id) compute_cell.registered_callbacks
