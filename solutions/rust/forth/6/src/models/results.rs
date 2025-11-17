use crate::Error;

pub type Result<T> = std::result::Result<T, Error>;

pub type ForthResult = Result<()>;
