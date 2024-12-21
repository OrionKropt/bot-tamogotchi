package orionkropt.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import orionkropt.collections.Pair;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    static final Integer ROW_SIZE = 4;
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private ArrayList<ArrayList<Pair<String, String>>> keyboardData;

    public InlineKeyboard() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        keyboardData = new ArrayList<>();
    }

    public void createInlineKeyboardMarkup(ArrayList<Pair<String, String>> rawKeyboardData) {
        ArrayList<Pair<String, String>> row = new ArrayList<>();
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Pair<String, String> pair : rawKeyboardData) {
            row.add(new Pair<>(pair.getFirst(), pair.getSecond()));
            if (row.size() == ROW_SIZE) {
                keyboardData.add(row);
                keyboardData = new ArrayList<>();
            }
        }
        keyboardData.add(row);
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        ArrayList<InlineKeyboardButton> lineKeyboardButtonsRow = new ArrayList<>();
        ArrayList<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        for (ArrayList<Pair<String, String>> row : keyboardData) {
            for (Pair<String, String> pair : row) {
                System.out.println(pair.getFirst() + " " + pair.getSecond());
                lineKeyboardButtonsRow.add(InlineKeyboardButton.builder()
                        .text(pair.getFirst()).callbackData(pair.getSecond())
                        .build());
            }
            keyboardRowList.add(lineKeyboardButtonsRow);
            lineKeyboardButtonsRow = new ArrayList<>();
        }
        inlineKeyboardMarkup.setKeyboard(keyboardRowList);
        return inlineKeyboardMarkup;
    }

    public void appendButton(Pair<String, String> button) {
            ArrayList<Pair<String, String>> row = new ArrayList<>();
            row.add(button);
            keyboardData.add(row);
    }

    public void clear() {
        keyboardData.clear();
    }
}
