module LogLevels

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
    
type LogEntry(level: Level, message: string) =
    member this.Level = level
    member this.Message = message

    override this.ToString() =
        $"{this.Message} ({this.Level.ToString().ToLower()})"

let logPattern = System.Text.RegularExpressions.Regex("^\\[(\\w+)\\]: (.+)$")
 
let parseLevel (levelString : string) : Result<Level, string> =
    let levelCases =
        FSharpType.GetUnionCases(typeof<Level>)
        |> Array.filter (fun case -> String.Equals(case.Name, levelString, StringComparison.OrdinalIgnoreCase))

    match levelCases with
    | [| case |] -> Ok (FSharpValue.MakeUnion(case, [||]) :?> Level)
    | _ -> Result.Error "Invalid level"

let parseLogEntry (input : string) : Result<LogEntry, string> =
    match logPattern.Match(input) with
    | logMatch when logMatch.Success ->
        parseLevel logMatch.Groups[1].Value
        |> Result.bind (fun level -> Ok (LogEntry(level, logMatch.Groups[2].Value.Trim())))
    | _ -> Result.Error "Invalid log entry format"
    
let message (logLine: string): string =
    match parseLogEntry logLine with
    | Ok logEntry -> logEntry.Message
    | Result.Error errorMessage -> failwith errorMessage
let logLevel(logLine: string): string =
    match parseLogEntry logLine with
    | Ok logEntry -> levelToString logEntry.Level
    | Result.Error errorMessage -> failwith errorMessage

let reformat(logLine: string): string =
    match parseLogEntry logLine with
    | Ok logEntry -> logEntry.ToString()
    | Result.Error errorMessage -> failwith errorMessage
