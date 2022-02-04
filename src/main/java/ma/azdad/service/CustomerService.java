package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Customer;
import ma.azdad.model.CustomerCategories;
import ma.azdad.model.CustomerCategory;
import ma.azdad.repos.CustomerRepos;
import ma.azdad.utils.LabelValue;

@Component
@Transactional
public class CustomerService {

	public static List<CustomerCategory> CATEGORY_LIST = new ArrayList<CustomerCategory>();
	static {
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY1.getValue(), "/resources/img/customer_category/1.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY2.getValue(), "/resources/img/customer_category/2.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY3.getValue(), "/resources/img/customer_category/3.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY4.getValue(), "/resources/img/customer_category/4.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY6.getValue(), "/resources/img/customer_category/6.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY7.getValue(), "/resources/img/customer_category/7.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.CATEGORY5.getValue(), "/resources/img/customer_category/5.png"));
		CATEGORY_LIST.add(new CustomerCategory(CustomerCategories.ALL.getValue(), "/resources/img/customer_category/all.png"));
	}

	@Autowired
	private CustomerRepos repos;

	@Autowired
	private StockRowService stockRowService;

	public List<LabelValue> findLabelValueList() {
		return repos.findLabelValueList();
	}

	public Customer findOne(Integer id) {
		return repos.findById(id).get();
	}

	public Customer findOneNullable(Integer id) {
		if (id == null)
			return null;
		return repos.findById(id).get();
	}

	public List<Customer> findLight() {
		return repos.findLight();
	}

	public List<Customer> findLight(List<Integer> idList) {
		if (idList == null || idList.isEmpty())
			return null;
		return repos.findLight(idList);
	}

	public List<Customer> findLight(List<Integer> idList, String category, Boolean isStockEmpty) {
		if (idList == null || idList.isEmpty())
			return null;

		if (CustomerCategories.ALL.getValue().equals(category))
			return repos.findLight(idList, isStockEmpty);
		else
			return repos.findLight(idList, isStockEmpty, category);
	}

	public void updateIsStockEmpty(Integer customerId) {
		repos.updateIsStockEmpty(customerId, stockRowService.isSotckEmpty(customerId));
	}

	public void updateIsStockEmptyScript() {
		findLight().forEach(i -> updateIsStockEmpty(i.getId()));
	}

	public String findNameByPo(Integer poId) {
		return repos.findNameByPo(poId);
	}
}
