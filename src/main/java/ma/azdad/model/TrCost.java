package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrCost   {


    private String transportationRequestReference;
    private Date startDate;
    private Date endDate;

    private Double startCost = 0.0;
    private Double itineraryCost = 0.0;
    private Double handlingCost = 0.0;

    // --- Constructor ---
    public TrCost(String transportationRequestReference, Date startDate, Date endDate,
                  Double startCost, Double itineraryCost, Double handlingCost) {
        this.transportationRequestReference = transportationRequestReference;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startCost = startCost;
        this.itineraryCost = itineraryCost;
        this.handlingCost = handlingCost;
    }

    public TrCost() {}

    public Double getCost() {
        return (startCost != null ? startCost : 0.0)
             + (itineraryCost != null ? itineraryCost : 0.0)
             + (handlingCost != null ? handlingCost : 0.0);
    }

  

    // --- Getters and Setters ---
    public String getTransportationRequestReference() {
        return transportationRequestReference;
    }

    public void setTransportationRequestReference(String transportationRequestReference) {
        this.transportationRequestReference = transportationRequestReference;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getStartCost() {
        return startCost;
    }

    public void setStartCost(Double startCost) {
        this.startCost = startCost;
    }

    public Double getItineraryCost() {
        return itineraryCost;
    }

    public void setItineraryCost(Double itineraryCost) {
        this.itineraryCost = itineraryCost;
    }

    public Double getHandlingCost() {
        return handlingCost;
    }

    public void setHandlingCost(Double handlingCost) {
        this.handlingCost = handlingCost;
    }
}
