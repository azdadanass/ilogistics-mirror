package ma.azdad.repos;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ma.azdad.GenericTest;
import ma.azdad.model.ZoneHeight;
import ma.azdad.service.LocationService;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	LocationService locationService;
	
	@Autowired
	LocationRepos locationRepos;

	@Test
	@Transactional
	public void test() throws Exception {
//		InputStream is = new FileInputStream(new File("/home/azdad/Bureau/test1.xls"));
//		try {
//			Location location= locationService.findOne(2);
//			List<ZoneLine> lines =  locationService.generateZoning(is);
//			lines.forEach(l->location.addLine(l));
//			locationService.save(location);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		List<ZoneHeight> heightList = locationRepos.findHeightListByLocation(2);
		
		heightList.forEach(i->{
			System.out.println(i.getReference());
		});
		
		
		Gson gson = new GsonBuilder()
			    .excludeFieldsWithoutExposeAnnotation()
			    .create();
		String json = gson.toJson(heightList);
		
		
		
		System.out.println(json);
		

//		Location location = locationService.findOne(2);
//		
//		
//		location.getLineList().forEach(l->l.getColumnList().forEach(c->c.getHeightList().forEach(h->System.out.println(h))));
//		
//		
//		Gson gson = new GsonBuilder()
//			    .excludeFieldsWithoutExposeAnnotation()
//			    .setPrettyPrinting()
//			    .create();
//		String json = gson.toJson(location.getLineList());
//		
//		
//		
//		System.out.println(json);

	}

}
