package orionkropt.game.rooms;

import orionkropt.game.Command;

public class Kitchen extends Room {
    public Kitchen() {
        super("kitchen", "Кухня", "src/main/resources/kitchen.png");
    }

    @Command(name = "feed", sendName = "Кормить")
    public void feed(Long id) {
        System.out.println("feed");
    }
    @Command(name = "cook", sendName = "Готовить")
    public void cook(Long id) {
        System.out.println("cook");
    }
}
