package orionkropt.characters;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.List;

public class CharacterSelection {
    private void addKeyBoard(@NotNull SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> lineKeyboardButtonsRow = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        lineKeyboardButtonsRow.add(InlineKeyboardButton.builder()
                .text("Свинка").callbackData("pig_selected")
                .build());
        lineKeyboardButtonsRow.add(InlineKeyboardButton.builder()
                .text("Другие").callbackData("other_selected")
                .build());

        keyboardRowList.add(lineKeyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    public SendMessage start(@NotNull Long id) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(id.toString())
                .text("Выберите персонажа").build();
        addKeyBoard(sendMessage);
        return sendMessage;
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
}
