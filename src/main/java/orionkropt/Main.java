package orionkropt;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import orionkropt.bot.Bot;
import orionkropt.game.Game;
import orionkropt.game.characters.CharacterManager;
import orionkropt.image.ImageManager;


public class Main {
    public static void main(String[] args) throws TelegramApiException {
        ImageManager.INSTANCE.initialize();
        new CharacterManager().initialize();
        new Game().initialize();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        botsApi.registerBot(bot);
    }
}