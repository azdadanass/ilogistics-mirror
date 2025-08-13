package ma.azdad.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transportation_job_itinerary")
public class TransportationJobItinerary implements Localizable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "google_address")
    private String googleAddress;

    @Column(name = "google_city")
    private String googleCity;

    @Column(name = "google_region")
    private String googleRegion;
    
    private Double distanceFromPrevious;
    private Double cumulativeDistance;


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TransportationJob transportationJob;
    
    public TransportationJobItinerary() {}

    public TransportationJobItinerary(Date timestamp, Double latitude, Double longitude, TransportationJob transportationJob) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.transportationJob = transportationJob;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getGoogleAddress() { return googleAddress; }
    public void setGoogleAddress(String googleAddress) { this.googleAddress = googleAddress; }

    public String getGoogleCity() { return googleCity; }
    public void setGoogleCity(String googleCity) { this.googleCity = googleCity; }

    public String getGoogleRegion() { return googleRegion; }
    public void setGoogleRegion(String googleRegion) { this.googleRegion = googleRegion; }

   
    public TransportationJob getTransportationJob() {
		return transportationJob;
	}

	public void setTransportationJob(TransportationJob transportationJob) {
		this.transportationJob = transportationJob;
	}

	public Double getDistanceFromPrevious() {
		return distanceFromPrevious;
	}

	public void setDistanceFromPrevious(Double distanceFromPrevious) {
		this.distanceFromPrevious = distanceFromPrevious;
	}

	public Double getCumulativeDistance() {
		return cumulativeDistance;
	}

	public void setCumulativeDistance(Double cumulativeDistance) {
		this.cumulativeDistance = cumulativeDistance;
	}
	
	

}
