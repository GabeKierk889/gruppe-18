package Services;

import Models.*;
import Models.FieldSubType.*;

public class FieldsCreator {
    // sets up the fields in an array using data pulled from external file
    public Field[] createFields() {
        FieldsDataReader read = new FieldsDataReader("Fields.csv");
        String[] fieldName = read.getFieldNamesArray();
        String[] streetColor = read.getStreetColorsArray();
        int[] housePrice = read.getHousePriceArray();
        int[] fieldPrice = read.getFieldPriceArray();
        int[][] rentArray = read.getRentArrayArray();

        return new Field[]{
                new Field(fieldName[0]),
                new StreetField(fieldName[1],fieldPrice[1],housePrice[1],rentArray[1],streetColor[1]),
                new ChanceField(fieldName[2]),
                new StreetField(fieldName[3],fieldPrice[3],housePrice[3],rentArray[3],streetColor[3]),
                new IncomeTaxField(fieldName[4], GameSettings.INCOME_TAX_AMOUNT, GameSettings.INCOME_TAX_RATE),
                new ShippingField(fieldName[5],fieldPrice[5],rentArray[5]),
                new StreetField(fieldName[6],fieldPrice[6],housePrice[6],rentArray[6],streetColor[6]),
                new ChanceField(fieldName[7]),
                new StreetField(fieldName[8],fieldPrice[8],housePrice[8],rentArray[8],streetColor[8]),
                new StreetField(fieldName[9],fieldPrice[9],housePrice[9],rentArray[9],streetColor[9]),
                new Field(fieldName[10]),
                new StreetField(fieldName[11],fieldPrice[11],housePrice[11],rentArray[11],streetColor[11]),
                new BreweryField(fieldName[12],fieldPrice[12],rentArray[12]),
                new StreetField(fieldName[13],fieldPrice[13],housePrice[13],rentArray[13],streetColor[13]),
                new StreetField(fieldName[14],fieldPrice[14],housePrice[14],rentArray[14],streetColor[14]),
                new ShippingField(fieldName[15],fieldPrice[15],rentArray[15]),
                new StreetField(fieldName[16],fieldPrice[16],housePrice[16],rentArray[16],streetColor[16]),
                new ChanceField(fieldName[17]),
                new StreetField(fieldName[18],fieldPrice[18],housePrice[18],rentArray[18],streetColor[18]),
                new StreetField(fieldName[19],fieldPrice[19],housePrice[19],rentArray[19],streetColor[19]),
                new Field(fieldName[20]),
                new StreetField(fieldName[21],fieldPrice[21],housePrice[21],rentArray[21],streetColor[21]),
                new ChanceField(fieldName[22]),
                new StreetField(fieldName[23],fieldPrice[23],housePrice[23],rentArray[23],streetColor[23]),
                new StreetField(fieldName[24],fieldPrice[24],housePrice[24],rentArray[24],streetColor[24]),
                new ShippingField(fieldName[25],fieldPrice[25],rentArray[25]),
                new StreetField(fieldName[26],fieldPrice[26],housePrice[26],rentArray[26],streetColor[26]),
                new StreetField(fieldName[27],fieldPrice[27],housePrice[27],rentArray[27],streetColor[27]),
                new BreweryField(fieldName[28],fieldPrice[28],rentArray[28]),
                new StreetField(fieldName[29],fieldPrice[29],housePrice[29],rentArray[29],streetColor[29]),
                new JailField(fieldName[30]),
                new StreetField(fieldName[31],fieldPrice[31],housePrice[31],rentArray[31],streetColor[31]),
                new StreetField(fieldName[32],fieldPrice[32],housePrice[32],rentArray[32],streetColor[32]),
                new ChanceField(fieldName[33]),
                new StreetField(fieldName[34],fieldPrice[34],housePrice[34],rentArray[34],streetColor[34]),
                new ShippingField(fieldName[35],fieldPrice[35],rentArray[35]),
                new ChanceField(fieldName[36]),
                new StreetField(fieldName[37],fieldPrice[37],housePrice[37],rentArray[37],streetColor[37]),
                new StateTaxField(fieldName[38],GameSettings.STATE_TAX_AMOUNT),
                new StreetField(fieldName[39],fieldPrice[39],housePrice[39],rentArray[39],streetColor[39])
        };
    }
}
