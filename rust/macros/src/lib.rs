#[macro_export]
macro_rules! hashmap {
    // Empty hashmap
    () => {
        ::std::collections::HashMap::new()
    };
    
    // Single key-value pair with trailing comma
    ($key:expr => $value:expr,) => {
        {
            let mut hm = ::std::collections::HashMap::new();
            hm.insert($key, $value);
            hm
        }
    };
    
    // Single key-value pair without trailing comma
    ($key:expr => $value:expr) => {
        {
            let mut hm = ::std::collections::HashMap::new();
            hm.insert($key, $value);
            hm
        }
    };
    
    // Multiple key-value pairs with trailing comma
    ($($key:expr => $value:expr,)+) => {
        {
            let mut hm = ::std::collections::HashMap::new();
            $(
                hm.insert($key, $value);
            )+
            hm
        }
    };
    
    // Multiple key-value pairs without trailing comma
    ($($key:expr => $value:expr),+) => {
        {
            let mut hm = ::std::collections::HashMap::new();
            $(
                hm.insert($key, $value);
            )+
            hm
        }
    };
}
