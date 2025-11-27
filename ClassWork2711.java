import java.util.Scanner;

public class ClassWork2711 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число от 1 до 50");
        int m = scanner.nextInt();
        int zn = 0;

        int[][] arr = new int[m][m];

        for(int k = 0; k < m; k++) {
            int t1 = k + 1;
            int t2 = k + 1;
            for(int v = 0; v < t2; v++) {
                arr[v][t1 - 1] = zn;
                t1--;
                zn++;
            }
        }
        int l = 0;
        for(int k2 = m - 1; k2 > 0; k2--) {
            int r1 = m - 1;
            int r2 = m - 1;
            for(int v = l; v < r2; v++) {
                arr[v + 1][r1] = zn;
                r1--;
                zn++;
            }
            l++;

        }

        for(int[] a : arr) {
            for(int x : a) {
                if (x < 10) {
                    System.out.print(" ");
                }
                System.out.print(x + " ");
            }
            System.out.println("");
        }

    }
}