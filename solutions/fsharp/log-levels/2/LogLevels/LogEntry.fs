module Logging.LogEntry

type LogEntry(level: Level.Level, message: string) =
    member this.Level = level
    member this.Message = message

    override this.ToString() =
        $"{this.Message} ({this.Level.ToString().ToLower()})"