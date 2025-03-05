package ma.azdad.mobile.model;

import java.util.List;

public class HardwareStatusList {
	
	private List<HardwareStatusData> hardwareStatusDataList;
	
	

    public HardwareStatusList() {
		super();
	}

	// Constructor
    public HardwareStatusList(List<HardwareStatusData> hardwareStatusDataList) {
        this.hardwareStatusDataList = hardwareStatusDataList;
    }

    // Getter and Setter
    public List<HardwareStatusData> getHardwareStatusDataList() {
        return hardwareStatusDataList;
    }

    public void setHardwareStatusDataList(List<HardwareStatusData> hardwareStatusDataList) {
        this.hardwareStatusDataList = hardwareStatusDataList;
    }

}
