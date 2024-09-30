package orionkropt;

import orionkropt.users.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private BotState botState = BotState.DEFAULT;

    @Override
    public String getBotUsername() {
        return "YouTamogotchi_bot";
    }

    @Override
    public String getBotToken() {
        return Token.readToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        var msg = update.getMessage();
        var user = msg.getFrom();
        Long id = user.getId();
        Commands.Command command;
        Commands.parseCommand(msg.getText());
        command = Commands.getCommand();
        if (command != Commands.Command.NOCOMMAND) {
            botState = BotState.COMMAND_EXECUTION;
        }
        switch (botState) {
            case COMMAND_EXECUTION:
                if (command != Commands.Command.NOCOMMAND) {
                    switch (command) {
                        case START:
                            StringBuffer request = new StringBuffer();
                            Auth.StatusCode ret = Auth.Registration(msg, request);
                            if (ret == Auth.StatusCode.REGISTRATION_FINISHED) {
                                Commands.setCommand(Commands.Command.NOCOMMAND);
                                botState = BotState.DEFAULT;
                            }
                            System.out.println(request);
                            sendText(id, request.toString());
                            break;

                        default:

                            break;

                    }
                }
                break;
            case DEFAULT:
                StringBuilder sb = new StringBuilder(msg.getText());
                String reversed = sb.reverse().toString();
                sendText(id, reversed);
                copyMessage(id, msg.getMessageId());
                break;
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
}
