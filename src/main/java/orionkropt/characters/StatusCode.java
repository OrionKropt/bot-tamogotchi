package orionkropt.characters;

public enum StatusCode {
    SUCCESS(0),
    ERROR(1);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
