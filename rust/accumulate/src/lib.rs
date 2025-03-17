/// Applies a function to each element of the input vector and returns a new vector with the results.
///
/// This is a custom implementation of the map operation without using the standard library's map function.
pub fn map<T, U, F>(input: Vec<T>, mut function: F) -> Vec<U>
where
    F: FnMut(T) -> U,
{
    let mut result = Vec::with_capacity(input.len());
    
    for item in input {
        result.push(function(item));
    }
    
    result
}
