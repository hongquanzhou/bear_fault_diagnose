package bean;

public class ModelName {
    public String name;
    public String path;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ModelName{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
