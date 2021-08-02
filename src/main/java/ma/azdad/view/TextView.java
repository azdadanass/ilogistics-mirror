package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Text;
import ma.azdad.repos.TextRepos;
import ma.azdad.service.TextService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class TextView extends GenericView<Integer, Text, TextRepos, TextService> {

	@Autowired
	private TextService textService;

	@Autowired
	private SessionView sessionView;

	private Text text;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
	}

	@Override
	public void refreshList() {
		if ("/partNumberConfiguration.xhtml".equals(currentPath))
			list2 = list1 = textService.findByBeanNameAndType("partNumber", "unitType");
	}

	public List<String> findValueByBeanName(String beanName) {
		return textService.findValueByBeanName(beanName);
	}

	public List<String> findValueByBeanNameAndType(String beanName, String type) {
		return textService.findValueByBeanNameAndType(beanName, type);
	}

	public void initText(String beanName, String type) {
		text = new Text(beanName, type);
	}

	public Boolean canAddText() {
		return sessionView.getIsAdmin();
	}

	public void saveText() {
		if (!canAddText())
			return;
		textService.save(text);
		refreshList();
	}

	public Boolean canDeleteText() {
		return sessionView.getIsAdmin();
	}

	public void deleteText() {
		try {
			textService.delete(text);
			refreshList();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// GETTERS & SETTERS
	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

}
