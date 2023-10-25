package unfp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import UFPLib.Format;
import UFPLib.IFormat;
import UNFPApp.GetFormatService;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/*
 *Class meant for all controllers with file loading capabilities 
 */
public class FileLoader extends Controller{

    protected boolean loadFiles(Window mainWindow) throws IOException
    {
        FileChooser fc = new FileChooser();
        List<File> files = fc.showOpenMultipleDialog(mainWindow.getScene().getWindow());
        if(load(files)){
            return true;
        }
        return false;
    }

    protected boolean load(List<File> files) throws IOException
    {
        if (files!=null)
        {
            GetFormatService fs = new GetFormatService();
            for (File file : files) {
                Path path = file.toPath();
                IFormat f = new Format(path);
                IFormat rootFile = fs.scanFormat(f);
                model.getLoadedFiles().add(rootFile);
            }
            return true;
        }
        return false;
    }
}
