package project.farmvisor;

/**
 * Created by admin on 27/03/2017.
 */

public class LandObject {

    String id,farmer_id,area_size,soil_type,landmark,cost_year,years,contact,description;
    String coords;

    public LandObject(String id,String farmer_id,String area_size,String soil_type,String landmark,String cost_year,String years,String contact,String description)
    {
        this.id=id;
        this.farmer_id=farmer_id;
        this.area_size=area_size;
        this.soil_type=soil_type;
        this.landmark=landmark;
        this.cost_year=cost_year;
        this.years=years;
        this.contact=contact;
        this.description=description;
    }

    public LandObject(String id,String farmer_id,String area_size,String soil_type,String landmark,String cost_year,String years,String contact,String description,String coords)
    {
        this.id=id;
        this.farmer_id=farmer_id;
        this.area_size=area_size;
        this.soil_type=soil_type;
        this.landmark=landmark;
        this.cost_year=cost_year;
        this.years=years;
        this.contact=contact;
        this.description=description;
        this.coords = coords;
    }

    public String getCoords(){
        return coords;
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

    public String getCost_year() {
        return cost_year;
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
        return "Area Size  :  "+this.area_size+"\n"+"Soil Type   :  "+this.soil_type+"\n"+"Cost/Year :  "+this.cost_year+"\n"+"Investing Years : "+this.years+"\n"+"Contact    :  "+this.contact+"\n"+"Description : "+this.description;
    }
}
