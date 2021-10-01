package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity

public class Stop extends GenericModel<Integer> implements Serializable, Comparable<Stop> {

	private Date date;
	private StopType type;
	private Boolean expected;
	private Site site;
	private Warehouse warehouse;
	private TransportationJob transportationJob;

	public Stop() {
		super();
	}

	public Stop(Date date, StopType type, Boolean expected, Site site, Warehouse warehouse, TransportationJob transportationJob) {
		super();
		this.date = date;
		this.type = type;
		this.expected = expected;
		this.site = site;
		this.warehouse = warehouse;
		this.transportationJob = transportationJob;
	}

	@Transient
	public String getAddress() {
		return site != null ? site.getGoogleAddress() : warehouse != null ? warehouse.getAddress() : null;
	}

	@Transient
	public GenericPlace getPlace() {
		return site != null ? site : warehouse != null ? warehouse : null;
	}

	@Transient
	public String getPlaceName() {
		return site != null ? site.getName() : warehouse != null ? warehouse.getName() : null;
	}

	@Transient
	public String getPlaceValue() {
		return site != null ? site.getValue() : warehouse != null ? warehouse.getValue() : null;
	}

	@Transient
	public Integer getSiteId() {
		if (site != null)
			return site.getId();
		return null;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Enumerated(EnumType.STRING)
	public StopType getType() {
		return type;
	}

	public void setType(StopType type) {
		this.type = type;
	}

	public Boolean getExpected() {
		return expected;
	}

	public void setExpected(Boolean expected) {
		this.expected = expected;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public TransportationJob getTransportationJob() {
		return transportationJob;
	}

	public void setTransportationJob(TransportationJob transportationJob) {
		this.transportationJob = transportationJob;
	}

	@Override
	public String toString() {
		return "Stop [date=" + UtilsFunctions.getFormattedDateTime(date) + ", date=" + date + ", type=" + type + ", expected=" + expected + ", site=" + (site != null ? site.getName() : null) + ", warehouse=" + (warehouse != null ? warehouse.getName() : null) + "]\n";
	}

	@Override
	public int compareTo(Stop o) {
		return date.compareTo(o.getDate());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
