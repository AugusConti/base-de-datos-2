package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DriverUser extends User {

    private String expedient;

    private List<Route> routes;

    public DriverUser() {
    }

    public DriverUser(String username, String password, String name, String email, Date birthdate, String phoneNumber,
            String expedient) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.expedient = expedient;
        this.routes = new ArrayList<>();
    }

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) {
        this.routes = routs;
    }

    public void addRoute(Route r) {
        this.routes.add(r);
    }

    @Override
    public boolean canBeDesactive() {
        return this.routes.isEmpty();
    }
}
