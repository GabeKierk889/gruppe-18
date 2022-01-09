package Services;

import Controllers.GameController;
import Models.Board;
import gui_fields.*;

import java.awt.*;

public class GUIBoardCreator {
    private GUI_Field[] guiFields;
    private GUI_Street[] guiStreets;
    private Board board = GameController.getInstance().getBoard();

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
        }

        colorStreets();
        setupShippingFields();
        setupBreweryFields();
        setupSTART();
        setupChanceFields();
        setupRefuge();
        setupJailFields();
        setupTaxFields();
    }

    // @Mark - denne klasse synes jeg kan/skal være helt fri for hardcodede eller importerede strenge
    // alt du behøver som fx pris og feltnavn findes allerede under board.getFieldObject(int).getFieldName for eks
    // eller, det kan godt være du skal bruge nogle få strenge såsom "kr. " + fieldPrice, eller det med skattefeltet
    // men det meste andet kan hentes fra felt-objekterne
    private void colorStreets() {
        Color orange = new Color(255, 165, 0);
        Color lilla = new Color(128, 0, 128);

        guiStreets[1].setBackGroundColor(Color.blue);
        guiStreets[1].setSubText("beløb");

        guiStreets[3].setBackGroundColor(Color.blue);
        guiStreets[3].setSubText("beløb");

        guiStreets[6].setBackGroundColor(orange);
        guiStreets[6].setSubText("beløb");
        guiStreets[8].setBackGroundColor(orange);
        guiStreets[8].setSubText("beløb");
        guiStreets[9].setBackGroundColor(orange);
        guiStreets[9].setSubText("beløb");

        guiStreets[11].setBackGroundColor(Color.green);
        guiStreets[11].setSubText("beløb");
        guiStreets[13].setBackGroundColor(Color.green);
        guiStreets[13].setSubText("beløb");
        guiStreets[14].setBackGroundColor(Color.green);
        guiStreets[14].setSubText("beløb");


        guiStreets[16].setBackGroundColor(Color.gray);
        guiStreets[16].setSubText("beløb");
        guiStreets[18].setBackGroundColor(Color.gray);
        guiStreets[18].setSubText("beløb");
        guiStreets[19].setBackGroundColor(Color.gray);
        guiStreets[19].setSubText("beløb");

        guiStreets[21].setBackGroundColor(Color.red);
        guiStreets[21].setSubText("beløb");
        guiStreets[23].setBackGroundColor(Color.red);
        guiStreets[23].setSubText("beløb");
        guiStreets[24].setBackGroundColor(Color.red);
        guiStreets[24].setSubText("beløb");

        guiStreets[26].setBackGroundColor(Color.white);
        guiStreets[26].setSubText("beløb");
        guiStreets[27].setBackGroundColor(Color.white);
        guiStreets[27].setSubText("beløb");
        guiStreets[29].setBackGroundColor(Color.white);
        guiStreets[29].setSubText("beløb");

        guiStreets[31].setBackGroundColor(Color.yellow);
        guiStreets[31].setSubText("beløb");
        guiStreets[32].setBackGroundColor(Color.yellow);
        guiStreets[32].setSubText("beløb");
        guiStreets[34].setBackGroundColor(Color.yellow);
        guiStreets[34].setSubText("beløb");

        guiStreets[37].setBackGroundColor(lilla);
        guiStreets[37].setSubText("beløb");
        guiStreets[39].setBackGroundColor(lilla);
        guiStreets[39].setSubText("beløb");

    }

    private void setupShippingFields() {
        guiFields[5] = new GUI_Shipping();
        guiStreets[5].setSubText("beløb");
        guiFields[15] = new GUI_Shipping();
        guiStreets[15].setSubText("beløb");
        guiFields[25] = new GUI_Shipping();
        guiStreets[25].setSubText("beløb");
        guiFields[35] = new GUI_Shipping();
        guiStreets[35].setSubText("beløb");
    }

    private void setupBreweryFields() {
        guiFields[12] = new GUI_Brewery();
        guiFields[12].setTitle("Tuborg");
        guiFields[12].setSubText("beløb");
        guiFields[28] = new GUI_Brewery();
        guiFields[28].setTitle("Carlsberg");
        guiFields[28].setSubText("beløb");
    }

    private void setupSTART() {
        guiFields[0] = new GUI_Start(
                GameController.getInstance().getBoard().getFieldObject(0).getFieldName(),
                "start",
                "passér start tekst",
                Color.MAGENTA,
                Color.WHITE);
    }

    private void setupChanceFields() {
        guiFields[2] = new GUI_Chance(
                "?",
                "Prøv lykken",
                "Prøv lykken",
                Color.black,
                Color.green
        );
        guiFields[7] = new GUI_Chance(
                "?",
                "Prøv lykken",
                "Prøv lykken",
                Color.black,
                Color.green
        );
        guiFields[17] = new GUI_Chance(
                "?",
                "Prøv lykken",
                "Prøv lykken",
                Color.black,
                Color.green
        );
        guiFields[22] = new GUI_Chance(
                "?",
                "Prøv lykken",
                "Prøv lykken",
                Color.black,
                Color.green
        );
        guiFields[33] = new GUI_Chance(
                "?",
                "Prøv lykken",
                "Prøv lykken",
                Color.black,
                Color.green
        );
        guiFields[36] = new GUI_Chance(
                "?",
                "Prøv lykken",
                "Prøv lykken",
                Color.black,
                Color.green
        );
    }

    private void setupRefuge() {
        guiFields[20] = new GUI_Refuge();
    }

    private void setupTaxFields() {
        guiFields[4] = new GUI_Tax(
                "Indkomstskat",
                "",
                "Betal inkomstskat: 10% eller kr. 4000",
                Color.lightGray,
                Color.black
        );
        guiFields[38] = new GUI_Tax(
                "Statsskat",
                "",
                "Ekstraordinær statsskat: Betal kr. 2000",
                Color.lightGray,
                Color.black
        );
    }

    private void setupJailFields() {
        guiFields[10] = new GUI_Jail();
        guiFields[10].setSubText("På besoeg");

        guiFields[30] = new GUI_Jail();
        guiFields[30].setSubText("De fængsles");
    }

    public GUI_Field[] createGuiFields() {
        return guiFields;
    }

    public GUI_Street[] createGuiStreets() {
        return guiStreets;
    }
}
