package unfp;

import java.awt.image.BufferedImage;
import java.io.IOException;

import UFPLib.PSI;
import UFPLib.PSI3;
import UFPLib.UFP;
import UNFPApp.Imager;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.embed.swing.SwingFXUtils;

public class TabContentController extends Controller{
    @FXML private AnchorPane anchorPane;
    @FXML private ScrollPane scrollPane;

    public void updateUFPData(UFP f)
    {
        int fileN = f.getFileNum();
        String fileno = "File total: "+fileN;
        Label t = new Label(fileno);
        anchorPane.getChildren().addAll(t);
    }

    public void updatePSIData(PSI f, int id) throws IOException
    {
        VBox vb = new VBox();
        makeCanvas(id, vb);
        anchorPane.getChildren().add(vb);
    }

    public void updatePSI3Data(PSI3 f, int id) throws IOException
    {
        int i = 0;
        VBox vb = new VBox();
        makeCanvas(id, vb);
        BufferedImage sheetImg = model.getSprtitesCanvases().getOrDefault(id, null);
        id++;
        
        for(String name : f.getFileNames())
        {
            int width = f.getImageSizes().get(i)[0];
            int height = f.getImageSizes().get(i)[1];
            BufferedImage spriteImg = model.getSprtitesCanvases().getOrDefault(id, sheetImg);

            String imgs = "'"+name+"' sprite view:";
            String wString = "Width: "+width+" px";
            String hString = "Heigth: "+height+" px";
            Label t = new Label(imgs);
            Label wLabel = new Label(wString);
            Label hLabel = new Label(hString);
            Label lLabel = new Label(""); //Line break label
            Canvas c = new Canvas(width, height);
            WritableImage wImg = SwingFXUtils.toFXImage(spriteImg, null);
            c.getGraphicsContext2D().drawImage(wImg, 0.0, 0.0);
            
            vb.getChildren().addAll(t, c, wLabel, hLabel, lLabel);
            i++;
            id++;
        }
        anchorPane.getChildren().add(vb);
    }

    private void makeCanvas(int id, VBox vb) throws IOException
    {
        BufferedImage imagePallete = model.getSpritePalleteCanvases().getOrDefault(id, null);
        BufferedImage imageSprite = model.getSprtitesCanvases().getOrDefault(id, null);
        int width = imageSprite.getWidth();
        int height = imageSprite.getHeight();

        Label pallete = new Label("Color Pallete:");
        Label fullImgLabel = new Label("Spritesheet:");
        Label imgWLabel = new Label("Width: "+width+" px");
        Label imgHLabel = new Label("Heigth: "+height+" px");
        Label nLabel = new Label(""); //Line break label
        Canvas palC = new Canvas(160, 160);
        Canvas imgCanvas = new Canvas(width, height);
        palC.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(imagePallete, null), 0, 0);
        imgCanvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(imageSprite, null), 0, 0);

        vb.getChildren().addAll(pallete, palC, fullImgLabel, imgCanvas, imgWLabel, imgHLabel, nLabel);
    }
}
