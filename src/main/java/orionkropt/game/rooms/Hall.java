package orionkropt.game.rooms;

import orionkropt.game.Command;

public class Hall extends Room {
    public Hall() {
        super("hall", "src/main/resources/hall.png");
    }

    @Command(name = "playGames")
    public void playGames(Long id) {
        System.out.println("Playing games");
    }

}
