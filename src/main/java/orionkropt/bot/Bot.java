package orionkropt.bot;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import orionkropt.Token;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import orionkropt.game.characters.CharacterSelection;
import orionkropt.game.Game;
import orionkropt.users.*;

public class Bot extends TelegramLongPollingBot {

    private final Auth auth = new Auth();
    private final Game game = new Game();
    private final CharacterSelection characterSelection = new CharacterSelection();

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
            id = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
            id = update.getCallbackQuery().getFrom().getId();
        }

        AppUser User = auth.getUser(id);
        System.out.println(User.getUsername() + " " + User.getCity() + " " + User.getId());
    }


    private void handleCallbackQuery(@NotNull CallbackQuery callbackQuery) {
        User user = callbackQuery.getFrom();
        Long id = user.getId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        SendMessage sm = new SendMessage();
        AppUser currentUser = auth.getUser(id);
        BotState botState;
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        SendPhoto sp = new SendPhoto();

        if (currentUser == null) {
            System.out.println("Пользавотель не найден");
            botState = BotState.DEFAULT;
        } else {
            botState = currentUser.getState();
        }

        switch (botState) {
            case CHARACTER_SELECTION:
                AnswerCallbackQuery answer;
                answer = characterSelection.handleCallback(callbackQuery, id, sm);
                sendAnswerCallBack(answer);
                sendMessage(sm);
                break;
            case GAME:
                DeleteMessage dm = new DeleteMessage(id.toString(), messageId);
                deleteMessage(dm);
                game.mainLoop(id, callbackQuery.getData(), sp, inlineKeyboard);
                sendPhoto(sp);
            case DEFAULT:
                break;
        }
        if (currentUser != null) {
            currentUser.setState(botState);
        }
    }

    private void handleMessage(@NotNull Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Long id = user.getId();
        AppUser currentUser = auth.getUser(id);
        SendMessage sm = new SendMessage();
        SendMediaGroup mediaGroup = new SendMediaGroup();
        SendPhoto photo = new SendPhoto();
        CommandsHandler.Command command;
        InlineKeyboard inlineKeyboard = new InlineKeyboard();
        BotState botState;
        if (currentUser == null) {
            System.out.println("Пользавотель не найден");
            botState = BotState.DEFAULT;
        } else {
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
                            Auth.StatusCode ret = auth.Registration(msg, sm);
                            if (ret == Auth.StatusCode.REGISTRATION_FINISHED || ret == Auth.StatusCode.ALREADY_REGISTERED) {
                                CommandsHandler.setCommand(CommandsHandler.Command.NOCOMMAND);
                                botState = (ret == Auth.StatusCode.REGISTRATION_FINISHED) ? BotState.CHARACTER_SELECTION : BotState.DEFAULT;
                            }
                            System.out.println(sm.getText());
                            sendMessage(sm);
                            if (ret == Auth.StatusCode.REGISTRATION_FINISHED) {
                                characterSelection.start(id, sm, mediaGroup, inlineKeyboard);
                                addInlineKeyboard(sm, inlineKeyboard);
                                sendMediaGroup(mediaGroup);
                                sendMessage(sm);
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
            case CHARACTER_SELECTION:
                botState = characterSelection.setNameOfUserCharacter(id, msg, sm);
                sendMessage(sm);
                if (botState == BotState.GAME) {
                    game.startGame(id, photo, inlineKeyboard);
                    addInlineKeyboard(photo, inlineKeyboard);
                    sendPhoto(photo);
                }
                break;
            case DEFAULT:
                StringBuilder sb = new StringBuilder(msg.getText());
                String reversed = sb.reverse().toString();
                sendText(id, reversed);
                break;
        }
        currentUser = auth.getUser(id);
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

    public void sendMediaGroup(SendMediaGroup mediaGroup) {
        try {
            execute(mediaGroup);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(SendPhoto sp) {
        try {
            execute(sp);
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

    public void editMessageMedia(EditMessageMedia editMedia) {
        try {
            execute(editMedia);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editReplyKeyboard(@NotNull EditMessageReplyMarkup editKeyboard, InlineKeyboard inlineKeyboard, Long id, Integer messageId) {
        editKeyboard.setReplyMarkup(inlineKeyboard.getInlineKeyboardMarkup());
        editKeyboard.setChatId(id.toString());
        editKeyboard.setMessageId(messageId);
        try {
            execute(editKeyboard);
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

    public void deleteMessage(DeleteMessage dm) {
        try {
            execute(dm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void addInlineKeyboard(@NotNull SendMessage sendMessage, InlineKeyboard inlineKeyboard) {
        sendMessage.setReplyMarkup(inlineKeyboard.getInlineKeyboardMarkup());
    }

    private void addInlineKeyboard(@NotNull SendPhoto sendPhoto, InlineKeyboard inlineKeyboard) {
        sendPhoto.setReplyMarkup(inlineKeyboard.getInlineKeyboardMarkup());
    }
}
