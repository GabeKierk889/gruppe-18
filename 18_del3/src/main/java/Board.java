public class Board {
    private static Field[] fields;

    public Board() {
        fields = new Field[]{
                new Field("START"), new AmusementField("brown","The Burger Joint",1),
                new AmusementField("brown","The Pizza House",1), new ChanceField("Chance"),
                new AmusementField("light blue","The Candy Shop",1), new AmusementField("light blue","The Ice Cream Parlor",1),
                new Field("Visit to Jail"), new AmusementField("pink","The Museum",2),
                new AmusementField("pink","The Library",2), new ChanceField("Chance"),
                new AmusementField("orange","The Skate Park",2), new AmusementField("orange","Swimming Pool",2),
                new Field("Free Parking"), new AmusementField("red","The Video Game Arcade",3),
                new AmusementField("red","The Cinema",3), new ChanceField("Chance"),
                new AmusementField("yellow","The Toy Store",3), new AmusementField("yellow","The Pet Store",3),
                new Jail("Go to Jail"), new AmusementField("green","The Bowling Alley",4),
                new AmusementField("green","The Zoo",4), new ChanceField("Chance"),
                new AmusementField("dark blue","The Water Park",5), new AmusementField("dark blue","The Beach Walk",5)
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

    public static boolean onePlayerOwnsAllFieldsofSameColor(int fieldArrayNum) {
        boolean test = false;
        String color = ((AmusementField) fields[fieldArrayNum]).getFieldColor();
        int ownernum = ((AmusementField) fields[fieldArrayNum]).getOwnerNum();
        String fieldtype = fields[fieldArrayNum].getClassName();
        for (int i = 0; i< Field.getTotalnumberOfFields(); i++) {
            if (fields[i].getClassName().equalsIgnoreCase(fieldtype) && i != fieldArrayNum) {
                if (color.equalsIgnoreCase(((AmusementField) fields[i]).getFieldColor())) {
                    if (((AmusementField) fields[i]).getOwnerNum() == ownernum && ownernum != 0)
                        test = true;
                    else
                        return false; }
            }
        }
        return test;
    }

    public static void updateRentForAllFieldsOfSameColor(int fieldArrayNum) {
        if (onePlayerOwnsAllFieldsofSameColor(fieldArrayNum)) {
            String color = ((AmusementField) fields[fieldArrayNum]).getFieldColor();
            String fieldtype = fields[fieldArrayNum].getClassName();
            for (int i = 0; i< Field.getTotalnumberOfFields(); i++) {
                if (fields[i].getClassName().equalsIgnoreCase(fieldtype)) {
                    if (color.equalsIgnoreCase(((AmusementField) fields[i]).getFieldColor())) {
                        ((AmusementField) fields[i]).updateRent();
                    }
                }
            }
        }
        else ((AmusementField) fields[fieldArrayNum]).updateRent();
    }

    public static int getFieldArrayNumber(String fieldName) {
        int number = -1; // return -1 if the field cannot be found
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getFieldName().equalsIgnoreCase(fieldName))
                number = i;
        }
        return number;
    }
}
