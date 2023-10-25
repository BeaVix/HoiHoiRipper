package unfp;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;

public class FileDataViewController extends Controller{
    @FXML private Parent fileListInclude;
    @FXML private FileListController fileListIncludeController;
    @FXML private Parent toolbarInclude;
    @FXML private ToolbarController toolbarIncludeController;
    @FXML private Parent fileDataTabs;
    @FXML private TabsController fileDataTabsController;
    @FXML private ButtonBar buttonbarInclude;

    @Override public void initModel(FilesModel model) throws IOException
    {
        if(this.model != null)
        {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        fileListIncludeController.initModel(model);
        toolbarIncludeController.initModel(model);
        fileDataTabsController.initModel(model);
    }
}
