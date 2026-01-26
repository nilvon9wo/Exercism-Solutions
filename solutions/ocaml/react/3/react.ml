type callback_id = int

type 'a cell =
  | Input of {
      mutable value: 'a;
      eq: 'a -> 'a -> bool;
      mutable callbacks: (callback_id * ('a -> unit)) list;
      mutable next_id: int;
      mutable dependents: 'a cell list;
    }
  | Compute of {
      mutable value: 'a;
      eq: 'a -> 'a -> bool;
      f: unit -> 'a;
      mutable callbacks: (callback_id * ('a -> unit)) list;
      mutable next_id: int;
      mutable dependents: 'a cell list;
    }

let value_of = function
  | Input input_data -> input_data.value
  | Compute compute_data -> compute_data.value

let rec propagate cell =
  match cell with
  | Input input_data ->
      List.iter (fun dependent ->
        match dependent with
        | Compute compute_data ->
            let new_value = compute_data.f () in
            if not (compute_data.eq new_value compute_data.value) then begin
              compute_data.value <- new_value;
              List.iter (fun (_, callback) -> callback new_value) compute_data.callbacks;
              propagate dependent
            end
        | Input _ -> ()
      ) input_data.dependents
  | Compute compute_data ->
      List.iter (fun dependent ->
        match dependent with
        | Compute compute_data2 ->
            let new_value = compute_data2.f () in
            if not (compute_data2.eq new_value compute_data2.value) then begin
              compute_data2.value <- new_value;
              List.iter (fun (_, callback) -> callback new_value) compute_data2.callbacks;
              propagate dependent
            end
        | Input _ -> ()
      ) compute_data.dependents

let create_input_cell ~value ~eq =
  Input { value; eq; callbacks = []; next_id = 0; dependents = [] }

let set_value cell new_value =
  match cell with
  | Input input_data ->
      if input_data.eq new_value input_data.value then ()
      else
        begin
          input_data.value <- new_value;
          (* BFS/Topological propagation *)
          let update_queue = Queue.create () in
          let visited_cells = Hashtbl.create 16 in
          List.iter (fun dependent -> Queue.add dependent update_queue) input_data.dependents;
          while not (Queue.is_empty update_queue) do
            let current_cell = Queue.pop update_queue in
            if not (Hashtbl.mem visited_cells current_cell) then begin
              Hashtbl.add visited_cells current_cell ();
              match current_cell with
              | Compute compute_data ->
                  let new_value = compute_data.f () in
                  if not (compute_data.eq new_value compute_data.value) then begin
                    compute_data.value <- new_value;
                    List.iter (fun (_, callback) -> callback new_value) compute_data.callbacks;
                    List.iter (fun dependent -> Queue.add dependent update_queue) compute_data.dependents
                  end
              | Input _ -> ()
            end
          done
        end
  | Compute _ -> failwith "Cannot set value of compute cell"

let create_compute_cell_1 dependency ~f ~eq =
  let initial_value = f (value_of dependency) in
  let compute_cell =
    Compute {
      value = initial_value;
      eq;
      f = (fun () -> f (value_of dependency));
      callbacks = [];
      next_id = 0;
      dependents = [];
    }
  in
  begin
    match dependency with
    | Input input_data -> input_data.dependents <- compute_cell :: input_data.dependents
    | Compute compute_data -> compute_data.dependents <- compute_cell :: compute_data.dependents
  end;
  compute_cell

let create_compute_cell_2 dep1 dep2 ~f ~eq =
  let initial_value = f (value_of dep1) (value_of dep2) in
  let compute_cell =
    Compute {
      value = initial_value;
      eq;
      f = (fun () -> f (value_of dep1) (value_of dep2));
      callbacks = [];
      next_id = 0;
      dependents = [];
    }
  in
  let register_dependent dependency =
    match dependency with
    | Input input_data -> input_data.dependents <- compute_cell :: input_data.dependents
    | Compute compute_data -> compute_data.dependents <- compute_cell :: compute_data.dependents
  in
  register_dependent dep1;
  register_dependent dep2;
  compute_cell

let add_callback cell ~k =
  match cell with
  | Input input_data ->
      let id = input_data.next_id in
      input_data.next_id <- input_data.next_id + 1;
      input_data.callbacks <- (id, k) :: input_data.callbacks;
      id
  | Compute compute_data ->
      let id = compute_data.next_id in
      compute_data.next_id <- compute_data.next_id + 1;
      compute_data.callbacks <- (id, k) :: compute_data.callbacks;
      id

let remove_callback cell callback_id =
  match cell with
  | Input input_data ->
      input_data.callbacks <- List.filter (fun (id, _) -> id <> callback_id) input_data.callbacks
  | Compute compute_data ->
      compute_data.callbacks <- List.filter (fun (id, _) -> id <> callback_id) compute_data.callbacks
