package Services;

import Controllers.GameController;
import Models.Board;
import gui_fields.*;

import java.awt.*;

public class GUIBoardCreator {
    private GUI_Field[] guiFields;
    private GUI_Street[] guiStreets;
    private Board board = GameController.getInstance().getBoard();
    private FieldsDataReader fieldsDataReader = new FieldsDataReader("Fields.csv");
    private int[] fieldPrice = fieldsDataReader.getFieldPriceArray();
    private FileImporter fileImporter = new FileImporter();
    private String[] takeTurnMessages = fileImporter.readAllLinesInFile("GameMessages_takeTurn.txt");
    private String[] fieldDescriptions = fileImporter.readAllLinesInFile("FieldDescriptions.txt");

    public GUIBoardCreator(GUI_Field[] guiFields, GUI_Street[] guiStreets) {

        this.guiFields = guiFields;
        this.guiStreets = guiStreets;
        setUpAndFormatGUIBoard();
    }

    private void setUpAndFormatGUIBoard() {
        for (int i = 0; i < board.getTotalNumOfFields(); i++) { // sets up all GUI fields/streets
            guiStreets[i] = new GUI_Street();
            guiStreets[i].setTitle(board.getFieldObject(i).getFieldName());
            guiStreets[i].setBackGroundColor(Color.lightGray);
            guiFields[i] = guiStreets[i];
            // if a field is ownable, set sub text to price
            if (
                    !(i == 0 || i == 2 || i == 7 ||
                        i == 10 || i == 17 || i == 20 || i == 22 ||
                        i == 30 || i == 33 || i == 36 || i == 38)
            ) {
                guiFields[i].setSubText(takeTurnMessages[9]+" "+fieldPrice[i]);
            }
            // setup chance fields
            if (i == 2 || i == 7 || i == 17 || i == 22 || i == 33 || i == 36) {
                guiFields[i] = new GUI_Chance();
                guiFields[i].setDescription("");

            }
            // setup shipping fields
            if (i == 5 || i == 15 || i == 25 || i == 35) {
                guiFields[i] = new GUI_Shipping();
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

    // @Mark - denne klasse synes jeg kan/skal være helt fri for hardcodede eller importerede strenge
    // alt du behøver som fx pris og feltnavn findes allerede under board.getFieldObject(int).getFieldName for eks
    // eller, det kan godt være du skal bruge nogle få strenge såsom "kr. " + fieldPrice, eller det med skattefeltet
    // men det meste andet kan hentes fra felt-objekterne
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
        guiFields[12].setSubText(takeTurnMessages[9]+" "+fieldPrice[12]);
        guiFields[28] = new GUI_Brewery();
        guiFields[28].setTitle(board.getFieldObject(28).getFieldName());
        guiFields[28].setSubText(takeTurnMessages[9]+" "+fieldPrice[28]);

    }

    private void formatShippingFields() {
        guiFields[5].setSubText(takeTurnMessages[9]+" "+fieldPrice[5]);
        guiFields[15].setSubText(takeTurnMessages[9]+" "+fieldPrice[15]);
        guiFields[25].setSubText(takeTurnMessages[9]+" "+fieldPrice[25]);
        guiFields[35].setSubText(takeTurnMessages[9]+" "+fieldPrice[35]);
    }

    private void setupSTART() {
        guiFields[0] = new GUI_Start(
                GameController.getInstance().getBoard().getFieldObject(0).getFieldName(),
                board.getFieldObject(0).getFieldName(),
                "",
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
                fieldDescriptions[1],
                Color.lightGray,
                Color.black
        );
        guiFields[38] = new GUI_Tax(
                GameController.getInstance().getBoard().getFieldObject(38).getFieldName(),
                "",
                fieldDescriptions[2],
                Color.lightGray,
                Color.black
        );
    }

    private void setupJailFields() {
        guiFields[10] = new GUI_Jail();
        guiFields[10].setSubText(GameController.getInstance().getBoard().getFieldObject(10).getFieldName());

        guiFields[30] = new GUI_Jail();
        guiFields[30].setSubText(GameController.getInstance().getBoard().getFieldObject(30).getFieldName());
    }

    public GUI_Field[] createGuiFields() {
        return guiFields;
    }

    public GUI_Street[] createGuiStreets() {
        return guiStreets;
    }
}
