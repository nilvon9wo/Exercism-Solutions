module ValentinesDay

type Approval =
    | Yes
    | No
    | Maybe

type Cuisine =
    | Korean
    | Turkish

let rateRestaurant(cuisine: Cuisine): Approval =
    match cuisine with
    | Korean -> Approval.Yes
    | Turkish -> Approval.Maybe

type Genre =
    | Crime
    | Horror
    | Romance
    | Thriller

let rateMovie(genre: Genre): Approval =
    match genre with
    | Romance -> Approval.Yes
    | _ -> Approval.No

type Activity =
    | BoardGame
    | Chill
    | Movie of Genre
    | Restaurant of Cuisine
    | Walk of int

let checkDistanceApproval kilometers =
    match kilometers with
    | d when d < 3 -> Yes
    | d when d >= 3 && d < 5 -> Maybe
    | _ -> No

let rateActivity (activity: Activity): Approval =
    match activity with
    | BoardGame -> Approval.No
    | Chill -> Approval.No
    | Movie genre -> rateMovie genre
    | Restaurant cuisine -> rateRestaurant cuisine
    | Walk kilometers -> checkDistanceApproval kilometers
