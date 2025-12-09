import java.util.Scanner;
import java.util.Random;

public class ClassWork0312 {
     public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);

         System.out.println("Введите число для расчета факториала");
         int number = scanner.nextInt();
         System.out.println(factorial(number));

         System.out.println("Введите размер массива");
         int size = scanner.nextInt();
         massiv(size);
         
     }
         public static long factorial (int number){
            long result = 1;
            for (int i = 1; i <= number; i++){             
            result *= i;
            }
            return result;
         }
         
         public static void massiv (int size){
            for (int i = 0; i < size; i++){           
            Random random = new Random();
            System.out.print(random.nextInt(100) + " ");
            }
         }
     }
    
