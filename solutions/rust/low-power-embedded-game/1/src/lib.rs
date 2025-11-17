const ORIGIN: (i16, i16) = (0, 0);

pub fn divmod(dividend: i16, divisor: i16) -> (i16, i16) {
    let quotient = dividend / divisor;
    let remainder = dividend % divisor;
    (quotient, remainder)
}

pub fn evens<T>(iter: impl Iterator<Item = T>) -> impl Iterator<Item = T>
{
    iter.enumerate()
            .filter(|(i, _value)| i % 2 == 0)
            .map(|(_i, value)| value)
}

pub struct Position(pub i16, pub i16);
impl Position {
    pub fn manhattan(&self) -> i16 {
        let (p1, p2) = ORIGIN;
        let Position(q1, q2) = self;
        (p1 - q1).abs() + (p2 - q2).abs()
    }
}
