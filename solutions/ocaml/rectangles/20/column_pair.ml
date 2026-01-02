open Position

type column_pair = {
  left: position;
  rights: position list;
}

let create left rights = {
    left;
    rights
}