import java.util.Random;
import java.util.Scanner;
import java.util.Objects;

public class Game {
    private static int getPowerOfKick(String name){
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int power = 0;
        while (power <= 0 || power > 9) {            
            System.out.print("%s, введите силу удара - ".formatted(name));
            power = scanner.nextInt();
            }
            int chance = random.nextInt(1, 10);
            if (chance >= 1 && chance <= (10 - power)){
                return power;
            }
            return 0;
        }
    

    private static String getName(int index){
        Scanner scanner = new Scanner(System.in);
        String name = null;
        while (Objects.isNull(name)) {
            System.out.print("Введите имя игрока номер %s - ".formatted(index + 1));
            name = scanner.nextLine();
            if (name.isEmpty() || name.length() > 15) {
                name = null;
            }
        }
        return name;
    }

    private static Player[] getPlayers(){
        Player[] players = new Player[2];
        for (int i = 0; i < players.length; i ++){
            players[i] = new Player(getName(i));
        }
        return players;
    }

    private static void moveOfPlayer(Player playerWithMove, Player player){
        int power = getPowerOfKick(playerWithMove.getName());
        player.minusHp(power);
        player.isAlive();
    }

    public static void main(String[] args) {
        Player[] players = getPlayers();
        while (true){
             moveOfPlayer(players[0], players[1]);
            if (!players[1].isAlive()){
                System.out.println("Победил игрок - %s".formatted(players[0].getName()));
                break;
            }
            System.out.println("%s, осталось здоровья - %s".formatted(players[1].getName(), players[1].getHp()));
            moveOfPlayer(players[1], players[0]);
            if (!players[0].isAlive()){
                System.out.println("Победил игрок - %s".formatted(players[1].getName()));
                break;    
        }
        System.out.println("%s, осталось здоровья - %s".formatted(players[0].getName(), players[0].getHp()));
    }
    }
}

