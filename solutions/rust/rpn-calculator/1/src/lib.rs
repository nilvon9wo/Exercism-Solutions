use std::mem::discriminant;

#[derive(Debug, Clone)]
pub enum CalculatorInput {
    Add,
    Subtract,
    Multiply,
    Divide,
    Value(i32),
}

pub fn evaluate(inputs: &[CalculatorInput]) -> Option<i32> {
    match inputs {
        [CalculatorInput::Value(x)] => Some(*x),

        [
        CalculatorInput::Value(x),
        CalculatorInput::Value(y),
        operator,
        tail @ ..
        ]
        if discriminant(operator) != discriminant(&CalculatorInput::Value(0)) => {
            let first_result = calculate(x, y, operator);
            let mut revised_values = [first_result].to_vec();
            revised_values.extend(tail.to_vec());
            evaluate(&revised_values[..])
        }

        [
        CalculatorInput::Value(x),
        CalculatorInput::Value(y),
        CalculatorInput::Value(z),
        operator1,
        operator2,
        tail @ ..
        ]
        if discriminant(operator1) != discriminant(&CalculatorInput::Value(0))
                && discriminant(operator2) != discriminant(&CalculatorInput::Value(0))
        => {
            if let CalculatorInput::Value(first_result) = calculate(y, z, operator1) {
                let second_result = calculate(x, &first_result, operator2);
                let mut revised_values = [second_result].to_vec();
                revised_values.extend(tail.to_vec());
                evaluate(&revised_values[..])
            } else {
                None
            }
        }

        _ => None
    }
}

fn calculate(x: &i32, y: &i32, operator: &CalculatorInput) -> CalculatorInput {
    let result = match operator {
        CalculatorInput::Add => *x + *y,
        CalculatorInput::Subtract => *x - *y,
        CalculatorInput::Multiply => *x * *y,
        CalculatorInput::Divide => *x / *y,
        _ => panic!("Invalid operator!")
    };
    CalculatorInput::Value(result)
}
