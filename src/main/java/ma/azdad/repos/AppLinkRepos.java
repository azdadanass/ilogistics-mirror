package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.AppLink;

@Repository
public interface AppLinkRepos extends JpaRepository<AppLink, Integer> {

	String madConversionRate = "(select b.oldInvoiceTerm.po.madConversionRate from Acceptance b where a.acceptance.id = b.id),(select b.budgetdetail.budget.madConversionRate from Expensepayment b where a.expensepayment.idexpensepayment = b.idexpensepayment )";
	String currency = "(select b.oldInvoiceTerm.po.currency.name from Acceptance b where a.acceptance.id = b.id),(select b.budgetdetail.budget.currency.name from Expensepayment b where a.expensepayment.idexpensepayment = b.idexpensepayment )";
	String acceptanceId = "(select b.id from Acceptance b where b.id = a.acceptance.id)";
	String expensepaymentId = "(select b.idexpensepayment from Expensepayment b where b.idexpensepayment = a.expensepayment.id)";
	String supplierName = " (select b.oldInvoiceTerm.po.supplier.name from Acceptance b where a.acceptance.id = b.id) ";
	String customerName = "(select b.oldInvoiceTerm.po.project.customer.name from Acceptance b where a.acceptance.id = b.id )";
	String idInvoice = "(select b.idInvoice from Acceptance b where b.id = a.acceptance.id)";
	String invoiceStatus = "(select b.invoiceStatus from Acceptance b where b.id = a.acceptance.id)";
	String invoiceDate = "(select b.dateInvoice from Acceptance b where b.id = a.acceptance.id)";
	String poNumeroIbuy = "(select b.oldInvoiceTerm.po.numeroIbuy from Acceptance b where b.id = a.acceptance.id)";
	String poNumeroInvoice = "(select b.oldInvoiceTerm.po.numeroInvoice from Acceptance b where b.id = a.acceptance.id)";
	String select = "select new AppLink(a.costType,a.revenueType,a.startDate,a.endDate,a.amount," + madConversionRate + "," + currency + "," + acceptanceId + " ," + expensepaymentId + ","
			+ supplierName + "," + customerName + "," + idInvoice + "," + invoiceStatus + "," + invoiceDate + "," + poNumeroIbuy + "," + poNumeroInvoice + ") ";

	@Query(select + "from AppLink a where a.deliveryRequest.id = ?1 and a.costType is not null")
	public List<AppLink> findCostsByDeliveryRequest(Integer deliveryRequestId);

//	@Query("select  from AppLink a where a.deliveryRequest.id = ?1 and a.costType is not null")
//	public List<AppLink> findIbuyCostsPaymentsByDeliveryRequest(Integer deliveryRequestId);

	@Query(select + "from AppLink a where a.deliveryRequest.id = ?1 and a.revenueType is not null")
	public List<AppLink> findRevenuesByDeliveryRequest(Integer deliveryRequestId);

	@Query("select a.acceptance.oldInvoiceTerm.po.project.id from AppLink a where a.deliveryRequest.id = ?1 and a.revenueType is not null group by a.acceptance.oldInvoiceTerm.po.project.id")
	public List<Integer> findRevenuesIdProjectByDeliveryRequest(Integer deliveryRequestId);

	@Query(select + "from AppLink a where a.transportationRequest.id = ?1")
	public List<AppLink> findByTransportationRequest(Integer transportationRequestId);

	@Query(select + "from AppLink a where a.warehouse.id = ?1")
	public List<AppLink> findByWarehouse(Integer warehouseId);

	@Modifying
	@Query("delete from AppLink a where a.deliveryRequest.id = ?1")
	public void deleteByDeliveryRequest(Integer deliveryRequestId);

//	@Query("select sum(a.amount * a.acceptance.oldInvoiceTerm.po.madConversionRate) from AppLink a where a.deliveryRequest.id = ?1 and a.acceptance.oldInvoiceTerm.po.id != ?2")
//	public Double findTotalAmountByDeliveryRequestAndNotPo(Integer deliveryRequestId, Integer poId);
	
	@Query("select coalesce(sum(a.amount * a.acceptance.oldInvoiceTerm.po.madConversionRate),0) from AppLink a where a.deliveryRequest.id = ?1")
	public Double findTotalAmountByDeliveryRequest(Integer deliveryRequestId);
	
	@Query("select coalesce(sum(a.amount),0) from AppLink a where a.transportationRequest.id = ?1")
	public Double findTotalAmountByTransportationRequest(Integer transportationRequestId);

}
