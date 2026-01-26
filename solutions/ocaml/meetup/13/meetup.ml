type schedule =
    First | Second | Third | Fourth | Teenth | Last
let to_schedule_t (sched : schedule) : Schedule.t =
  match sched with
  | First -> Schedule.First
  | Second -> Schedule.Second
  | Third -> Schedule.Third
  | Fourth -> Schedule.Fourth
  | Teenth -> Schedule.Teenth
  | Last -> Schedule.Last

type weekday =
  | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
let to_weekday_t (weekday : weekday) : Weekday.t =
  match weekday with
  | Monday -> Weekday.Monday
  | Tuesday -> Weekday.Tuesday
  | Wednesday -> Weekday.Wednesday
  | Thursday -> Weekday.Thursday
  | Friday -> Weekday.Friday
  | Saturday -> Weekday.Saturday
  | Sunday -> Weekday.Sunday

type date = int * int * int  (* year, month, day *)
let to_meetup_date (date : Date.t) : date =
    match date.day with
    | Some day -> (date.year, date.month, day)
    | None -> invalid_arg "Date.to_meetup_date: day is None"

let meetup_day input_schedule input_weekday ~year ~month =
  let result = Meetup_logic.compute_date ~context:{
                        schedule = to_schedule_t input_schedule;
                        weekday = to_weekday_t input_weekday;
                        date = Date.make_partial ~year ~month;
                      } in
  to_meetup_date result
