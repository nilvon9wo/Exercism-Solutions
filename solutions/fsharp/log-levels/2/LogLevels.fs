module LogLevels

open Logging.Level
open Logging.LogUtilities

let message (logLine: string): string =
    handleLogEntry logLine (fun logEntry -> logEntry.Message)

let logLevel (logLine: string): string =
    handleLogEntry logLine (fun logEntry -> levelToString logEntry.Level)

let reformat (logLine: string): string =
    handleLogEntry logLine (fun logEntry -> $"%s{logEntry.Message} (%s{levelToString logEntry.Level})")
