public class Step {
    /**
     * 移动的块儿
     */
    public int i;

    /**
     * 移动的方向 只有L,R,U,D四种
     */
    public Direction direction;

    /**
     * 要移动的块儿的原横纵坐标
     */

    public Step(int i, Direction direction) {
        this.i = i;
        this.direction = direction;

    }

    @Override
    public String toString() {
        return  i +
                " " + direction;
    }
}
