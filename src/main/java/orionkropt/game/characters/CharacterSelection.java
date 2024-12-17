package orionkropt.game.characters;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import orionkropt.bot.BotState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelection {
    private void addKeyBoard(@NotNull SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<InlineKeyboardButton> lineKeyboardButtonsRow = new ArrayList<>();
        ArrayList<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        lineKeyboardButtonsRow.add(InlineKeyboardButton.builder()
                .text("Свинка").callbackData("pig")
                .build());
        lineKeyboardButtonsRow.add(InlineKeyboardButton.builder()
                .text("Другие").callbackData("other")
                .build());

        keyboardRowList.add(lineKeyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    public void start(@NotNull Long id, SendMessage sm, SendMediaGroup mediaGroup) {
        CharacterManager characterManager = new CharacterManager();
        ArrayList<Character> listCharacters = characterManager.getAllCharacters();
        ArrayList<InputMedia> inputMedia = new ArrayList<>();

        for (Character character : listCharacters) {
            File file = character.getImage().getFile();
            InputMediaPhoto mediaPhoto = new InputMediaPhoto();
            mediaPhoto.setMedia(file, file.getName());
            inputMedia.add(mediaPhoto);
            System.out.println(file.getAbsolutePath());
        }
        System.out.println(inputMedia.size());

        inputMedia.getFirst().setCaption("Персонажи");
        mediaGroup.setChatId(id.toString());
        mediaGroup.setMedias(inputMedia);

        sm.setChatId(id.toString());
        sm.setText("Выберите персонажа");
        addKeyBoard(sm);
    }

    public AnswerCallbackQuery handleCallback(@NotNull CallbackQuery callbackQuery, Long chatId, SendMessage sm) {
        CharacterManager characterManager = new CharacterManager();
        Character character = characterManager.createCharacter(callbackQuery.getData());
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackQuery.getId());
        if (characterManager.addCharacter(chatId, character) == StatusCode.SUCCESS) {
            answer.setText("Вы выбрали тамогочи " + characterManager.getSendingType(character) + '.');
            sm.setChatId(chatId.toString());
            sm.setText("Введите имя вашего тамагочи:");
            System.out.println("Selected character: " + callbackQuery.getData() + " for user: " + callbackQuery.getFrom().getFirstName());
        } else {
            System.out.println("character is null. Can't create new Character");
        }
        return answer;
    }

    public BotState setNameOfUserCharacter(@NotNull Long chatId, Message message, SendMessage sendMessage) {
        CharacterManager characterManager = new CharacterManager();
        Character userCharacter = characterManager.getCharacter(chatId);
        sendMessage.setChatId(chatId.toString());
        if (checkCorrectName(message.getText())){
            userCharacter.setName(message.getText());
            sendMessage.setText("Регистрация прошла успешно!");
            return BotState.GAME;
        } else {
            sendMessage.setText("Имя персонажа некорректно, попробуйте еще раз");
            return BotState.CHARACTER_SELECTION;
        }
    }

    private boolean checkCorrectName(String name){
        return name.matches("^[A-ZА-ЯЁ][a-zA-Zа-яА-ЯёЁ]+$") && name.length() > 2 && name.length() <= 64;
    }
}
