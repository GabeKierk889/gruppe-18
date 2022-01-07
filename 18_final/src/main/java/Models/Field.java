package Models;

public class Field {
    protected final String fieldName;

    public Field(String fieldName) {
        this.fieldName = fieldName;
    }

    public void landOnField(Player currentplayerobject) {
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isOwnableField() {
        return false;
    }
    public boolean isShippingField() {
        return false;
    }
    public boolean isStreetField() {
        return false;
    }
    public boolean isJailField() {
        return false;
    }

}
