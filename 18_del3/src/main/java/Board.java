public class Board {
    private Field[] fields;

    public Board() {
        fields = new Field[]{
                new Field("Start"), new Field("The Burger Bar (M$1)")
        };
    }

    public Field getFieldObject(int arrayindex) {
        return fields[arrayindex];
    }

    public String toString() {
        String str = "";
        for (int t=0; t< Field.getTotalnumberOfFields();t++) {
            str += fields[t].toString()+"\n";
        }
        return str;
    }
}
