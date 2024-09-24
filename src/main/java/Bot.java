import GithubComOrionKroptBotTamogotchUsers.AppUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import GithubComOrionKroptBotTamogotchUsers.Auth;

public class Bot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "YouTamogotchi_bot";
    }

    @Override
    public String getBotToken() {
        return Token.ReadToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        var msg = update.getMessage();
        var user = msg.getFrom();
        Long id = user.getId();
        Commands.Command command;
        Commands.SetCommand(msg.getText());
        command = Commands.GetCommand();

        if (command == Commands.Command.START) {
            StringBuffer request = new StringBuffer();
            Auth.statusCode ret = Auth.Registration(msg, request);
            if (ret == Auth.statusCode.FAILED_REGISTRATION) {
                command = Commands.Command.NOCOMMAND;
            }
            System.out.println(request);
            sendText(id, request.toString());
        } else {
            StringBuilder sb = new StringBuilder(msg.getText());
            String reversed = sb.reverse().toString();

            sendText(id, reversed);

            copyMessage(id, msg.getMessageId());
        }




        AppUser User = Auth.getUser(id);
        System.out.println(User.getUsername() + " " + User.getCity() + " " + User.getId());

        System.out.println(user.getFirstName() + " wrote " + msg.getText());


    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyMessage(Long who, Integer msgId) {
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())
                .chatId(who.toString())
                .messageId(msgId)
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        botsApi.registerBot(bot);
    }
}
