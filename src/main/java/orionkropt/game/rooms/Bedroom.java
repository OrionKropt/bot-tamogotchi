package orionkropt.game.rooms;

import orionkropt.game.Command;

public class Bedroom extends Room {
    public Bedroom() {
        super("bedroom", "Спальня", "src/main/resources/bedroom.png");
    }

    @Command(name = "sleep", sendName = "Уложить спать")
    public void sleep(Long id) {
        System.out.println("Sleeping");
    }

}

