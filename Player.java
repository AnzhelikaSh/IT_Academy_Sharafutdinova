public class Player {
    private static long playerId = 1;

    private String name;
    private int hp;
    private final long id;

    public Player(String name) {
        this.name = name;
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

    
    public void minusHp(int power) {
        this.hp -= power * 10;
    
    }
    
    public boolean isAlive(){
        return this.hp > 0;
    }

    
   @Override
    public String toString() {
        return "Id - " + this.id + ", Player - " + this.name +  ", hp is - " + this.hp;
    }

}
