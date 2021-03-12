package project.farmvisor;

/**
 * Created by admin on 25/05/2017.
 */

public class CropObject {

    String id,name,price,description,quantity,image,farmer_id,bid_date;

    public CropObject(String id,String farmer_id,String name,String price,String description,String quantity,String image, String bid_date)
    {
        this.id=id;
        this.farmer_id=farmer_id;
        this.name=name;
        this.price=price;
        this.description=description;
        this.quantity=quantity;
        this.image=image;
        this.bid_date = bid_date;
    }

    public CropObject(String id,String farmer_id,String name,String price, String description,String quantity,String bid_date)
    {
        this.id=id;
        this.farmer_id=farmer_id;
        this.name=name;
        this.price = price;
        this.description=description;
        this.quantity=quantity;
        this.bid_date = bid_date;
    }

    public CropObject(String id,String farmer_id,String name,String description,String quantity)
    {
        this.id=id;
        this.farmer_id=farmer_id;
        this.name=name;
        this.price = "0";
        this.description=description;
        this.quantity=quantity;
        this.bid_date = "";
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Name    "+this.name+"\n"+"Quantity  "+this.quantity+"\n"+"Bid date: "+this.bid_date;
    }
}
