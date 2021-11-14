public class Board {
    private static Field[] fields;

    public Board() {
        fields = new Field[]{
                new Field("Start"), new AmusementField("Brown","The Burger Joint",1),
                new AmusementField("Brown","The Pizza House",1), new ChanceField("Chance"),
                new AmusementField("Light blue","The Candy Shop",1), new AmusementField("Light blue","The Ice Cream Parlor",1),
                new Field("Visit to Jail"), new AmusementField("Pink","The Museum",2),
                new AmusementField("Pink","The Library",2), new ChanceField("Chance"),
                new AmusementField("Orange","The Skate Park",2), new AmusementField("Orange","Swimming Pool",2),
                new Field("Free Parking"), new AmusementField("Red","The Video Game Arcade",3),
                new AmusementField("Red","The Cinema",3), new ChanceField("Chance"),
                new AmusementField("Yellow","The Toy Store",3), new AmusementField("Yellow","The Pet Store",3),
                new Jail("Go to Jail"), new AmusementField("Green","The Bowling Alley",4),
                new AmusementField("Green","The Zoo",4), new ChanceField("Chance"),
                new AmusementField("Blue","The Water Park",5), new AmusementField("Blue","The Beach Walk",5)
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

    public static boolean onePlayerOwnsAllFieldsofSameColor(int fieldnumber) {
        boolean test = false;
        String color = ((AmusementField) fields[fieldnumber-1]).getFieldColor();
        int ownernum = ((AmusementField) fields[fieldnumber-1]).getOwnerNum();
        String fieldtype = fields[fieldnumber-1].getClassName();
        for (int i = 0; i< Field.getTotalnumberOfFields(); i++) {
            if (fields[i].getClassName().equalsIgnoreCase(fieldtype) && i != fieldnumber-1) {
                if (color.equalsIgnoreCase(((AmusementField) fields[i]).getFieldColor())) {
                    if (((AmusementField) fields[i]).getOwnerNum() == ownernum)
                        test = true;
                    else
                        return false; }
            }
        }
        return test;
    }
}
