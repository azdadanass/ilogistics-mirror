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
	private TransportationJobStatus status = TransportationJobStatus.EDITED;
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
	
	// TIMELINE
		private Date date1;
		private Date date2; 
		private Date date3; 
		private Date date4; 
		private Date date5;
		private Date date6; 
		private Date date7; 
		private Date date8;
		private Date date9; 

		private User user1;
		private User user2;
		private User user3;
		private User user4;
		private User user5;
		private User user6;
		private User user7;
		private User user8;
		private User user9;

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


	public Date getDate1() {
		return date1;
	}


	public void setDate1(Date date1) {
		this.date1 = date1;
	}


	public Date getDate2() {
		return date2;
	}


	public void setDate2(Date date2) {
		this.date2 = date2;
	}


	public Date getDate3() {
		return date3;
	}


	public void setDate3(Date date3) {
		this.date3 = date3;
	}


	public Date getDate4() {
		return date4;
	}


	public void setDate4(Date date4) {
		this.date4 = date4;
	}


	public Date getDate5() {
		return date5;
	}


	public void setDate5(Date date5) {
		this.date5 = date5;
	}


	public Date getDate6() {
		return date6;
	}


	public void setDate6(Date date6) {
		this.date6 = date6;
	}


	public Date getDate7() {
		return date7;
	}


	public void setDate7(Date date7) {
		this.date7 = date7;
	}


	public Date getDate8() {
		return date8;
	}


	public void setDate8(Date date8) {
		this.date8 = date8;
	}


	public Date getDate9() {
		return date9;
	}


	public void setDate9(Date date9) {
		this.date9 = date9;
	}


	public User getUser1() {
		return user1;
	}


	public void setUser1(User user1) {
		this.user1 = user1;
	}


	public User getUser2() {
		return user2;
	}


	public void setUser2(User user2) {
		this.user2 = user2;
	}


	public User getUser3() {
		return user3;
	}


	public void setUser3(User user3) {
		this.user3 = user3;
	}


	public User getUser4() {
		return user4;
	}


	public void setUser4(User user4) {
		this.user4 = user4;
	}


	public User getUser5() {
		return user5;
	}


	public void setUser5(User user5) {
		this.user5 = user5;
	}


	public User getUser6() {
		return user6;
	}


	public void setUser6(User user6) {
		this.user6 = user6;
	}


	public User getUser7() {
		return user7;
	}


	public void setUser7(User user7) {
		this.user7 = user7;
	}


	public User getUser8() {
		return user8;
	}


	public void setUser8(User user8) {
		this.user8 = user8;
	}


	public User getUser9() {
		return user9;
	}


	public void setUser9(User user9) {
		this.user9 = user9;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
