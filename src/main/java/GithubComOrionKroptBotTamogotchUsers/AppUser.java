package GithubComOrionKroptBotTamogotchUsers;


public class AppUser {
    private String username;
    private String city;
    private final Long id;

    public AppUser(Long id) {
        this.username = null;
        this.city = null;
        this.id = id;
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
}
