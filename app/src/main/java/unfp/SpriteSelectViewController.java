package unfp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import UFPLib.IFormat;
import UFPLib.PSI3;
import UNFPApp.Extractor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SpriteSelectViewController extends Controller{
    @FXML private ListView<String> listView;
    @FXML private CheckBox selectCheckBox;
    @FXML private CheckBox deselectCheckBox;
    private ObservableMap<String, SimpleBooleanProperty> checkedSprites;
    private ObservableMap<String, Integer> spriteIds;

    @Override public void initModel(FilesModel model)
    {
        if(this.model != null)
        {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;

        setCheckedSprites(FXCollections.observableMap(new HashMap<String, SimpleBooleanProperty>()));
        this.spriteIds = FXCollections.observableMap(new HashMap<String, Integer>());

        TreeItem<IFormat> selected = model.getListSelected().get(0);
        PSI3 file = (PSI3)selected.getValue();
        ArrayList<String> names = file.getFileNames();
        int i = model.getFileId(file)*1000+1;
        for (String name : names) {
            spriteIds.put(name, i);
            getCheckedSprites().put(name, new SimpleBooleanProperty(true));
            i++;
        }
        listView.setItems(FXCollections.observableArrayList(names));
    }

    @FXML public void initialize()
    {
        listView.setCellFactory(CheckBoxListCell.forListView(c-> {
            return checkedSprites.get(c);
        }));
    }

    @FXML protected void extractSprites()
    {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(listView.getScene().getWindow());
        checkedSprites.forEach((k, v)->{
            if(v.get())
            {
                String name = k+".png";
                BufferedImage spriteImage = model.getSprtitesCanvases().get(spriteIds.get(k));
                if(f != null)
                {
                    Extractor.extractImage(spriteImage, Paths.get(f.getAbsolutePath()+"/"+name));
                }
            }
        });
    }

    @FXML protected void cancel()
    {
        ((Stage)listView.getScene().getWindow()).close();
    }

    @FXML protected void select()
    {
        Collection<SimpleBooleanProperty> val = checkedSprites.values();
        val.forEach(c->{ //do it like this because hashmap.containsValue does not work with SimpleBooleanProperties
            if(!c.get())
            {
                checkedSprites.replaceAll((k, v)->new SimpleBooleanProperty(true));
                listView.refresh();
                deselectCheckBox.selectedProperty().set(false);
                return;
            }
        });
        selectCheckBox.selectedProperty().set(true);
    }

    @FXML protected void deselect()
    {
        Collection<SimpleBooleanProperty> val = checkedSprites.values();
        val.forEach(c->{
            if(c.get())
            {
                checkedSprites.replaceAll((k, v)->new SimpleBooleanProperty(false));
                listView.refresh();
                selectCheckBox.selectedProperty().set(false);
                return;
            }
        });
        deselectCheckBox.selectedProperty().set(true);
    }

    private void setCheckedSprites(ObservableMap<String, SimpleBooleanProperty>a)
    {
        this.checkedSprites = a;
    }

    private ObservableMap<String, SimpleBooleanProperty> getCheckedSprites()
    {
        return this.checkedSprites;
    }
}
