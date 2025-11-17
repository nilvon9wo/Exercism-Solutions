#[macro_export]
macro_rules! hashmap {
    () => {
        ::std::collections::HashMap::new()
    };
    ($($key:expr => $value:expr),+ $(,)?) => {
        {
            let mut hashmap = ::std::collections::HashMap::new();
            $(
                hashmap.insert($key, $value);
            )*
            hashmap
        }
    };
}
