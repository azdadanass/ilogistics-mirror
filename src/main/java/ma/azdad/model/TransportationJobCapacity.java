package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "transportation_job_capacity")
public class TransportationJobCapacity extends GenericModel<Integer> implements Serializable {

    

   
    private TransportationJob transportationJob;

    private String type;

    private Date date;

    private Double weight;

    private Double volume;

    private Double cumulativeWeight;

    private Double cumulativeVolume;

    public TransportationJobCapacity() {}

    public TransportationJobCapacity(TransportationJob transportationJob, String type,
                                     Date date, Double weight, Double volume,
                                     Double cumulativeWeight, Double cumulativeVolume) {
        this.transportationJob = transportationJob;
        this.type = type;
        this.date = date;
        this.weight = weight;
        this.volume = volume;
        this.cumulativeWeight = cumulativeWeight;
        this.cumulativeVolume = cumulativeVolume;
    }

    // Getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id", nullable = false)
    public TransportationJob getTransportationJob() {
        return transportationJob;
    }

    public void setTransportationJob(TransportationJob transportationJob) {
        this.transportationJob = transportationJob;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getCumulativeWeight() {
        return cumulativeWeight;
    }

    public void setCumulativeWeight(Double cumulativeWeight) {
        this.cumulativeWeight = cumulativeWeight;
    }

    public Double getCumulativeVolume() {
        return cumulativeVolume;
    }

    public void setCumulativeVolume(Double cumulativeVolume) {
        this.cumulativeVolume = cumulativeVolume;
    }

}
