package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class TourGuideUser extends User {

    @Field
    private String education;

    @DBRef(lazy = false)
    private List<Route> routes;

    public TourGuideUser() {
    }

    public TourGuideUser(String username, String password, String name, String email, Date birthdate,
            String phoneNumber, String education) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.education = education;
        this.routes = new ArrayList<>();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route r) {
        this.routes.add(r);
    }

    @Override
    public boolean canBeDesactive() {
        return this.routes.isEmpty();
    }

}
