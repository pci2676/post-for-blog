package chap2.item1;

/**
 * 장점 1. 이름을 가질 수 있다.
 */
public class Ladder {
    public static Ladder left() {
        return new Ladder(Direction.LEFT);
    }

    public static Ladder right() {
        return new Ladder(Direction.RIGHT);
    }

    public static Ladder down() {
        return new Ladder(Direction.DOWN);
    }

    private Direction direction;

    public Ladder(Direction direction) {
        this.direction = direction;
    }

    enum Direction {
        LEFT,
        RIGHT,
        DOWN;
    }
}


