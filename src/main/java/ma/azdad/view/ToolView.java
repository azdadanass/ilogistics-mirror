package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Tool;
import ma.azdad.service.ToolService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class ToolView {

	@Autowired
	protected ToolService toolService;

	@PostConstruct
	public void init() {

	}

	// GENERIC
	public List<Tool> findLightCarList() {
		return toolService.findLightCarList();
	}

}