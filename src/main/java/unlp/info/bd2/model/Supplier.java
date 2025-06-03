package unlp.info.bd2.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Supplier {

    private ObjectId id;

    private String businessName;

    private String authorizationNumber;

    private List<Service> services;

    public Supplier() {
    }

    public Supplier(String businessName, String authorizationNumber) {
        this.businessName = businessName;
        this.authorizationNumber = authorizationNumber;
        this.services = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void addService(Service s) {
        this.services.add(s);
    }

}
