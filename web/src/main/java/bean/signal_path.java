package bean;

public class signal_path {
    String string = null;
    String path = null;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "{" +
                "\"string\":\"" + string + "\"," +
                "\"path\":\"" + path + "\"" +
                '}';
    }
}
