package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

public enum CostType {
	CAR_RENTAL("Car Rental"),
	PETROL_HIGHWAY("Petrol"),
	CAR_OPEX("Car OPEX"),
	TELECOM("Telecom"),
	TOOLS_PURCHASE("Tools Purchase"),

	HOUSING_ALLOWANCE("Housing allowance & Accomodations"),
	PROJECT_GOODS_PURCHASE("Project Goods Purchase"),
	PROJECT_TRANSPORTATION("Project Transportation"),
	SUBCONTRACTING("Subcontracting"),
	WAREHOUSING("Warehousing"),

	FACILITY_MANAGEMENT("Facility Management"),
	ADMIN_PURCHASES("Admin / Facility Purchases"),
	ADMIN_TRANSPORTATION("Admin Transportation"),

	SALARY("Salary"),
	BONUS_AND_AWARDS("Bonus & Awards"),

	LEGAL_FEE("Legal Fee"),
	FINANCIAL_SERVICES("Financial Services"),
	INSURANCE_SERVICES("Insurance Services"),
	LOGISTICS("Logistics"),
	PROJECT_PURCHASE("Project Purchase"),
	TRANSPORTATOIN_ALLOWANCE("Transportation Allowance"),
	OTHER("Other"),

	FREIGHT_AND_TRANSIT("Customs, Freight & Transit"),

	TOOLS_OPEX("Tools OPEX"),
	TRAVEL("Travel & Travel Accommodations"),
	HIGHWAY("Highway"),
	SOFTWARE_SUPPORT("Software / Support");

	public static List<CostType> MAPPABLE_LIST;
	static {
		MAPPABLE_LIST = Arrays.asList(CostType.CAR_RENTAL, CostType.PETROL_HIGHWAY, CostType.CAR_OPEX, CostType.TELECOM, CostType.TOOLS_PURCHASE, CostType.TOOLS_OPEX, CostType.PROJECT_GOODS_PURCHASE,
				CostType.FREIGHT_AND_TRANSIT, CostType.PROJECT_PURCHASE, CostType.PROJECT_TRANSPORTATION, CostType.WAREHOUSING, CostType.FACILITY_MANAGEMENT, CostType.ADMIN_PURCHASES,
				CostType.HIGHWAY);
	}

	private final String value;

	private CostType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Boolean showDates() {
		return Arrays.asList(CAR_RENTAL, PETROL_HIGHWAY, TELECOM, HOUSING_ALLOWANCE, WAREHOUSING, FACILITY_MANAGEMENT, SALARY, BONUS_AND_AWARDS, CostType.HIGHWAY).contains(this);
	}

	public Boolean requireDates() {
		return Arrays.asList(CAR_RENTAL, PETROL_HIGHWAY, TELECOM, HIGHWAY).contains(this);
	}

	@Override
	public String toString() {
		return this.value;
	}

}