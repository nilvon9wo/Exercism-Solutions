/// Create an empty vector
pub fn create_empty() -> Vec<u8> {
    Vec::<u8>::new()
}

/// Create a buffer of `count` zeroes.
///
/// Applications often use buffers when serializing data to send over the network.
pub fn create_buffer(count: usize) -> Vec<u8>
{
    let mut buffer = create_empty();
    while buffer.len() < count {
           buffer.push(0);
    }
    buffer
}

const DEFAULT_FIBANACCI_LENGTH: usize = 5;

/// Create a vector containing the first five elements of the Fibonacci sequence.
///
/// Fibonacci's sequence is the list of numbers where the next number is a sum of the previous two.
/// Its first five elements are `1, 1, 2, 3, 5`.
pub fn fibonacci() -> Vec<u8> {
    generate_fibonacci(DEFAULT_FIBANACCI_LENGTH)
}

fn generate_fibonacci(count: usize) -> Vec<u8> {
    let mut buffer = create_empty();
    let mut value1 = 1;
    let mut value2 = 1;
    while buffer.len() < count {
        let next_value = value1 + value2;
        buffer.push(next_value);
        value1 = value2;
        value2 = next_value;
    }
    buffer
}
