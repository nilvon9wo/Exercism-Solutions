open Base

let annotate (lines : string list) : string list =
    Annotator.annotate ~lines
