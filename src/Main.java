public class Main {
    private static GameGUI Game;
    private static World world;
    public static void main(String args[]){
        world = new World(60,100);
        Game = new GameGUI("Game of Life",world);
    }
}
