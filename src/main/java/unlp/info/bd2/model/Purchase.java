package unlp.info.bd2.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase {

    ObjectId id;

    private String code;

    private float totalPrice;

    private Date date;

    private User user;

    private Route route;

    private Review review;

    private List<ItemService> itemServiceList;

    public Purchase() {
    }

    public Purchase(String code, Date date, User user, Route route) {
        this.code = code;
        this.totalPrice = route.getPrice();
        this.date = date;
        this.user = user;
        this.route = route;
        this.review = null;
        this.itemServiceList = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }

    public void addItem(ItemService item, float price) {
        this.itemServiceList.add(item);
        this.totalPrice += price;
    }

    public Review addReview(int rating, String comment) {
        Review r = new Review(rating, comment, this);
        this.review = r;
        return r;
    }
}
