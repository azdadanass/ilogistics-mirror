package ma.azdad.view;

import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Customer;
import ma.azdad.model.CustomerCategory;
import ma.azdad.service.CustomerService;
import ma.azdad.utils.LabelValue;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class CustomerView {

	@Autowired
	protected CustomerService customerService;

	private Customer customer;

	private List<CustomerCategory> categoryList = CustomerService.CATEGORY_LIST;
	

	public void setCustomer(Integer customerId) {
		customer = customerService.findOne(customerId);
	}

	// GENERIC
	public List<LabelValue> findLabelValueList() {
		return customerService.findLabelValueList();
	}

	public List<Customer> findLight() {
		return customerService.findLight();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<CustomerCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CustomerCategory> categoryList) {
		this.categoryList = categoryList;
	}

}
