package courthaven.pack;

public class Booking {

    int id_booking;
    int code;
    double cost;
    String timePeriod;
    Court court;

    public Booking(int id_booking, int code, double cost, String timePeriod, Court court) {
        this.id_booking = id_booking;
        this.code = code;
        this.cost = cost;
        this.timePeriod = timePeriod;
        this.court = court;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id_booking=" + id_booking +
                ", code=" + code +
                ", cost=" + cost +
                ", timePeriod='" + timePeriod + '\'' +
                ", court=" + court +
                '}';
    }

    public int getId_booking() {
        return id_booking;
    }
    public void setId_booking(int id_booking) {
        this.id_booking = id_booking;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public String getTimePeriod() {
        return timePeriod;
    }
    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
    public Court getCourt() {
        return court;
    }
    public void setCourt(Court court) {
        this.court = court;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

}
