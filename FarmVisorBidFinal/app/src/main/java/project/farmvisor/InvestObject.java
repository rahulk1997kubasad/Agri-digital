package project.farmvisor;

/**
 * Created by admin on 27/03/2017.
 */

public class InvestObject {

    String id,farmer_id,area_size,soil_type,landmark,investment,years,contact,description;


    public InvestObject(String id, String farmer_id, String area_size, String soil_type, String landmark, String investment, String years, String contact, String description)
    {
        this.id=id;
        this.farmer_id=farmer_id;
        this.area_size=area_size;
        this.soil_type=soil_type;
        this.landmark=landmark;
        this.investment=investment;
        this.years=years;
        this.contact=contact;
        this.description=description;
    }

    public String getId() {
        return id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public String getDescription() {
        return description;

    }

    public String getContact() {
        return contact;
    }

    public String getArea_size() {
        return area_size;
    }

    public String getinvestment() {
        return investment;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getSoil_type() {
        return soil_type;
    }

    public String getYears() {
        return years;
    }

    @Override
    public String toString() {
        return "Area Size      :  "+this.area_size+"\n"+"Soil Type       :  "+this.soil_type+"\n"+"Investment  :  "+this.investment+"\n"+"Investing Years : "+this.years+"\n"+"Contact       :  "+this.contact+"\n"+"Description : "+this.description;
    }
}
