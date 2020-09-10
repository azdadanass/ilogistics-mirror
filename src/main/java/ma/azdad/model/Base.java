package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity

public class Base extends GenericBeanOld implements Serializable {

	private String field1;
	private String field2;
	private String field3;

	private List<BaseFile> fileList = new ArrayList<>();
	private List<BaseHistory> historyList = new ArrayList<>();

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && field1 != null)
			result = field1.toLowerCase().contains(query);
		if (!result && field2 != null)
			result = field2.toLowerCase().contains(query);
		return result;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<BaseFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<BaseFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<BaseHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<BaseHistory> historyList) {
		this.historyList = historyList;
	}

}
