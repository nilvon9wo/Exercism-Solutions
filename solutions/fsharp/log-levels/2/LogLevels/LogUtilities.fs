module Logging.LogUtilities

open System.Text.RegularExpressions
open Logging.Level
open Logging.LogEntry

let logPattern = Regex("^\\[(\\w+)\\]: (.+)$")

let parseLogEntry (input : string) : Result<LogEntry, string> =
    match logPattern.Match(input) with
    | logMatch when logMatch.Success ->
        parseLevel logMatch.Groups[1].Value
        |> Result.bind (fun level -> Ok (LogEntry(level, logMatch.Groups[2].Value.Trim())))
    | _ -> Result.Error "Invalid log entry format"
    
let handleLogEntry<'a> (logLine: string) (successFn : LogEntry -> 'a) : string =
    match parseLogEntry logLine with
    | Ok logEntry -> (successFn logEntry :> obj) :?> string
    | Result.Error errorMessage -> failwith errorMessage

let message (logLine: string): string =
    handleLogEntry logLine (fun logEntry -> logEntry.Message)

let logLevel (logLine: string): string =
    handleLogEntry logLine (fun logEntry -> levelToString logEntry.Level)

let reformat (logLine: string): string =
    handleLogEntry logLine (fun logEntry -> $"%s{logEntry.Message} (%s{levelToString logEntry.Level})")