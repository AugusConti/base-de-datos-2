package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class User {

    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    @Field
    private String password;

    @Field
    private String name;

    @Field
    private String email;

    @Field
    private Date birthdate;

    @Field
    private String phoneNumber;

    @Field
    private boolean active;

    @DBRef(lazy = true)
    private List<Purchase> purchaseList;

    public User() {
    }

    public User(String username, String password, String name, String email, Date birthdate, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.active = true;
        this.purchaseList = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addPurchase(Purchase p) {
        this.purchaseList.add(p);
    }

    public boolean canBeDesactive() {
        return true;
    }

    public boolean canBeDeleted() {
        return this.purchaseList.isEmpty();
    }

    public void removePurchase (Purchase p){
        this.purchaseList.remove(p);
    }
}
