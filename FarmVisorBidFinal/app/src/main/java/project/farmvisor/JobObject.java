package project.farmvisor;

/**
 * Created by admin on 27/03/2017.
 */

public class JobObject {
    String id,tittle,farmer_id,contact,description,payment;

    public JobObject(String id,String tittle,String farmer_id,String contact,String description,String payment)
    {
        this.id=id;
        this.tittle=tittle;
        this.farmer_id=farmer_id;
        this.contact=contact;
        this.description=description;
        this.payment=payment;
    }

    public String getId() {
        return id;
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

    public String getTittle() {
        return tittle;
    }

    public String getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return "Title              : "+this.tittle+"\n"+"Payment      : "+this.payment+"\n"+"Contact       : "+this.contact+"\n"+"Description  "+this.description;
    }
}
