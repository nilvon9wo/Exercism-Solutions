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
  | Input c -> c.value
  | Compute c -> c.value

let create_input_cell ~value ~eq =
  Input { value; eq; callbacks = []; next_id = 0; dependents = [] }

let set_value cell new_value =
  match cell with
  | Input c ->
      if c.eq new_value c.value then ()
      else begin
        c.value <- new_value;
        (* BFS/Topological propagation *)
        let queue = Queue.create () in
        let visited = Hashtbl.create 16 in
        List.iter (fun dep -> Queue.add dep queue) c.dependents;
        while not (Queue.is_empty queue) do
          let current = Queue.pop queue in
          if not (Hashtbl.mem visited current) then begin
            Hashtbl.add visited current ();
            match current with
            | Compute cc ->
                let new_val = cc.f () in
                if not (cc.eq new_val cc.value) then begin
                  cc.value <- new_val;
                  List.iter (fun (_, k) -> k new_val) cc.callbacks;
                  List.iter (fun dep -> Queue.add dep queue) cc.dependents
                end
            | Input _ -> ()
          end
        done
      end
  | Compute _ -> failwith "Cannot set value of compute cell"

let create_compute_cell_1 dep ~f ~eq =
  let initial_value = f (value_of dep) in
  let compute_cell =
    Compute
      {
        value = initial_value;
        eq;
        f = (fun () -> f (value_of dep));
        callbacks = [];
        next_id = 0;
        dependents = [];
      }
  in
  begin match dep with
  | Input d -> d.dependents <- compute_cell :: d.dependents
  | Compute d -> d.dependents <- compute_cell :: d.dependents
  end;
  compute_cell

let create_compute_cell_2 dep1 dep2 ~f ~eq =
  let initial_value = f (value_of dep1) (value_of dep2) in
  let compute_cell =
    Compute
      {
        value = initial_value;
        eq;
        f = (fun () -> f (value_of dep1) (value_of dep2));
        callbacks = [];
        next_id = 0;
        dependents = [];
      }
  in
  begin match dep1 with
  | Input d -> d.dependents <- compute_cell :: d.dependents
  | Compute d -> d.dependents <- compute_cell :: d.dependents
  end;
  begin match dep2 with
  | Input d -> d.dependents <- compute_cell :: d.dependents
  | Compute d -> d.dependents <- compute_cell :: d.dependents
  end;
  compute_cell

let add_callback cell ~k =
  match cell with
  | Input c ->
      let id = c.next_id in
      c.next_id <- c.next_id + 1;
      c.callbacks <- (id, k) :: c.callbacks;
      id
  | Compute c ->
      let id = c.next_id in
      c.next_id <- c.next_id + 1;
      c.callbacks <- (id, k) :: c.callbacks;
      id

let remove_callback cell id =
  match cell with
  | Input c -> c.callbacks <- List.filter (fun (cid, _) -> cid <> id) c.callbacks
  | Compute c -> c.callbacks <- List.filter (fun (cid, _) -> cid <> id) c.callbacks
