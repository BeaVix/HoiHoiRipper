package unfp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class InitialViewController extends FileLoader{
    @FXML private BorderPane mainBP;
    @FXML private HBox mainHB;
    
    @FXML private void initialize()
    {
        mainBP.setOnDragOver(e->{
            if(e.getGestureSource() != mainBP && e.getDragboard().hasFiles())
            {
                e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            e.consume();
        });
        mainBP.setOnDragDropped(e->{
            Dragboard db = e.getDragboard();
            if(db.hasFiles())
            {
                try{
                    if(load(db.getFiles())){
                        loadListView();
                    }
                }catch(IOException err)
                {
                    System.out.println(err.getStackTrace());
                }
            }
        });
    }

    @FXML protected void handleOpenFileAction(ActionEvent event) throws IOException {
        if(loadFiles(mainBP.getScene().getWindow()))
        {
            loadListView();
        }
    }

    private void loadListView() throws IOException
    {
        FXMLLoader dataContainerLoader = new FXMLLoader(getClass().getClassLoader().getResource("file_data_container.fxml"));
        Parent newParent = dataContainerLoader.load();
        mainBP.getScene().setRoot(newParent);
        FileDataViewController dv = dataContainerLoader.getController();
        dv.initModel(this.model);
    }
}
