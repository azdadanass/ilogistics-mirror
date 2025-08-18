package ma.azdad.model;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Entity
@Table(name = "driver_location")
public class DriverLocation  implements Localizable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User driver; 
    
    public DriverLocation() {
    }

    public DriverLocation(Date date, Double latitude, Double longitude, User driver) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.driver = driver;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    public String getGoogleCity() {
        return googleCity;
    }

    public void setGoogleCity(String googleCity) {
        this.googleCity = googleCity;
    }

    public String getGoogleRegion() {
        return googleRegion;
    }

    public void setGoogleRegion(String googleRegion) {
        this.googleRegion = googleRegion;
    }

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}
    
    

   
}