package project.farmvisor;

/**
 * Created by guruvyasa on 5/3/18.
 */

class BidObject {
    private String id;
    private String bidDate;
    private String bidPrice;
    private String crop;
    private String cropName;
    private String cropMinPrice;
    private String status;
    private String farmerPhone,farmerName;

    public BidObject(String id, String cropName, String bidDate, String bidPrice, String crop) {
        this.id = id;
        this.bidDate = bidDate;
        this.bidPrice = bidPrice;
        this.crop = crop;
        this.cropName = cropName;
    }
    public BidObject(String id, String cropName, String bidDate, String bidPrice, String crop,String status) {
        this(id,cropName,bidDate,bidPrice,crop);
        this.status = status;
    }

    public BidObject(String id, String bidDate, String bidPrice, String cropMinPrice,String farmerName, String farmerPhone,String status){
        this.id = id;
        this.cropMinPrice = cropMinPrice;
        this.bidDate = bidDate;
        this.bidPrice = bidPrice;
        this.farmerName = farmerName;
        this.farmerPhone = farmerPhone;
        this.status = status;
    }


    public String getCropName() {
        return cropName;
    }

    public String getCropMinPrice() {
        return cropMinPrice;
    }

    public void setCropMinPrice(String cropMinPrice) {
        this.cropMinPrice = cropMinPrice;
    }

    public String getFarmerPhone() {
        return farmerPhone;
    }

    public void setFarmerPhone(String farmerPhone) {
        this.farmerPhone = farmerPhone;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String newStatus){
        status = newStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return cropName;
    }

    public void setCropName(String name) {
        this.cropName = name;
    }


    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }
    @Override
    public String toString() {
        return "Trader Name :  "+this.farmerName+"\n"+"Phone   :  "+this.farmerPhone+"\n"+"Bid Price :  "+this.bidPrice+"\n"+"Bid Date : "+this.bidDate+"\n";//+"Contact    :  "+this.contact+"\n"+"Description : "+this.description;
    }
}
