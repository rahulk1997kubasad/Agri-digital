package project.farmvisor;

/**
 * Created by admin on 27/03/2017.
 */

public class EquipmentObject {

    String id, name, farmer_id, model, rent, contact, description;

    public EquipmentObject(String id, String name, String farmer_id, String model, String rent, String contact, String description)
    {
        this.id=id;
        this.name=name;
        this.farmer_id=farmer_id;
        this.model=model;
        this.rent=rent;
        this.contact=contact;
        this.description=description;

    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public String getModel() {
        return model;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRent() {
        return rent;
    }

    @Override
    public String toString() {
        return "Name    :  "+this.name+"\n"+"Model    :  "+this.model+"\n"+"Rent    :  "+this.rent+"\n"+"Contact    :  "+this.contact+"\n"+"Description  :  "+this.description;
    }
}
