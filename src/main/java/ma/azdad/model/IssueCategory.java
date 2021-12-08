package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

import ma.azdad.utils.Color;

public enum IssueCategory {
	ACCESS_PROBLEM("Access problem", Color.PURPLE, Arrays.asList(IssueType.WRONG_KEYS, IssueType.NON_ROUTES, IssueType.BAD_WEATHER, IssueType.NEED_CRANE, IssueType.NEED_AUTHORIZATION, IssueType.SITE_PROPRIETARY_BLOCKAGE, IssueType.SITE_NEIGHBORS_BLOCKAGE, IssueType.OTHER)),
	SPACE_PROBLEM("Space problem", Color.RED, Arrays.asList(IssueType.LACK_OF_SPACE, IssueType.INADEQUATE_SPACE, IssueType.OTHER)),
	TECHNICAL("Technical", Color.BLUE, Arrays.asList(IssueType.MISSING_TECHNICAL_REQUIREMENT, IssueType.OTHER)),
	ENVIRONMENT("Environment", Color.GREEN, Arrays.asList(IssueType.CIVIL, IssueType.POWER, IssueType.AIR_CONDITIONING, IssueType.OTHER)),
	OTHER("Other", Color.L_BLUE, Arrays.asList(IssueType.OTHER)),;

	private final String value;
	private final Color color;
	private final List<IssueType> typeList;

	private IssueCategory(String value, Color color, List<IssueType> typeList) {
		this.value = value;
		this.color = color;
		this.typeList = typeList;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	public List<IssueType> getTypeList() {
		return typeList;
	}

	@Override
	public String toString() {
		return this.value;
	}
}