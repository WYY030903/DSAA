import java.awt.*;
import java.util.ArrayList;

public class CanSolve {
    /**
     * 判断数字华容道是否可解
     */
    public boolean canSolve(int[][] board) {
        int zero = 0;
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (ints[j] == 0) {
                    zero++;
                }
            }
        }
        int[] A = new int[board.length * board[0].length - zero];
        ArrayList<Point> points = new ArrayList<>();
        int flag = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    Point point = new Point(i, j);
                    points.add(point);
                    flag++;
                } else {
                    A[i * board[0].length + j - flag] = board[i][j];
                }
            }
        }
        //若格子列数为奇数，则逆序数必须为偶数
        if (board[0].length % 2 == 1) {
            return reversePairs(A) % 2 == 0;
        } else {
            if (reversePairs(A) % 2 == 0) {
                //差值的和
                int x = 0;
                for (Point point : points) {
                    x += point.x - (board.length - 1);
                }
                return x % 2 == 0;
            } else {
                int x = 0;
                for (Point point : points) {
                    x += point.x - (board.length - 1);
                }
                return x % 2 == 1;
            }
        }
    }

    public static int reversePairs(int[] A) {
        int[] temp = new int[A.length];
        return reversePairs(0, A.length - 1, A, temp);
    }

    public static int reversePairs(int low, int high, int[] a, int[] temp) {
        if (low == high) {
            return 0;
        }
        int mid = (low + high) >> 1;
        int leftCount = reversePairs(low, mid, a, temp);
        int rightCount = reversePairs(mid + 1, high, a, temp);
        if (a[mid] <= a[mid + 1]) {
            return leftCount + rightCount;
        }
        int ct = 0, i = mid, j = high, idx = high;
        while (i >= low && j > mid) {
            if (a[i] > a[j]) {
                ct += j - mid;
                temp[idx--] = a[i--];
            } else {
                temp[idx--] = a[j--];
            }
        }
        while (i >= low) {
            temp[idx--] = a[i--];
        }
        while (j > mid) {
            temp[idx--] = a[j--];
        }
        for (i += 1; i <= high; i++) {
            a[i] = temp[i];
        }
        return leftCount + rightCount + ct;
    }

    public static void main(String[] args) {
        int[][] board = {{1, 0, 3, 4}, {6, 7, 8, 9}, {11, 12, 13, 14}};
        Run run = new Run();
        // run.BlockMove(board,11);


//        int[] A = new int[board.length * board[0].length - 1];
//        int high = 0, wide = 0;
//        boolean flag = false;
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board[0].length; j++) {
//                if (board[i][j] == 0) {
//                    high = i;
//                    wide = j;
//                    flag = true;
//                } else {
//                    int i1 = i * board[0].length + j;
//                    if (flag) {
//                        A[i1 - 1] = board[i][j];
//                    } else {
//                        A[i1] = board[i][j];
//                    }
//                }
//            }
//        }
//        for (int j : A) {
//            System.out.print(j + " ");
//        }
//        System.out.println(high);
//        System.out.println(wide);
    }

}
