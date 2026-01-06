type t = {
    row : int;
    column : int;
}

let create ~row ~column = {
    row;
    column;
}

let neighbors (coordinate : t) : t list =
    let row = coordinate.row in
    let column = coordinate.column in
    [
        create ~row:(row-1) ~column:(column-1);
        create ~row:(row-1) ~column:column;
        create ~row:(row-1) ~column:(column+1);
        create ~row:row   ~column:(column-1);
        create ~row:row   ~column:(column+1);
        create ~row:(row+1) ~column:(column-1);
        create ~row:(row+1) ~column:column;
        create ~row:(row+1) ~column:(column+1);
    ]
