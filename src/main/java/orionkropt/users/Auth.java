package orionkropt.users;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.Message;


import java.util.HashMap;
import java.util.Map;

public class Auth {
    // Пока что харним пользователя в hashmap
    private static final Map<Long, AppUser> users = new HashMap<>();

    public enum StatusCode {INCORRECT_NAME, INCORRECT_CITY, INCORRECT_INPUT, REGISTRATION_FAILED, SUCCESS, REGISTRATION_FINISHED}

    public static StatusCode registerUser(AppUser user) {
        String username;
        String city;
        Long id;
        id = user.getId();

        if (users.containsKey(id)) {
            System.out.println("User is already registered!");
            return StatusCode.REGISTRATION_FAILED;
        }
        users.put(id, user);
        return StatusCode.SUCCESS;
    }

    @Nullable
    public static AppUser getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        return null;
    }

    @Contract(pure = true)
    private static boolean isValidUsername(@NotNull String username) {
        // Имя должно содержать только буквы и быть длиной от 2 до 32 символов
        return username.matches("^[a-zA-Zа-яА-ЯёЁ\\s']{2,32}$");
    }

    // TODO Позже можно бдует заменить на запрос API чтобы проверять со списком доступых городов
    private static boolean isValidCity(@NotNull String city) {
        // Название города может содержать только буквы, пробелы и дефисы
        return city.matches("^[A-ZА-ЯЁ][a-zA-Zа-яА-ЯёЁ]*([\\s'-][a-zA-Zа-яА-ЯёЁ]+)*$") && city.length() > 2 && city.length() <= 64;
    }

    public static StatusCode checkRegisterData(String username, String city) {
        if (username == null || city == null) {
            return StatusCode.INCORRECT_INPUT;
        }
        if (!isValidUsername(username)) {
            return StatusCode.INCORRECT_NAME;
        }
        if (!isValidCity(city)) {
            return StatusCode.INCORRECT_CITY;
        }
        return StatusCode.SUCCESS;
    }

    private static StatusCode retrieveUserCityAndNameFromMessage(@NotNull Message msg, StringBuffer city, StringBuffer username) {
        String[] splitted = msg.getText().split("\n");
        StatusCode res;
        if (splitted.length != 2) {
            res = StatusCode.INCORRECT_INPUT;
        } else {
            username.append(splitted[0]);
            city.append(splitted[1]);
            res = StatusCode.SUCCESS;
        }
        return res;
    }

    public static StatusCode Registration(@NotNull Message msg, StringBuffer request) {
        Long id = msg.getChat().getId();
        StatusCode res = Auth.registerUser(new AppUser(id));
        if (res == StatusCode.SUCCESS) {
            System.out.println("Start registration");
            request.append("""
                    Добро пожаловть!
                    Введите ваше имя и город
                    
                    Формат:
                    Имя
                    Город""");
            return res;
        } else {
            StringBuffer bufUsername = new StringBuffer();
            StringBuffer bufCity = new StringBuffer();
            String username = null;
            String city = null;
            AppUser CurrentUser = Auth.getUser(id);
            if (CurrentUser == null) {
                throw new RuntimeException("CurrentU" +
                        "" +
                        "ser == null");
            }
            res = retrieveUserCityAndNameFromMessage(msg, bufUsername, bufCity);
            if (res == StatusCode.SUCCESS) {
                username = bufUsername.toString();
                city = bufCity.toString();
                res = checkRegisterData(username, city);
            }
            switch (res) {
                case StatusCode.SUCCESS:
                    CurrentUser.setCity(city);
                    CurrentUser.setUsername(username);
                    request.append("Регистрация прошла успешно!");
                    System.out.println("Successful registration " + id + " " + msg.getFrom().getFirstName());
                    return StatusCode.REGISTRATION_FINISHED;
                case StatusCode.INCORRECT_INPUT:
                    request.append("""
                            Некорректный формат имени или города
                            Формат:
                            Имя
                            Город""");
                    break;
                case StatusCode.INCORRECT_NAME:
                    request.append("Имя должно содержать только латинские или русские буквы и быть длиной от 2 до 32 символов");
                    break;
                case StatusCode.INCORRECT_CITY:
                    request.append("Название города может содержать только буквы, пробелы и дефисы и иметь длинну от 2 до 64 символов и начинаться с заглавной буквы");
                    break;
            }
            System.out.println("Incorrect registration data" + id + " " + msg.getFrom().getFirstName());
            return res;
        }
    }
}
