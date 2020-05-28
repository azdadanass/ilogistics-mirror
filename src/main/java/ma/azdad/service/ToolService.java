package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Tool;
import ma.azdad.repos.ToolRepos;

@Component
@Transactional
public class ToolService {

	@Autowired
	ToolRepos toolRepos;

	public List<Tool> findLight(Integer toolTypeId, String status) {
		return toolRepos.findLight(toolTypeId, status);
	}

	public List<Tool> findLightCarList() {
		return findLight(1, "Active");
	}
	
	
	public Tool findOne(Integer id){
		return toolRepos.findOne(id);
	}
}