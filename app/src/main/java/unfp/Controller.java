package unfp;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller {
    protected Stage progressStage;
    protected FilesModel model;

    public void initModel(FilesModel model) throws IOException
    {
        if(this.model != null)
        {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }

    protected void showProgress()
    {
        FXMLLoader progressLoader = new FXMLLoader(getClass().getClassLoader().getResource("progress_view.fxml"));
        Parent root;
        try {
            root = progressLoader.load();
            progressStage = new Stage();
            Scene scene = new Scene(root, 400, 100);
            progressStage.setTitle("Loading");
            progressStage.setScene(scene);
            progressStage.setResizable(false);
            progressStage.initStyle(StageStyle.UNDECORATED);
            progressStage.setAlwaysOnTop(true);
            progressStage.show();
            System.out.println("Loading");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeProgress()
    {
        if(progressStage != null)
        {
            progressStage.close();
        }
    }
}
