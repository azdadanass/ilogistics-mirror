package ma.azdad.model;

import java.io.Serializable;

public enum CustomerCategories implements Serializable {
    CATEGORY1("Enterprise"),
    CATEGORY2("Telco Operator"),
    CATEGORY3("Telco Regulator"),
    CATEGORY4("Telco Supplier"),
    CATEGORY5("Others"),
    CATEGORY6("Medical"),
    CATEGORY7("Channel Partner"),
    ALL("All"),
    ;

    private String value;

    private CustomerCategories(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
