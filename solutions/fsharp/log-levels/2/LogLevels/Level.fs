module Logging.Level

open System
open Microsoft.FSharp.Reflection

type Level =
    | Info
    | Warning
    | Error

let levelNames = FSharpType.GetUnionCases(typeof<Level>) |> Array.map (fun case -> case.Name.ToUpper())

let levelToString (level : Level) : string =
    FSharpType.GetUnionCases(typeof<Level>)
    |> Array.tryFind (fun case -> FSharpValue.MakeUnion(case, [||]) :?> Level = level)
    |> Option.map (fun case -> case.Name.ToLower())
    |> Option.defaultValue "unknown"

let parseLevel (levelString : string) : Result<Level, string> =
    let levelCases =
        FSharpType.GetUnionCases(typeof<Level>)
        |> Array.filter (fun case -> String.Equals(case.Name, levelString, StringComparison.OrdinalIgnoreCase))

    match levelCases with
    | [| case |] -> Ok (FSharpValue.MakeUnion(case, [||]) :?> Level)
    | _ -> Result.Error "Invalid level"