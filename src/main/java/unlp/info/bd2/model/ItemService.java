package unlp.info.bd2.model;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class ItemService {

    @MongoId(FieldType.OBJECT_ID)
    ObjectId id;

    @Field
    private int quantity;

    @DBRef(lazy = false)
    private Purchase purchase;

    @DBRef(lazy = false)
    private Service service;

    public ItemService() {
    }

    public ItemService(int quantity, Purchase purchase, Service service) {
        this.quantity = quantity;
        this.purchase = purchase;
        this.service = service;
        purchase.addItem(this, quantity * service.getPrice());
        service.addItem(this);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
