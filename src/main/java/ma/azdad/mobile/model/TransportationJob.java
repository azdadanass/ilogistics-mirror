package ma.azdad.mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ma.azdad.model.TransportationJobFile;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;

public class TransportationJob {

	private Integer id;
	private String comment;
	private Date startDate;
	private Date endDate;
	private TransportationJobStatus status = TransportationJobStatus.NOT_STARTED;
	private Integer totalTr;

	// Costs
	private Double realCost = 0.0;
	private Double estimatedCost = 0.0;

	private Double vehiclePrice = 0.0;

	//weights
	private Integer numberOfItems;
	private Double grossWeight;
	private Double volume;
	//distance 
	private String estimatedDistanceText;
	private String realDistanceText;

	// TM
	private String vehiculeMatricule;
	private ma.azdad.mobile.model.Vehicule vehicule;
	private ma.azdad.mobile.model.User driver;
	private User transporter;
	
	private List<ma.azdad.mobile.model.TransportationJobFile> fileList = new ArrayList<>();
	private List<ma.azdad.mobile.model.TransportationJobHistory> historyList = new ArrayList<>();
	private List<ma.azdad.mobile.model.TransportationRequest> transportationRequestList = new ArrayList<>();
	
	
	public TransportationJob() {
		super();
	}
	

	public TransportationJob(Integer id, Date startDate, Date endDate, TransportationJobStatus status, Double realCost,
			Double estimatedCost, User driver,Integer totalTr,String vehiculeMatricule) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.realCost = realCost;
		this.estimatedCost = estimatedCost;
		this.driver = driver;
		this.totalTr = totalTr;
		this.vehiculeMatricule = vehiculeMatricule;
	}
	
	



	public TransportationJob(Integer id, Date startDate, Date endDate, TransportationJobStatus status, Double realCost,
			Double estimatedCost, Double vehiclePrice, String vehiculeMatricule, User driver) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.realCost = realCost;
		this.estimatedCost = estimatedCost;
		this.vehiclePrice = vehiclePrice;
		this.vehiculeMatricule = vehiculeMatricule;
		this.driver = driver;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public TransportationJobStatus getStatus() {
		return status;
	}
	public void setStatus(TransportationJobStatus status) {
		this.status = status;
	}
	public Double getRealCost() {
		return realCost;
	}
	public void setRealCost(Double realCost) {
		this.realCost = realCost;
	}
	public Double getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(Double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	public Double getVehiclePrice() {
		return vehiclePrice;
	}
	public void setVehiclePrice(Double vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}


	public User getTransporter() {
		return transporter;
	}


	public void setTransporter(User transporter) {
		this.transporter = transporter;
	}


	public Vehicule getVehicule() {
		return vehicule;
	}


	public void setVehicule(Vehicule vehicule) {
		this.vehicule = vehicule;
	}

	public User getDriver() {
		return driver;
	}



	public void setDriver(User driver) {
		this.driver = driver;
	}



	public List<ma.azdad.mobile.model.TransportationJobFile> getFileList() {
		return fileList;
	}



	public void setFileList(List<ma.azdad.mobile.model.TransportationJobFile> fileList) {
		this.fileList = fileList;
	}



	public List<ma.azdad.mobile.model.TransportationJobHistory> getHistoryList() {
		return historyList;
	}



	public void setHistoryList(List<ma.azdad.mobile.model.TransportationJobHistory> historyList) {
		this.historyList = historyList;
	}



	public List<ma.azdad.mobile.model.TransportationRequest> getTransportationRequestList() {
		return transportationRequestList;
	}



	public void setTransportationRequestList(List<ma.azdad.mobile.model.TransportationRequest> transportationRequestList) {
		this.transportationRequestList = transportationRequestList;
	}


	public Integer getTotalTr() {
		return totalTr;
	}


	public void setTotalTr(Integer totalTr) {
		this.totalTr = totalTr;
	}


	public String getVehiculeMatricule() {
		return vehiculeMatricule;
	}


	public void setVehiculeMatricule(String vehiculeMatricule) {
		this.vehiculeMatricule = vehiculeMatricule;
	}


	public String getEstimatedDistanceText() {
		return estimatedDistanceText;
	}


	public void setEstimatedDistanceText(String estimatedDistanceText) {
		this.estimatedDistanceText = estimatedDistanceText;
	}


	public String getRealDistanceText() {
		return realDistanceText;
	}


	public void setRealDistanceText(String realDistanceText) {
		this.realDistanceText = realDistanceText;
	}


	public Integer getNumberOfItems() {
		return numberOfItems;
	}


	public void setNumberOfItems(Integer numberOfItems) {
		this.numberOfItems = numberOfItems;
	}


	public Double getGrossWeight() {
		return grossWeight;
	}


	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}


	public Double getVolume() {
		return volume;
	}


	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
