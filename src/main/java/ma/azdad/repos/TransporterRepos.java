package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Transporter;

@Repository
public interface TransporterRepos extends JpaRepository<Transporter, Integer> {

	String c1 = "select new Transporter(a.id,a.type,a.user,a.privateFirstName,a.privateLastName,a.privateEmail,a.privatePhone,a.privateCin,(select b.name from Supplier b where b.id = a.supplier.id), (select b.email from Supplier b where b.id = a.supplier.id), (select b.phone from Supplier b where b.id = a.supplier.id)) ";
	
	@Query(c1 + "from Transporter a")
	public List<Transporter> findLight();
	
	
	
	
	
}
