public class Board {
    private Field[] fields;

    public Board() {
        fields = new Field[]{
                new Field("Start"), new AmusementField("The Burger Joint",1),
                new AmusementField("The Pizza House",1), new ChanceField("Chance"),
                new AmusementField("The Candy Shop",1), new AmusementField("The Ice Cream Parlor",1),
                new Field("Visit to Jail"), new AmusementField("The Museum",2),
                new AmusementField("The Library",2), new ChanceField("Chance"),
                new AmusementField("The Skate Park",2), new AmusementField("Swimming Pool",2),
                new Field("Free Parking"), new AmusementField("The Video Game Arcade",3),
                new AmusementField("The Cinema",3), new ChanceField("Chance"),
                new AmusementField("The Toy Store",3), new AmusementField("The Pet Store",3),
                new Jail("Go to Jail"), new AmusementField("The Bowling Alley",4),
                new AmusementField("The Zoo",4), new ChanceField("Chance"),
                new AmusementField("The Water Park",5), new AmusementField("The Beach Walk",5)
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
