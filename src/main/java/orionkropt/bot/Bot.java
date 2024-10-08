package orionkropt.bot;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import orionkropt.Token;
import orionkropt.characters.CharacterSelection;
import orionkropt.characters.StatusCode;
import orionkropt.users.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {

    private final Auth auth = new Auth();

    @Override
    public String getBotUsername() {
        return "YouTamogotchi_bot";
    }

    @Override
    public String getBotToken() {
        return Token.readToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        Long id = null;
        BotState botState = BotState.DEFAULT;
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(botState, update);
            id = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(botState, update.getCallbackQuery());
            id = update.getCallbackQuery().getFrom().getId();
        }

        AppUser User = auth.getUser(id);
        System.out.println(User.getUsername() + " " + User.getCity() + " " + User.getId());
    }


    private void handleCallbackQuery(BotState botState, @NotNull CallbackQuery callbackQuery) {
        User user = callbackQuery.getFrom();
        Long id = user.getId();
        SendMessage sm = new SendMessage();
        AppUser currentUser = auth.getUser(id);
        CharacterSelection characterSelection = new CharacterSelection();
        if (currentUser != null) {
            botState = currentUser.getState();
        }
        switch (botState) {
            case CHARACTER_SELECTION:
                AnswerCallbackQuery answer;
                answer = characterSelection.handleCallback(callbackQuery, id, sm);
                sendAnswerCallBack(answer);
                sendMessage(sm);
                break;
            case DEFAULT:
                break;
        }
        if (currentUser != null) {
            currentUser.setState(botState);
        }
    }

    private void handleMessage(BotState botState, @NotNull Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Long id = user.getId();
        AppUser currentUser = auth.getUser(id);
        CharacterSelection characterSelection = new CharacterSelection();
        SendMessage sm;
        CommandsHandler.Command command;

        if (currentUser != null) {
            botState = currentUser.getState();
        }

        CommandsHandler.parseCommand(msg.getText());
        command = CommandsHandler.getCommand();

        if (command != CommandsHandler.Command.NOCOMMAND) {
            botState = BotState.COMMAND_EXECUTION;
        }
        switch (botState) {
            case COMMAND_EXECUTION:
                if (command != CommandsHandler.Command.NOCOMMAND) {
                    switch (command) {
                        case START:
                            StringBuffer request = new StringBuffer();
                            Auth.StatusCode ret = auth.Registration(msg, request);
                            if (ret == Auth.StatusCode.REGISTRATION_FINISHED || ret == Auth.StatusCode.ALREADY_REGISTERED) {
                                CommandsHandler.setCommand(CommandsHandler.Command.NOCOMMAND);
                                botState = (ret == Auth.StatusCode.REGISTRATION_FINISHED) ? BotState.CHARACTER_SELECTION : BotState.DEFAULT;
                            }
                            System.out.println(request);
                            sendText(id, request.toString());
                            if (ret == Auth.StatusCode.REGISTRATION_FINISHED) {
                                sm = characterSelection.start(id);
                                sendMessage(sm);
                            }
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
                break;
        }
        if (currentUser != null) {
            currentUser.setState(botState);
        }
        System.out.println(user.getFirstName() + " wrote " + msg.getText());
    }


    public void sendMessage(SendMessage sm) {
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendText(@NotNull Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAnswerCallBack(AnswerCallbackQuery answer) {
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
