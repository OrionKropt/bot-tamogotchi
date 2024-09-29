package orionkropt.users;


import org.telegram.telegrambots.meta.api.objects.Message;


import java.util.HashMap;
import java.util.Map;

public class Auth {
    // Пока что харним пользователя в hashmap
    private static final Map<Long, AppUser> users = new HashMap<Long, AppUser>();

    public static enum statusCode {INCORRECT_NAME, INCORRECT_CITY, INCORRECT_INPUT, FAILED_REGISTRATION, SUCCESS, REGISTRATION_FINISHED}

    public static statusCode registerUser(AppUser user) {
        String username;
        String city;
        Long id;
        id = user.getId();

        if (users.containsKey(id)) {
            System.out.println("User is already registered!");
            return statusCode.FAILED_REGISTRATION;
        }
        users.put(id, user);
        return statusCode.SUCCESS;
    }

    public static AppUser getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        return null;
    }

    private static boolean isValidUsername(String username) {
        // Имя должно содержать только буквы и быть длиной от 2 до 32 символов
        return username.matches("^[a-zA-Zа-яА-ЯёЁ\\s']{2,32}$");
    }

    // TODO Позже можно бдует заменить на запрос API чтобы проверять со списком доступых городов
    private static boolean isValidCity(String city) {
        // Название города может содержать только буквы, пробелы и дефисы
        return city.matches("^[A-ZА-ЯЁ][a-zA-Zа-яА-ЯёЁ]*([\\s'-][a-zA-Zа-яА-ЯёЁ]+)*$") && city.length() > 2 && city.length() <= 64;
    }

    public static statusCode checkRegisterData(String username, String city) {
        if (username == null || city == null) {
            return statusCode.INCORRECT_INPUT;
        }
        if (!isValidUsername(username)) {
            return statusCode.INCORRECT_NAME;
        }
        if (!isValidCity(city)) {
            return statusCode.INCORRECT_CITY;
        }
        return statusCode.SUCCESS;
    }


    public static statusCode Registration(Message msg, StringBuffer request) {
        Long id = msg.getChat().getId();
        statusCode res = Auth.registerUser(new AppUser(id));
        if (res == statusCode.SUCCESS) {
            System.out.println("Start registration");
            request.append("""
                    Добро пожаловть!
                    Введите ваше имя и город
                    
                    Формат:
                    Имя
                    Город""");
            return statusCode.SUCCESS;
        } else {
            String str = msg.getText();
            String username;
            String city;
            AppUser CurrentUser = Auth.getUser(id);
            assert CurrentUser != null;
            username = msg.getText().split("\n")[0];
            city = msg.getText().split("\n")[1];

            res = Auth.checkRegisterData(username, city);
            switch (res) {
                case statusCode.SUCCESS:
                    CurrentUser.setCity(city);
                    CurrentUser.setUsername(username);
                    request.append("Регистрация прошла успешно!");
                    System.out.println("Successful registration " + id + " " + msg.getFrom().getFirstName());
                    return statusCode.REGISTRATION_FINISHED;
                case statusCode.INCORRECT_INPUT:
                    request.append("""
                            Не корректный формат имени или города
                            Формат:
                            Имя
                            Город""");
                    break;
                case statusCode.INCORRECT_NAME:
                    request.append("Имя должно содержать только латинские или русские буквы и быть длиной от 2 до 32 символов");
                    break;
                case statusCode.INCORRECT_CITY:
                    request.append("Название города может содержать только буквы, пробелы и дефисы и иметь длинну от 2 до 64 символов и начинаться с заглавной буквы");
                    break;
            }
            System.out.println("Incorrect registration data" + id + " " + msg.getFrom().getFirstName());
            return res;
        }
    }
}
