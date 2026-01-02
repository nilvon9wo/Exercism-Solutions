open Position

type count_context = {
  validator: position -> position -> bool;
}