package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.application.HostServices;

import java.io.InputStream;

/**
 * Controller for the alcohol info page that appears when an alcohol is selected from a search.
 */
public class AlcoholInfoController {
    DataPasser dataPass = new DataPasser();
    @FXML
    Label alcAID, alcBrandName, alcType, alcAppelation, alcSulfite, alcAlcoholContent, alcNetContent,alcHealthWarning,
          alcProductType, alcClass, alcLegibility, alcSize, alcFormula, alcInfo;
    @FXML
    Hyperlink buy, review;
    @FXML
    ImageView alcImage;
    @FXML
    Button close;

    public void initialize(){
        alcAID.setText(String.valueOf(dataPass.getAlcData().getAid()));
        alcBrandName.setText(String.valueOf(dataPass.getAlcData().getBrandName()));
        alcType.setText(String.valueOf(dataPass.getAlcData().getAlcoholType()));
        alcAppelation.setText(String.valueOf(dataPass.getAlcData().getAppellation()));
        alcSulfite.setText(String.valueOf(dataPass.getAlcData().getSulfiteDescription()));
        alcAlcoholContent.setText(String.valueOf(dataPass.getAlcData().getAlchContent()));
        alcNetContent.setText(String.valueOf(dataPass.getAlcData().getNetContent()));
        alcHealthWarning.setText(String.valueOf(dataPass.getAlcData().getHealthWarning()));
        alcProductType.setText(String.valueOf(dataPass.getAlcData().getProductType()));
        alcClass.setText(String.valueOf(dataPass.getAlcData().getClassType()));
        alcLegibility.setText(String.valueOf(dataPass.getAlcData().getLabelLegibility()));
        alcSize.setText(String.valueOf(dataPass.getAlcData().getLabelSize()));
        alcFormula.setText(String.valueOf(dataPass.getAlcData().getFormulas()));
        alcInfo.setText(String.valueOf(dataPass.getAlcData().getBottlersInfo()));

        try {
            InputStream resource = ScreenUtil.class.getClassLoader().getResourceAsStream("labels/"  + dataPass.getAlcData().getAid() + ".jfif");
            alcImage.setImage(new javafx.scene.image.Image(resource, 500.0, 0.0, true, true));
        }
        catch(NullPointerException nullPoint){
            InputStream resource = ScreenUtil.class.getClassLoader().getResourceAsStream("labels/imageUnavailable.jpg");
            alcImage.setImage(new javafx.scene.image.Image(resource, 100.0, 0.0, true, true));
            System.out.println("Image Was Not Found For " + dataPass.getAlcData().getBrandName() + "'s "+ dataPass.getAlcData().getName());
        }

    }

    public void closeWindow(){
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void buy(ActionEvent event){
        Application a = new Application() {
            @Override
            public void start(Stage primaryStage) throws Exception {

            }
        };
        a.getHostServices().showDocument("http://www.wine-searcher.com/find/" + alcBrandName.getText());
    }
    @FXML
    public void review(ActionEvent event){
        Application a = new Application() {
            @Override
            public void start(Stage primaryStage) throws Exception {

            }
        };
        a.getHostServices().showDocument("https://www.google.com/search?q=" + alcBrandName.getText() + " reviews");
       // a.getHostServices().showDocument("https://www.tripadvisor.com/Search?geo=&pid=3825&redirect=&startTime=&uiOrigin=&q=" + alcBrandName.getText());
    }
}
