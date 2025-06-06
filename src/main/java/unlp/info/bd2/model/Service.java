package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document
public class Service {

    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;

    @Field
    private String name;

    @Field
    private float price;

    @Field
    private String description;

    @Field
    private List<ItemService> itemServiceList;

    @DBRef(lazy = true)
    private Supplier supplier;

    public Service() {
    }

    public Service(String name, float price, String description, Supplier supplier) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.itemServiceList = new ArrayList<>();
        this.supplier = supplier;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void addItem(ItemService item) {
        this.itemServiceList.add(item);
    }
}
