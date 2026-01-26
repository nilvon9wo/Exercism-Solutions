open React

type 'a signal = 'a React.S.t

let create_signal initial_value =
  React.S.create initial_value

let read_signal signal =
  React.S.value signal
