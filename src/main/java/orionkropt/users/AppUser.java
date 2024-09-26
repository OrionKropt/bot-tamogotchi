package orionkropt.users;


import orionkropt.bot.BotState;

public class AppUser {
    private String username;
    private String city;
    private final Long id;
    private BotState state;

    public AppUser(Long id) {
        this.username = null;
        this.city = null;
        this.id = id;
        this.state = BotState.DEFAULT;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BotState getState() {
        return state;
    }

    public void setState(BotState state) {
        this.state = state;
    }
}
