package unfp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutController extends Controller {
    @FXML private Button closeBtn;

    @FXML private void close()
    {
        closeBtn.getScene().getWindow().hide();
    }
}
