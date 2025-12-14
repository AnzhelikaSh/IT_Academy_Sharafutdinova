import java.util.Objects;

public class Player {
    private static long playerId = 1;

    private String name;
    private int hp;
    private String team;
    private long id;

    public Player(String name, String team) {
        this.name = name;
        this.team = team;
        this.hp = 100;
        this.id = playerId;
        setIdPlayer(playerId + 1);
    }

    private static void setIdPlayer(long idPlayer) {
        playerId = idPlayer;
    }
    public long getId() {
        return id;
    }

     public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp(){
        return this.hp;
    }

    public String getTeam(){
        return this.team;
    }

    public void minusHp(int hpForMinus) {
        this.hp -= hpForMinus;
    }

    private void attack (Player player) {
        player.minusHp(10);
    }

   @Override
    public String toString() {
        return "Id - " + this.id + ", Player - " + this.name + ", Team - " + this.team + ", hp is - " + this.hp;
    }

}
