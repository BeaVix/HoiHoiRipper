package unfp;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class ToolbarController extends FileLoader{
    @FXML private MenuBar toolbar;

    public void initModel(FilesModel model)
    {
        if(this.model != null)
        {
            throw new IllegalStateException("Model can only be initialized once");
        }else{
            this.model = model;
        }
    }

    @FXML protected void openFileEventHandler(ActionEvent event) throws IOException
    {
        loadFiles(toolbar.getScene().getWindow());
    }

    @FXML protected void close()
    {
        Platform.exit();
    }

    @FXML protected void showAbout()
    {
        try{
            FXMLLoader aboutLoader = new FXMLLoader(getClass().getClassLoader().getResource("about_view.fxml"));
            Parent root = aboutLoader.load();
            AboutController controller = aboutLoader.getController();
            controller.initModel(model);
            Stage aboutStage = new Stage();
            Scene scene = new Scene(root, 300, 400);
            aboutStage.setTitle("About");
            aboutStage.setScene(scene);
            aboutStage.setResizable(false);
            aboutStage.initStyle(StageStyle.UTILITY);
            aboutStage.show();
        }catch(IOException e)
        {
            System.out.println(e.getStackTrace());
        }

    }

    @FXML protected void spritesExtract()
    {
        String setting = "Extract Sprites";
        if(model.getSettings().get(setting)){
            model.getSettings().replace(setting, true, false);
        }else{
            model.getSettings().replace(setting, false, true);
        }
    }
}
