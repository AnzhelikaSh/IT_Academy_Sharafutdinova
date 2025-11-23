import java.util.Scanner;

public class ClassWork2111 {
    public static void main (String[] args)
    {
        Scanner scanner = new Scanner (System.in);
        System.out.println ("Введите число от 1 до 14");
        int N = scanner.nextInt();
                long Factorial = 1;
        for (int i=1; i<=N; i++)
            Factorial=Factorial*i;
        System.out.println ("Факториал числа " + N + " равен " + Factorial);

    }
}