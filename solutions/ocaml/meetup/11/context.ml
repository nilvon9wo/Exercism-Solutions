type t = {
    schedule : Schedule.t;
    weekday: Weekday.t;
    date: Date.t;
}

let set_date ~(context: t) ~(day: int) : t =
    {
        context with
        date = {context.date with day = Some day}
    }