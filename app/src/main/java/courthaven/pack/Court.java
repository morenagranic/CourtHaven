package courthaven.pack;

public class Court {
    int id;
    int distance;

    String name;
    String address;
    String sport;
    int id_admin;
    int imageDrawable;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Court{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", sport='" + sport + '\'' +
                ", id_admin=" + id_admin +
                '}';
    }

    public Court(int id, String name, String address, String sport, int id_admin, int distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.address = address;
        this.sport = sport;
        this.id_admin = id_admin;
    }

    public Court() {
    }

    public Court(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }
}
