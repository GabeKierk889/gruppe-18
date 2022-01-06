package Models;

public class Field {
    protected final String fieldName;

    public Field(String fieldName) {
        this.fieldName = fieldName;
    }

    public void landOnField(Player currentplayerobject) {
    }

    //no setters as all attributes are final variables
    public String getFieldName() {
        return fieldName;
    }

    public boolean isOwnableField() {
        return false;
    }

}
