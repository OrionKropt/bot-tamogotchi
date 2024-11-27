package orionkropt.game.rooms;

import orionkropt.game.Command;

public class Kitchen extends Room {
    public Kitchen() {
        super("kitchen", "src/main/resources/kitchen.png");
    }

    @Command(name = "feed")
    public void feed(Long id) {
        System.out.println("feed");
    }
    @Command(name = "cook")
    public void cook(Long id) {
        System.out.println("cook");
    }
}
