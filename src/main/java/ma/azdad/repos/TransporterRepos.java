package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Transporter;

@Repository
public interface TransporterRepos extends JpaRepository<Transporter, Integer> {
	
	@Query("select new Transporter(a.id,a.type,a.user,a.firstName,a.lastName,a.email,a.phone,(select b.name from Supplier b where b.id = a.supplier.id), (select b.email from Supplier b where b.id = a.supplier.id), (select b.phone from Supplier b where b.id = a.supplier.id)) from Transporter a")
	public List<Transporter> findLight();
	
	
	
	
	
}
