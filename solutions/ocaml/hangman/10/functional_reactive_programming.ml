open React

type 'a signal = 'a React.S.t

type 'a reactive_signal = {
  current_value : 'a signal;
  update : 'a -> unit;
}

let create_signal initial_value : 'a reactive_signal =
  let signal, setter = React.S.create initial_value in
  {
        current_value = signal;
        update = setter
  }

let read_signal signal =
  React.S.value signal
