package project.farmvisor;

/**
 * Created by admin on 19/04/2017.
 */

public class RequestObject {

    private String id,request_for,farmer_contact,object_id,rfarmer_id,farmer_name;

    public RequestObject(String id,String request_for,String farmer_contact,String object_id,String rfarmer_id,String farmer_name)
    {
        this.id=id;
        this.request_for=request_for;
        this.farmer_contact=farmer_contact;
        this.object_id=object_id;
        this.rfarmer_id=rfarmer_id;
        this.farmer_name=farmer_name;
    }

    public String getId() {
        return id;
    }

    public String getFarmer_contact() {
        return farmer_contact;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public String getObject_id() {
        return object_id;
    }

    public String getRequest_for() {
        return request_for;
    }

    public String getRfarmer_id() {
        return rfarmer_id;
    }

    @Override
    public String toString() {
        return this.farmer_name;
    }
}