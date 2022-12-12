import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Run {

    public static void main(String[] args) {
//        int[][] board = {{1, 2, 4, 5, 0}, {6, 15, 3, 14, 9}, {16, 10, 19, 20, 22}, {12, 13, 17, 24, 21}, {8, 18, 11, 7, 23}};
//        int[][] board = {{5, 12, 15, 3, 10}, {21, 4, 1, 9, 8}, {24, 13, 2, 14, 6}, {16, 17, 18, 23, 20,}, {7, 22, 19, 11, 0}};
//        int[][] board = {{13, 6, 11, 5}, {8, 2, 12, 4}, {15, 10, 14, 7}, {9, 1, 3, 0}};
//        int[][] board = {{8, 2, 3, 14}, {13, 11, 4, 12}, {5, 9, 1, 6}, {15, 7, 0, 10}};
//        int[][] board = {{10, 12, 15, 19, 8, 6}, {29, 28, 5, 23, 21, 26}, {13, 33, 3, 14, 17, 20}, {4, 24, 1, 22, 16, 34}, {2, 30, 27, 9, 18, 7}, {32, 31, 25, 11, 35, 0}};
//        int[][] board = {{1, 2, 6}, {4, 3, 5}, {7, 8, 0}};
//        int[][] board = {{3, 7, 4, 8,}, {1, 2, 0, 11}, {5, 6, 15, 10}, {9, 14, 0, 12}, {13, 17, 0, 16}};//有特殊块儿
        int[][] special = {{2, 0, 0}, {12, 2, 1}, {1, 2, 2}};
        int[][] board = {{2, 1}, {0, 3}};
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }

        ArrayList<Step> steps = Running(board);
//        for (int i = 0; i < steps.size(); i++) {
//            System.out.print(i + " ");
//            System.out.println(steps.get(i));
//        }

        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
    }


    //无特殊块儿的处理
    public static ArrayList<Step> Running(int[][] board) {
        int wide = board[0].length, high = board.length;
        ArrayList<Step> steps = new ArrayList<Step>();
        CanSolve canSolve = new CanSolve();
        if (!canSolve.canSolve(board)) {
            return steps;
        }
        //block依次归位
        //除倒数前两行的归位
        //除每行最后一个
        int[][] lock = new int[board.length][board[0].length];//锁定为1未锁定为0
        for (int i = 0; i < board.length - 2; i++) {
            for (int j = 0; j < board[0].length - 1; j++) {
                int temp = i * board[0].length + j + 1;
                if (!RightPosition(board, temp)) {
                    steps.addAll(BlockMove(board, temp, lock));
                }
                lock[i][j] = 1;
            }
            //让末尾块儿来到正确位置下一个
            if (!RightPosition(board, board[0].length * (i + 1))) {
                if (board[i][board[0].length - 1] == 0 && board[i + 1][board[0].length - 1] == board[0].length * (i + 1)) {
                    steps.addAll(BlockMove(board, (i + 1) * board[0].length, lock));
                    lock[i][board[0].length - 1] = 1;
                } else {
                    steps.addAll(LastBlockMove(board, (i + 1) * board[0].length, lock));
                    //让空格来到末尾块儿左边
                    if (board[i + 2][board[0].length - 1] == 0) {
                        board[i + 2][board[0].length - 1] = board[i + 2][board[0].length - 2];
                        steps.add(new Step((i + 2) * wide + board[0].length - 2, Direction.R));
                        board[i + 2][board[0].length - 2] = board[i + 1][board[0].length - 2];
                        steps.add(new Step((i + 1) * wide + board[0].length - 2, Direction.D));
                        board[i + 1][board[0].length - 2] = 0;
                    }
                    //末尾块儿归位
                    board[i + 1][board[0].length - 2] = board[i + 1][board[0].length - 3];
                    steps.add(new Step((i + 1) * wide + board[0].length - 3, Direction.R));
                    board[i + 1][board[0].length - 3] = board[i][board[0].length - 3];
                    steps.add(new Step((i) * wide + board[0].length - 3, Direction.D));
                    board[i][board[0].length - 3] = board[i][board[0].length - 2];
                    steps.add(new Step((i) * wide + board[0].length - 2, Direction.L));
                    board[i][board[0].length - 2] = board[i][board[0].length - 1];
                    steps.add(new Step((i) * wide + board[0].length - 1, Direction.L));
                    board[i][board[0].length - 1] = board[i + 1][board[0].length - 1];
                    steps.add(new Step((i + 1) * wide + board[0].length - 1, Direction.U));
                    board[i + 1][board[0].length - 1] = 0;
                    //锁定末尾块儿
                    lock[i][board[0].length - 1] = 1;
                    //解锁前两块儿
                    lock[i][board[0].length - 2] = 0;
                    lock[i][board[0].length - 3] = 0;
                    //前两块儿归位
                    steps.addAll(BlockMove(board, board[0].length * i + board[0].length - 1, lock));
                    steps.addAll(BlockMove(board, board[0].length * i + board[0].length - 2, lock));
                    //继续锁定
                    lock[i][board[0].length - 2] = 1;
                    lock[i][board[0].length - 3] = 1;
                }
            } else {
                lock[i][board[0].length - 1] = 1;
            }
        }
        //处理最后两行
        for (int i = 0; i < board[0].length - 2; i++) {
            steps.addAll(BlockMove(board, (board.length - 2) * board[0].length + i + 1, lock));
            lock[board[0].length - 2][i] = 1;
            if (!RightPosition(board, (board.length - 1) * board[0].length + i + 1)) {
                steps.addAll(RightBlockMove(board, (board.length - 1) * board[0].length + i + 1, lock));
                ArrayList<Step> temp = ZeroMove(board, (board.length - 1) * board[0].length + i, (board.length - 1) * board[0].length + i + 2, lock);
                steps.addAll(temp);
                NewBoard(board, temp);
                board[board.length - 1][i] = board[board.length - 2][i];
                steps.add(new Step((high - 2) * wide + i, Direction.D));
                board[board.length - 2][i] = board[board.length - 2][i + 1];
                steps.add(new Step((high - 2) * wide + i + 1, Direction.L));
                board[board.length - 2][i + 1] = board[board.length - 1][i + 1];
                steps.add(new Step((board.length - 1) * wide + i + 1, Direction.U));
                board[board.length - 1][i + 1] = board[board.length - 1][i + 2];
                steps.add(new Step((board.length - 1) * wide + i + 2, Direction.L));
                board[board.length - 1][i + 2] = board[board.length - 2][i + 2];
                steps.add(new Step((board.length - 2) * wide + i + 2, Direction.D));
                board[board.length - 2][i + 2] = board[board.length - 2][i + 1];
                steps.add(new Step((board.length - 2) * wide + i + 1, Direction.R));
                board[board.length - 2][i + 1] = board[board.length - 2][i];
                steps.add(new Step((board.length - 2) * wide + i, Direction.R));
                board[board.length - 2][i] = board[board.length - 1][i];
                steps.add(new Step((board.length - 1) * wide + i, Direction.U));
                board[board.length - 1][i] = board[board.length - 1][i + 1];
                steps.add(new Step((board.length - 1) * wide + i + 1, Direction.L));
                board[board.length - 1][i + 1] = 0;
                lock[board.length - 2][i] = 1;
                lock[board.length - 1][i] = 1;
            }
        }
        steps.addAll(BlockMove(board, board[0].length * (board.length - 1) - 1, lock));
        steps.addAll(BlockMove(board, board[0].length * (board.length - 1), lock));
        steps.addAll(BlockMove(board, board[0].length * board.length - 1, lock));
        return steps;
    }

    /**
     * right last block归位
     */
    public static ArrayList<Step> RightBlockMove(int[][] board, int block, int[][] lock) {
        ArrayList<Step> list = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == block) {
                    start = i * board[0].length + j;
                }
            }
        }
        Graph graph = new Graph(board.length * board[0].length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length - 1; j++) {
                if (unlock(lock, j, i) && unlock(lock, j + 1, i)) {
                    graph.addEdge(i * board[0].length + j, i * board[0].length + j + 1);
                }
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length - 1; j++) {
                if (unlock(lock, i, j) && unlock(lock, i, j + 1)) {
                    graph.addEdge(j * board[0].length + i, j * board[0].length + i + board[0].length);
                }
            }
        }
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, start);
        Iterable<Integer> integer = breadthFirstPaths.pathTo(block + 1);
        Iterator<Integer> iterator = integer.iterator();
        ArrayList<Integer> integers = new ArrayList<>();
        while (iterator.hasNext()) {
            integers.add(iterator.next());
        }
        //integers是block归位需要经过的空白格的路径（第一位为block的位置）
        for (int i = 1; i < integers.size(); i++) {
            //空白格归位：空白格->i位置
            ArrayList<Step> steps;
            steps = ZeroMove(board, integers.get(i), integers.get(i - 1), lock);

            //当前空白格参数
            int m = integers.get(i);
            int x = m % board[0].length, y = m / board[0].length;
            //当前block参数
            int xn = start % board[0].length, yn = start / board[0].length;
            Direction direction;
            if (x == xn) {
                if (y - yn == 1) {
                    direction = Direction.D;
                } else {
                    direction = Direction.U;
                }
            } else {
                if (x - xn == 1) {
                    direction = Direction.R;
                } else {
                    direction = Direction.L;
                }
            }

            //block移动到空白格
            Step moveToZero = new Step(start, direction);
            steps.add(moveToZero);
            list.addAll(steps);
            NewBoard(board, steps);
            start = m;
        }
        return list;
    }


    public static boolean unlock(int[][] lock, int x, int y) {
        return lock[y][x] == 0;
    }


    /**
     * last block归位
     */
    public static ArrayList<Step> LastBlockMove(int[][] board, int block, int[][] lock) {
        ArrayList<Step> list = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == block) {
                    start = i * board[0].length + j;
                }
            }
        }
        Graph graph = new Graph(board.length * board[0].length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length - 1; j++) {
                if (unlock(lock, j, i) && unlock(lock, j + 1, i)) {
                    graph.addEdge(i * board[0].length + j, i * board[0].length + j + 1);
                }
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length - 1; j++) {
                if (unlock(lock, i, j) && unlock(lock, i, j + 1)) {
                    graph.addEdge(j * board[0].length + i, j * board[0].length + i + board[0].length);
                }
            }
        }
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, start);
        Iterable<Integer> integer = breadthFirstPaths.pathTo(block + board[0].length - 1);
        Iterator<Integer> iterator = integer.iterator();
        ArrayList<Integer> integers = new ArrayList<>();
        while (iterator.hasNext()) {
            integers.add(iterator.next());
        }
        //integers是block归位需要经过的空白格的路径（第一位为block的位置）
        for (int i = 1; i < integers.size(); i++) {
            //空白格归位：空白格->i位置
            ArrayList<Step> steps;
            steps = ZeroMove(board, integers.get(i), integers.get(i - 1), lock);

            //当前空白格参数
            int m = integers.get(i);
            int x = m % board[0].length, y = m / board[0].length;
            //当前block参数
            int xn = start % board[0].length, yn = start / board[0].length;
            Direction direction;
            if (x == xn) {
                if (y - yn == 1) {
                    direction = Direction.D;
                } else {
                    direction = Direction.U;
                }
            } else {
                if (x - xn == 1) {
                    direction = Direction.R;
                } else {
                    direction = Direction.L;
                }
            }

            //block移动到空白格
            Step moveToZero = new Step(start, direction);
            steps.add(moveToZero);
            list.addAll(steps);
            NewBoard(board, steps);
            start = m;
        }
        return list;
    }


    /**
     * block归位
     */
    public static ArrayList<Step> BlockMove(int[][] board, int block, int[][] lock) {
        ArrayList<Step> list = new ArrayList<>();

        //找到起始点
        int start = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == block) {
                    start = i * board[0].length + j;
                }
            }
        }

        //根据锁定情况创建图
        Graph graph = new Graph(board.length * board[0].length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length - 1; j++) {
                if (unlock(lock, j, i) && unlock(lock, j + 1, i)) {
                    graph.addEdge(i * board[0].length + j, i * board[0].length + j + 1);
                }
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length - 1; j++) {
                if (unlock(lock, i, j) && unlock(lock, i, j + 1)) {
                    graph.addEdge(j * board[0].length + i, j * board[0].length + i + board[0].length);
                }
            }
        }

        //找到块儿归位要走的路径
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, start);
        Iterable<Integer> integer = breadthFirstPaths.pathTo(block - 1);
        Iterator<Integer> iterator = integer.iterator();
        ArrayList<Integer> integers = new ArrayList<>();
        while (iterator.hasNext()) {
            integers.add(iterator.next());
        }

        //integers是block归位需要经过的空白格的路径（第一位为block的位置）
        for (int i = 1; i < integers.size(); i++) {
            //空白格归位：空白格->i位置
            ArrayList<Step> steps;
            steps = ZeroMove(board, integers.get(i), integers.get(i - 1), lock);

            //当前空白格参数
            int m = integers.get(i);
            int x = m % board[0].length, y = m / board[0].length;
            //当前block参数
            int xn = start % board[0].length, yn = start / board[0].length;
            Direction direction;
            if (x == xn) {
                if (y - yn == 1) {
                    direction = Direction.D;
                } else {
                    direction = Direction.U;
                }
            } else {
                if (x - xn == 1) {
                    direction = Direction.R;
                } else {
                    direction = Direction.L;
                }
            }

            //block移动到空白格
            Step moveToZero = new Step(start, direction);
            steps.add(moveToZero);
            list.addAll(steps);
            NewBoard(board, steps);
            start = m;
        }
        return list;
    }

    /**
     * 在保证block不动的情况下，让空白格来到目标位置
     *
     * @param target zero的目标一维数组位置
     * @param block  block的一维数组位置
     */
    public static ArrayList<Step> ZeroMove(int[][] board, int target, int block, int[][] lock) {
        //找到zero的目前位置
        int start = 0;
        outer:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    start = board[0].length * i + j;
                    break outer;
                }
            }
        }

        //根据锁定块儿建图
        Graph graph = new Graph(board[0].length * board.length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length - 1; j++) {
                if (i * board[0].length + j + 1 != block && i * board[0].length + j != block && unlock(lock, j, i) && unlock(lock, j + 1, i)) {
                    graph.addEdge(i * board[0].length + j, i * board[0].length + j + 1);
                }
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length - 1; j++) {
                if (j * board[0].length + i != block && j * board[0].length + i + board[0].length != block && unlock(lock, i, j) && unlock(lock, i, j + 1)) {
                    graph.addEdge(j * board[0].length + i, j * board[0].length + i + board[0].length);
                }
            }
        }

        //找到zero归位的路径
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, start);
        Iterable<Integer> integer = breadthFirstPaths.pathTo(target);
        Iterator<Integer> iterator = integer.iterator();
        ArrayList<Integer> integers = new ArrayList<>();
        while (iterator.hasNext()) {
            integers.add(iterator.next());
        }

        //归位
        ArrayList<Step> steps = new ArrayList<>();
        for (int i = 1; i < integers.size(); i++) {
            //空白格参数
            int xn = start % board[0].length, yn = start / board[0].length;

            int temp = integers.get(i);
            int x = temp % board[0].length, y = temp / board[0].length;
            Direction direction;
            if (x == xn) {
                if (y - yn == 1) {
                    direction = Direction.U;
                } else {
                    direction = Direction.D;
                }
            } else {
                if (x - xn == 1) {
                    direction = Direction.L;
                } else {
                    direction = Direction.R;
                }
            }
            start = temp;
            Step step = new Step(temp, direction);
            steps.add(step);
        }
        return steps;
    }

    /**
     * 由steps得到归位后的board
     */
    public static void NewBoard(int[][] board, ArrayList<Step> steps) {
        for (Step step : steps) {
            int num = step.i;
            int y = num / board[0].length, x = num % board[0].length;
            int temp = board[y][x];
            switch (step.direction) {
                case D -> board[y + 1][x] = temp;
                case U -> board[y - 1][x] = temp;
                case R -> board[y][x + 1] = temp;
                case L -> board[y][x - 1] = temp;
            }
            board[y][x] = 0;
        }
    }


    public static boolean RightPosition(int[][] board, int block) {
        int high = board.length;
        int wide = board[0].length;

        int x = (block - 1) % wide;//block的横坐标
        int y = (block - 1) / wide;//block的纵坐标

        for (int i = 0; i < board.length; i++) {
            if (y == i) {
                for (int j = 0; j < board[i].length; j++) {
                    if (x == j) {
                        if (board[y][x] == block) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}



