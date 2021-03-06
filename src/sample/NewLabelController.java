package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.control.TextField;

import javax.imageio.ImageIO;
import java.util.Random;
/**
 * Controller for new label screen.
 */
public class NewLabelController{

    //  ApplicationUtil appUtil = new ApplicationUtil();
    DatabaseUtil db = new DatabaseUtil();
    WorkflowFacade facadeWork = new WorkflowFacade();
    ScreenUtil work = new ScreenUtil();

    @FXML private TextField myFilePath;
    @FXML private ImageView image;
    @FXML private TextField RepID;
    @FXML private TextField PlantReg;
    @FXML private TextField SerialNo;
    @FXML private TextField ApplicantName;
    @FXML private TextField Varietal;
    @FXML private TextField Appellation;
    @FXML private TextField BrandName;
    @FXML private TextField Name;
    @FXML private TextField Formula;
    @FXML private TextField PhoneNumber;
    @FXML private TextField EmailAddress;
    @FXML private CheckBox dom1;
    @FXML private CheckBox dom11;
    @FXML private CheckBox dom111;
    @FXML private CheckBox dom;
    @FXML private CheckBox imp;
    @FXML private CheckBox wine;
    @FXML private CheckBox beer;
    @FXML private CheckBox other;
    @FXML private TextField Vintage;
    @FXML private TextField pH;
    @FXML private TextField Address;
    @FXML private TextField MailingAddress;
    @FXML private TextField Content;
    @FXML private TextField type2Box;
    @FXML private TextField type3box;

    @FXML private TextField originField;
    @FXML private TextField netContentField;
    @FXML private TextField sulfiteField;
    @FXML private TextField bottlerField;
    @FXML private TextField healthWarningField;

    @FXML private Button Submit;
    @FXML private Button back;
    @FXML private Button clear;
    @FXML private Button helpNewButton;
    private String filepath;
    private File tempFile;
    private String ttb;
    @FXML
    /**
     * Clears information from the screen.
     */
    private void setClear(){
        ScreenUtil work = new ScreenUtil();
        work.switchScene("NewLabel.fxml", "New Label");
    }

    @FXML
    private void handledomBox(){
        if(dom.isSelected()){
            imp.setSelected(false);
        }
    }
    @FXML
    private void handleimpBox(){
        if(imp.isSelected()){
            dom.setSelected(false);
        }
    }

    @FXML
    private void handlewineBox(){
        if(wine.isSelected()){
            beer.setSelected(false);
            other.setSelected(false);

        }
    }

    @FXML
    private void handlebeerBox(){
        if(beer.isSelected()){
            wine.setSelected(false);
            other.setSelected(false);
        }
    }

    @FXML
    private void handleotherBox(){
        if(other.isSelected()){
            beer.setSelected(false);
            wine.setSelected(false);
        }
    }
    public void goBack (ActionEvent event){
        work.switchScene("MainMenu.fxml","Main Menu");
    }

    /**
     * Fills out an application in the database.
     * @throws SQLException
     */
    public void fillOutApplication() throws SQLException{

        boolean valid = true;
        int fid = 1;
        String ttbid = "";
        int repid = 0;
        String serial = "";
        String address;
        String fancyName = "";
        String formula = "";
        String grape_varietal = "";
        String appellation = "";
        int permit_no = 0;
        String infoOnBottle = "";
        String source_of_product = "";
        String type_of_product = "";
        String brand_name;
        String phone_number;
        String email;
        String date = "";
        String applicantName = "";
        String alcoholType = "";
        String alcoholContent = "";
        double alcoholContentDouble = 0;
        int type1 = -1;
        String type2 = "";
        int type3 = -1;
        int vintage_date = 0;
        double ph_level = 0;
        int max = 999999999;

        int originCode = 0;
        double netContentDouble = 0;
        String sulfiteDesc= "";
        String bottlerInfo= "";
        String healthWarningText= "";


        if(!dom1.isSelected() && !dom11.isSelected() && !dom111.isSelected()){
            valid = false;
            work.createAlertBox("ERROR", "Please choose applicable box(es)");
        }else{
            if(dom1.isSelected()){
                type1 = 1;
            }
            if(dom11.isSelected()){
                type2  = type2Box.getText();
            }
            if(dom111.isSelected()){
                try {
                    type3 = Integer.parseInt(type3box.getText());
                }catch (NumberFormatException e){
                    work.createAlertBox("ERROR", "Invalid Input for Amount");
                    valid = false;
                }
            }
        }

        if(!RepID.getText().trim().isEmpty()) {
            try {
                repid = Integer.parseInt(RepID.getText());
                if (repid > max) {
                    work.createAlertBox("ERROR", "Input for RepID is too big");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                work.createAlertBox("ERROR", "Invaid Input for RepID");
                valid = false;
            }
        }else{
            work.createAlertBox("ERROR", "Rep ID is empty");
            valid = false;
        }

        if(!PlantReg.getText().trim().isEmpty()) {
            try {
                permit_no = Integer.parseInt(PlantReg.getText());
                if (permit_no > max) {
                    work.createAlertBox("ERROR", "Input for PlantReg is too big");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                work.createAlertBox("ERROR", "Invalid Input for PlantReg");
                valid = false;
            }
        }else{
            work.createAlertBox("ERROR", "Plant Reg is empty");
            valid = false;
        }

        if(!Formula.getText().trim().isEmpty()){
            formula = Formula.getText();
        }else{
            work.createAlertBox("ERROR", "Formula is empty");
        }

        if(!Appellation.getText().trim().isEmpty()){
            appellation = Appellation.getText();
      }else if(wine.isSelected()){
            work.createAlertBox("ERROR", "Appellation is empty");
            valid = false;
        }

        /*if(!MailingAddress.getText().trim().isEmpty()){
            address = MailingAddress.getText();
        }else{
            work.createAlertBox("ERROR", "Mailling Address is empty");
            valid = false;
        }*/

        if(!Varietal.getText().trim().isEmpty()){
            grape_varietal = Varietal.getText();
        }else if(wine.isSelected()){
            work.createAlertBox("ERROR", "Varietal is empty");
            valid = false;
        }

        if(!ApplicantName.getText().trim().isEmpty()){
            applicantName = ApplicantName.getText();
        }else{
            work.createAlertBox("ERROR", "Applicant Name is empty");
            valid = false;
        }

        if(!Content.getText().trim().isEmpty()){
            try {
                alcoholContentDouble = Double.parseDouble(Content.getText());
                if(alcoholContentDouble > 66666 || alcoholContentDouble < 0){
                    work.createAlertBox("ERROR", "Invaid Input for Alcohol Content");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                work.createAlertBox("ERROR", "Invaid Input for Alcohol content");
                valid = false;
            }
            alcoholContent = Content.getText();
        }else{
            work.createAlertBox("ERROR", "Alcohol Content is empty");
            valid = false;
        }

        if(!SerialNo.getText().trim().isEmpty()){
            serial = (SerialNo.getText());
        }else{
            work.createAlertBox("ERROR", "Serial No is empty");
            valid = false;
        }


        if (dom.isSelected()) {
            source_of_product = "DOMESTIC";
        } else if (imp.isSelected()) {
            source_of_product = "IMPORTED";
        }if(!(dom.isSelected()) && !(imp.isSelected())) {
            work.createAlertBox("ERROR", "Please select Domestic or Imported");
            valid = false;
        }

        if (wine.isSelected()) {
            type_of_product = "WINE";
            alcoholType = "WINE";
        } else if (beer.isSelected()) {
            type_of_product = "MALT BEVERAGES";
            alcoholType = "MALT BEVERAGES";
        } else if (other.isSelected()) {
            type_of_product = "DISTILLED SPIRITS";
            alcoholType = "DISTILLED SPIRITS";
        }else{
            work.createAlertBox("ERROR", "Please selecct the type of product");
            valid = false;
        }

        if(!BrandName.getText().trim().isEmpty()) {
            brand_name = BrandName.getText();
        }else{
            work.createAlertBox("ERROR", "BrandName is empty");
            valid = false;
            brand_name = "";
        }
        if(!PhoneNumber.getText().trim().isEmpty()){
            phone_number = PhoneNumber.getText();
        }else{
            work.createAlertBox("ERROR", "PhoneNumber is empty");
            valid = false;
            phone_number = "";
        }
        if(!EmailAddress.getText().trim().isEmpty()){
            email = EmailAddress.getText();
        }else{
            work.createAlertBox("ERROR", "Email is empty");
            valid = false;
            email = "";
        }

        if(!Name.getText().trim().isEmpty()){
            fancyName = Name.getText();
        }else{
            work.createAlertBox("ERROR", "FancyName is empty");
            valid = false;
        }

        if(!Address.getText().trim().isEmpty()){
            address = Address.getText();
        }else{
            work.createAlertBox("ERROR", "Address is empty");
            address = "";
        }

        if(!Vintage.getText().trim().isEmpty()){
            try{
                vintage_date = Integer.parseInt(Vintage.getText());
                if(vintage_date > max || vintage_date < 0){
                    work.createAlertBox("ERROR", "Invalid Input for VintageDate");
                    valid = false;
                }
            }catch (NumberFormatException e){
                work.createAlertBox("ERROR", "Not a Number");
                valid = false;
            }
        }else if(wine.isSelected()){
            work.createAlertBox("ERROR", "Vintage is empty");
            valid = false;
        }
        if(!pH.getText().trim().isEmpty()){
            try{
                ph_level = Double.parseDouble(pH.getText());
                if(ph_level > 14 || ph_level < 0){
                    work.createAlertBox("ERROR", "Invalid Input for PH ");
                    valid = false;
                }
            }catch (NumberFormatException e){
                work.createAlertBox("ERROR", "Not a Number");
                valid = false;
            }
        }else if(wine.isSelected()){
            work.createAlertBox("ERROR", "PH is empty");
            valid = false;
        }

        if(!originField.getText().trim().isEmpty()) {
            try {
                originCode = Integer.parseInt(originField.getText());
                if(originCode > 66666 || originCode < 0){
                    work.createAlertBox("ERROR", "Invaid Input for Origin Code");
                    valid = false;
                }

            } catch (NumberFormatException e) {
                work.createAlertBox("ERROR", "Invaid Input for Origin Code");
                valid = false;
            }
        }else{
            work.createAlertBox("ERROR", "Origin Code is empty");
            valid = false;
        }

        if(!netContentField.getText().trim().isEmpty()) {
            try {
                netContentDouble = Double.parseDouble(netContentField.getText());
                if(netContentDouble > 66666 || netContentDouble < 0){
                    work.createAlertBox("ERROR", "Invaid Input for Net Content");
                    valid = false;
                }

            } catch (NumberFormatException e) {
                work.createAlertBox("ERROR", "Invalid Input for Net Content");
                valid = false;
            }
        }else{
            work.createAlertBox("ERROR", "Net content is empty");
            valid = false;
        }

        sulfiteDesc= sulfiteField.getText();
        bottlerInfo= bottlerField.getText();
        healthWarningText= healthWarningField.getText();
        int aid;
        int getId;
        AcceptanceInformation acceptanceInfo = new AcceptanceInformation(date, applicantName,
                null, "UNASSIGNED");

        if(valid){
            if (wine.isSelected()) {
                if(work.createConfirmBox("Confirm", "Would you like to submit the form?")){
                    WineApplicationData Data = new WineApplicationData(fid, acceptanceInfo,ttbid, repid, serial,address,
                            fancyName, formula, grape_varietal, appellation, permit_no, infoOnBottle,
                            source_of_product, type_of_product, brand_name, phone_number, email, date, applicantName,
                            alcoholType, alcoholContent, type1, type2, type3,vintage_date, ph_level);
                    getId = submitWine(Data);

                    // Add to alcohol
                    aid = db.addAlcohol(fancyName, appellation, sulfiteDesc, alcoholContentDouble, netContentDouble, healthWarningText, 0, 0, "n/a", 0, "n/a", 2, bottlerInfo, brand_name);
                    saveImage(ttb, aid);
                    //connect form and alcohol
                    db.updateAlcoholIDForForm(aid, getId);

                    System.out.println("It Works");
                    work.switchScene("NewApp.fxml", "New Application");
                }

            } else if (beer.isSelected()) {
                if(work.createConfirmBox("Confirm", "Would you like to submit the form?")){
                    BeerApplicationData Data = new BeerApplicationData(fid, acceptanceInfo,ttbid, repid, serial,address,
                            fancyName, formula, permit_no, infoOnBottle,
                            source_of_product, type_of_product, brand_name, phone_number, email, date, applicantName,
                            alcoholType, alcoholContent, type1, type2, type3);

                    getId = submitBeer(Data);

                    // Add to alcohol
                    aid = db.addAlcohol(fancyName, appellation, sulfiteDesc, alcoholContentDouble, netContentDouble, healthWarningText, type1, 0, "n/a", 0, "n/a", 1, bottlerInfo, brand_name);
                    saveImage(ttb, aid);
                    //connect form and alcohol
                    db.updateAlcoholIDForForm(aid, getId);

                    System.out.println("This works");
                    work.switchScene("NewApp.fxml", "New Application");
                }

            } else if (other.isSelected()) {
                if(work.createConfirmBox("Confirm", "Would you like to submit the form?")){
                    BeerApplicationData Data = new BeerApplicationData(fid, acceptanceInfo,ttbid, repid, serial,address,
                            fancyName, formula, permit_no, infoOnBottle,
                            source_of_product, type_of_product, brand_name, phone_number, email, date, applicantName,
                            alcoholType, alcoholContent, type1, type2, type3);
                    getId = submitDistilledSpirits(Data);
                    // Add to alcohol
                    aid = db.addAlcohol(fancyName, appellation, sulfiteDesc, alcoholContentDouble, netContentDouble, healthWarningText, 0, 0, "n/a", 0, "n/a", 3, bottlerInfo, brand_name);
                    saveImage(ttb, aid);
                    //connect form and alcohol
                    db.updateAlcoholIDForForm(aid, getId);

                    System.out.println("This works too");
                    work.switchScene("NewApp.fxml", "New Application");

                }
            }
        }

    }

    /**
     * Submits a wine to the database.
     * @param wd Instance of WineApplicationData.
     * @throws SQLException
     */
    public int submitWine(WineApplicationData wd)throws SQLException{
        String ttbid = db.getNewTTBID();
        ttb = ttbid;
        int repid = wd.getRepid();
        String serial = wd.getSerial();
        String address = wd.getAddress();
        String fancyName = wd.getFancyName();
        String formula = wd.getFormula();
        String grape_varietal = wd.getGrape_varietal();
        String appellation = wd.getAppellation();
        int permit_no = wd.getPermit_no();
        String infoOnBottle = wd.getInfoOnBottle();
        String source_of_product = wd.getSource_of_product();
        String type_of_product = wd.getType_of_product();
        String brand_name = wd.getBrand_name();
        String phone_number = wd.getPhone_number();
        String email = wd.getEmail();
        String date = wd.getDate();
        String dateFormat = date.toString();
        String applicantName = wd.getApplicantName();
        String alcoholType = wd.getAlcoholType();
        String alcoholContent = wd.getAlcoholContent();
        int vintage_date = wd.getVintage_date();
        double ph_level = wd.getPh_level();
        String status = wd.getAcceptanceInfo().getStatus();
        int type1 = wd.getType1();
        String type2 = wd.getType2();
        int type3 = wd.getType3();
        int id;
        id = db.addWineForm(ttbid,repid, serial,address, fancyName, formula, grape_varietal, appellation, permit_no, infoOnBottle, source_of_product,
                type_of_product, brand_name, phone_number, email, dateFormat, applicantName,alcoholType,
                vintage_date, ph_level, alcoholContent, status, type1, type2, type3);

        roundRobin();


        return id;

    }

    /**
     * Chooses an image file for the label.
     * @param event "Choose File" button pressed.
     */

    public void chooseFile(ActionEvent event)throws Exception{
            tempFile = work.openFileChooser();
            if (tempFile != null) {
                //myFilePath.setText(tempFile.getPath());
                filepath = tempFile.toURI().toString();
                System.out.println(filepath);
                javafx.scene.image.Image img = new Image(filepath);
                image.setImage(img);
            }

    }

    /**
     * Submits a beer application to the database.
     * @param bd Instance of BeerApplicationData.
     * @throws SQLException
     */
    public int submitBeer(BeerApplicationData bd) throws SQLException{
        String ttbid = db.getNewTTBID();
        ttb = ttbid;
        int repid = bd.getRepid();
        String serial = bd.getSerial();
        String address = bd.getAddress();
        String fancyName = bd.getFancyName();
        String formula = bd.getFormula();
        int permit_no = bd.getPermit_no();
        String infoOnBottle = bd.getInfoOnBottle();
        String source_of_product = bd.getSource_of_product();
        String type_of_product = bd.getType_of_product();
        String brand_name = bd.getBrand_name();
        String phone_number = bd.getPhone_number();
        String email = bd.getEmail();
        String date = bd.getDate();
        String dateFormat = date.toString();
        String applicantName = bd.getApplicantName();
        String alcoholType = bd.getAlcoholType();
        String alcoholContent = bd.getAlcoholContent();
        String status = bd.getAcceptanceInfo().getStatus();
        int type1 = bd.getType1();
        String type2 = bd.getType2();
        int type3 = bd.getType3();
        int id;
        id = db.addBeerForm(ttbid, repid, serial, address, fancyName, formula, permit_no, infoOnBottle, source_of_product, type_of_product, brand_name, phone_number, email,
                dateFormat, applicantName, alcoholType, alcoholContent, status, type1, type2, type3);


        roundRobin();

        return id;
    }

    public int  submitDistilledSpirits(BeerApplicationData bd) throws SQLException{
        String ttbid = db.getNewTTBID();
        ttb = ttbid;
        int repid = bd.getRepid();
        String serial = bd.getSerial();
        String address = bd.getAddress();
        String fancyName = bd.getFancyName();
        String formula = bd.getFormula();
        int permit_no = bd.getPermit_no();
        String infoOnBottle = bd.getInfoOnBottle();
        String source_of_product = bd.getSource_of_product();
        String type_of_product = bd.getType_of_product();
        String brand_name = bd.getBrand_name();
        String phone_number = bd.getPhone_number();
        String email = bd.getEmail();
        String date = bd.getDate();
        String dateFormat = date.toString();
        String applicantName = bd.getApplicantName();
        String alcoholType = bd.getAlcoholType();
        String alcoholContent = bd.getAlcoholContent();
        String status = bd.getAcceptanceInfo().getStatus();
        int type1 = bd.getType1();
        String type2 = bd.getType2();
        int type3 = bd.getType3();
        int id;
        id = db.addDistilledSpiritsForm(ttbid, repid, serial, address, fancyName, formula, permit_no, infoOnBottle, source_of_product, type_of_product, brand_name, phone_number, email,
                dateFormat, applicantName, alcoholType, alcoholContent, status, type1, type2, type3);

        roundRobin();

        return id;
    }

    /**
     * Assigns new applications to government workers.
     * @throws SQLException
     */
    public void roundRobin() throws  SQLException{
        System.out.println("Running roundrobin");
        ArrayList<ApplicationData> unAssignedForms = db.searchUnassignedForms();
        System.out.println("Unassigned forms = "+ unAssignedForms.size());
        if(!(unAssignedForms.size() == 0)){
            for(int i = 0; i < unAssignedForms.size(); i++) {;
                int GOVID = db.searchMinWorkLoad();
                System.out.println("Found govid with min workload = " + GOVID);
                db.assignForm(GOVID, unAssignedForms.get(i));
                System.out.println("FORM ID "+ unAssignedForms.get(i) + " ASSIGNED");
            }
        }

    }

    public void buttonClicked (){
            work.switchScene("NewHelp.fxml","Help");
    }

    public void exportPDF () {


    }

    public String getPath() throws UnsupportedEncodingException{


        URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        String parentPath = new File(jarPath).getParentFile().getPath();

        String fileSeparator = System.getProperty("file.separator");
        String newDir = parentPath + fileSeparator + "images" + fileSeparator;

        System.out.println(newDir);

        return newDir;
    }

    public void saveImage(String id, int aid){
        BufferedImage image2 = null;
        BufferedImage image3 = null;
        try {
            String path = getPath();
            image2 = ImageIO.read(tempFile);
            image3 = ImageIO.read(tempFile);
            ImageIO.write(image2, "jpg", new File(path + "/" + id + ".jpg"));
            ImageIO.write(image3, "jpg", new File(path + "/" + aid + ".jpg"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
