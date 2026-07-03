class Robot {
    private GridPosition position;
    private Orientation orientation;

    Robot(GridPosition initialPosition, Orientation initialOrientation) {
        this.position = initialPosition;
        this.orientation = initialOrientation;
    }

    GridPosition getGridPosition() {
        return position;
    }

    Orientation getOrientation() {
        return orientation;
    }

    void advance() {
        switch (orientation) {
            case NORTH:
                position = new GridPosition(position.x, position.y + 1);
                break;
            case SOUTH:
                position = new GridPosition(position.x, position.y - 1);
                break;
            case EAST:
                position = new GridPosition(position.x + 1, position.y);
                break;
            case WEST:
                position = new GridPosition(position.x - 1, position.y);
                break;
        }
    }

    void turnLeft() {
        switch (orientation) {
            case NORTH:
                orientation = Orientation.WEST;
                break;
            case WEST:
                orientation = Orientation.SOUTH;
                break;
            case SOUTH:
                orientation = Orientation.EAST;
                break;
            case EAST:
                orientation = Orientation.NORTH;
                break;
        }
    }

    void turnRight() {
        switch (orientation) {
            case NORTH:
                orientation = Orientation.EAST;
                break;
            case EAST:
                orientation = Orientation.SOUTH;
                break;
            case SOUTH:
                orientation = Orientation.WEST;
                break;
            case WEST:
                orientation = Orientation.NORTH;
                break;
        }
    }

    void simulate(String instructions) {
        for (char character : instructions.toCharArray()) {
            switch (character) {
                case 'A':
                    advance();
                    break;
                case 'L':
                    turnLeft();
                    break;
                case 'R':
                    turnRight();
                    break;
            }
        }
    }
}