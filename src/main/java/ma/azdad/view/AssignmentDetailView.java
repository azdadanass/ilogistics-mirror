package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.AssignmentDetail;
import ma.azdad.service.AssignmentDetailService;

@ManagedBean
@Component
@Scope("view")
public class AssignmentDetailView extends GenericView<AssignmentDetail> {

	@Autowired
	AssignmentView assignmentView;

	@Autowired
	AssignmentDetailService assignmentDetailService;

	@Override
	@PostConstruct
	public void init() {
		super.init();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		if ("/assignmentList.xhtml".equals(currentPath))
			list2 = list1 = assignmentDetailService.find(sessionView.getUsername(), assignmentView.getAssignator(), assignmentView.getActive());
	}

}
