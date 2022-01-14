package Services;

import Controllers.GameController;
import Models.*;
import gui_fields.*;

import java.awt.*;

public class GUIBoardCreator {
    private GUI_Field[] guiFields;
    private GUI_Street[] guiStreets;
    private Board board = GameController.getInstance().getBoard();
    private FieldsDataReader fieldsDataReader = new FieldsDataReader("Fields.csv");
    private int[] fieldPrice = fieldsDataReader.getFieldPriceArray();
    private int[][] rentAmount = fieldsDataReader.getRentArrayArray();
    private int[] housePrice = fieldsDataReader.getHousePriceArray();
    private FileImporter fileImporter = new FileImporter();
    private String[] takeTurnMessages = fileImporter.readAllLinesInFile("GameMessages_takeTurn.txt");
    private String[] fieldDescriptions = fileImporter.readAllLinesInFile("FieldDescriptions.txt");

    public GUIBoardCreator(GUI_Field[] guiFields, GUI_Street[] guiStreets) {
        this.guiFields = guiFields;
        this.guiStreets = guiStreets;
    }

    public void setUpAndFormatGUIBoard() {
        for (int i = 0; i < board.getTotalNumOfFields(); i++) { // sets up all GUI fields/streets
            guiStreets[i] = new GUI_Street();
            guiStreets[i].setTitle(board.getFieldObject(i).getFieldName());
            guiStreets[i].setBackGroundColor(Color.lightGray);
            guiFields[i] = guiStreets[i];
            // if a field is ownable, set sub text to price
            if (board.getFieldObject(i) instanceof OwnableField) {
                guiFields[i].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[i]));
                guiFields[i].setDescription(
                        String.format(fieldDescriptions[7],rentAmount[i][0])+fieldDescriptions[6]+
                                fieldDescriptions[6]+
                        String.format(fieldDescriptions[8],rentAmount[i][1])+fieldDescriptions[6]+
                        String.format(fieldDescriptions[9],rentAmount[i][2])+fieldDescriptions[6]+
                        String.format(fieldDescriptions[10],rentAmount[i][3])+fieldDescriptions[6]+
                        String.format(fieldDescriptions[11],rentAmount[i][4])+fieldDescriptions[6]+
                        String.format(fieldDescriptions[12],rentAmount[i][5])+fieldDescriptions[6]+
                                fieldDescriptions[6]+
                        String.format(fieldDescriptions[13],housePrice[i])+fieldDescriptions[6]+
                        String.format(fieldDescriptions[14],housePrice[i])+fieldDescriptions[6]
                );
            }
            // setup chance fields
            if (board.getFieldObject(i) instanceof ChanceField) {
                guiFields[i] = new GUI_Chance();
                guiFields[i].setDescription("");

            }
            // setup shipping fields
            if (board.getFieldObject(i) instanceof ShippingField) {
                guiFields[i] = new GUI_Shipping();
                guiFields[i].setDescription(
                        String.format(fieldDescriptions[7],rentAmount[i][0])+fieldDescriptions[6]+
                                fieldDescriptions[6]+
                        String.format(fieldDescriptions[15],rentAmount[i][1])+fieldDescriptions[6]+
                                fieldDescriptions[6]+
                        String.format(fieldDescriptions[16],rentAmount[i][1])+fieldDescriptions[6]+
                                fieldDescriptions[6]+
                        String.format(fieldDescriptions[17],rentAmount[i][1])+fieldDescriptions[6]+
                                fieldDescriptions[6]
                );
            }
        }

        formatStreets();
        formatShippingFields();
        setupBreweryFields();
        setupSTART();
        setupRefuge();
        setupJailFields();
        setupTaxFields();
    }

    private void formatStreets() {
        Color orange = new Color(255, 165, 0);
        Color lilla = new Color(128, 0, 128);

        guiStreets[1].setBackGroundColor(Color.blue);

        guiStreets[3].setBackGroundColor(Color.blue);

        guiStreets[6].setBackGroundColor(orange);
        guiStreets[8].setBackGroundColor(orange);
        guiStreets[9].setBackGroundColor(orange);

        guiStreets[11].setBackGroundColor(Color.green);
        guiStreets[13].setBackGroundColor(Color.green);
        guiStreets[14].setBackGroundColor(Color.green);

        guiStreets[16].setBackGroundColor(Color.gray);
        guiStreets[18].setBackGroundColor(Color.gray);
        guiStreets[19].setBackGroundColor(Color.gray);

        guiStreets[21].setBackGroundColor(Color.red);
        guiStreets[23].setBackGroundColor(Color.red);
        guiStreets[24].setBackGroundColor(Color.red);

        guiStreets[26].setBackGroundColor(Color.white);
        guiStreets[27].setBackGroundColor(Color.white);
        guiStreets[29].setBackGroundColor(Color.white);

        guiStreets[31].setBackGroundColor(Color.yellow);
        guiStreets[32].setBackGroundColor(Color.yellow);
        guiStreets[34].setBackGroundColor(Color.yellow);

        guiStreets[37].setBackGroundColor(lilla);
        guiStreets[39].setBackGroundColor(lilla);
    }

    private void setupBreweryFields() {
        guiFields[12] = new GUI_Brewery();
        guiFields[12].setTitle(board.getFieldObject(12).getFieldName());
        guiFields[12].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[12]));
        guiFields[12].setDescription(fieldDescriptions[18]+fieldDescriptions[20]+fieldDescriptions[19]+fieldDescriptions[20]);
        guiFields[28] = new GUI_Brewery();
        guiFields[28].setTitle(board.getFieldObject(28).getFieldName());
        guiFields[28].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[28]));
        guiFields[28].setDescription(fieldDescriptions[18]+fieldDescriptions[20]+fieldDescriptions[19]+fieldDescriptions[20]);

    }

    private void formatShippingFields() {
        guiFields[5].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[5]));
        guiFields[5].setTitle(board.getFieldObject(5).getFieldName());
        guiFields[15].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[15]));
        guiFields[15].setTitle(board.getFieldObject(15).getFieldName());
        guiFields[25].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[25]));
        guiFields[25].setTitle(board.getFieldObject(25).getFieldName());
        guiFields[35].setSubText(String.format(takeTurnMessages[9],""+fieldPrice[35]));
        guiFields[35].setTitle(board.getFieldObject(35).getFieldName());
    }

    private void setupSTART() {
        guiFields[0] = new GUI_Start(
                GameController.getInstance().getBoard().getFieldObject(0).getFieldName(),
                board.getFieldObject(0).getFieldName(),
                String.format(fieldDescriptions[0], ""+GameSettings.STARTBONUS),
                Color.MAGENTA,
                Color.WHITE);
    }

    private void setupRefuge() {
        guiFields[20] = new GUI_Refuge();
        guiFields[20].setTitle(GameController.getInstance().getBoard().getFieldObject(20).getFieldName());
        guiFields[20].setSubText(fieldDescriptions[3]);
        guiFields[20].setDescription(fieldDescriptions[4]);
    }

    private void setupTaxFields() {
        guiFields[4] = new GUI_Tax(
                GameController.getInstance().getBoard().getFieldObject(4).getFieldName(),
                "",
                String.format(fieldDescriptions[1], ""+GameSettings.INCOME_TAX_AMOUNT),
                Color.lightGray,
                Color.black
        );
        guiFields[38] = new GUI_Tax(
                GameController.getInstance().getBoard().getFieldObject(38).getFieldName(),
                "",
                String.format(fieldDescriptions[2], ""+GameSettings.STATE_TAX_AMOUNT),
                Color.lightGray,
                Color.black
        );
    }

    private void setupJailFields() {
        guiFields[10] = new GUI_Jail();
        guiFields[10].setSubText(GameController.getInstance().getBoard().getFieldObject(10).getFieldName());
        guiFields[10].setDescription(fieldDescriptions[21]);

        guiFields[30] = new GUI_Jail();
        guiFields[30].setSubText(GameController.getInstance().getBoard().getFieldObject(30).getFieldName());
        guiFields[30].setDescription(fieldDescriptions[22]);
    }

}
