package courthaven.pack;

public class User {
    int id;
    String name;
    String description;
    String email;
    String password;

    String courts;
    boolean isAdmin = false;

    public User(int id, String name, String description, String email, String password, String courts,boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.description=description;
        this.email = email;
        this.password = password;
        this.courts= courts;
        this.isAdmin = isAdmin;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", courts='" + name + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourts() {
        return courts;
    }

    public void setCourts(String courts) {
        this.courts = courts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
