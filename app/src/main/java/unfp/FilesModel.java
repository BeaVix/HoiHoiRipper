package unfp;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import UFPLib.IFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TreeItem;

public class FilesModel {

    private ObservableList<IFormat> loadedFiles;
    private ObservableMap<Integer,IFormat> files;
    private ObservableList<TreeItem<IFormat>> listSelected;
    private ObservableMap<Integer, BufferedImage> spriteImages;
    private ObservableMap<Integer, BufferedImage> spritePalleteImages;
    private ObservableMap<String, Boolean> settings;

    public FilesModel(){
        ObservableList<IFormat> list = FXCollections.observableArrayList();
        ObservableList<TreeItem<IFormat>> sel = FXCollections.observableArrayList();
        ObservableMap<Integer, IFormat> files = FXCollections.observableMap(new HashMap<Integer, IFormat>());
        ObservableMap<Integer, BufferedImage> spriteImages = FXCollections.observableMap(new HashMap<Integer, BufferedImage>());
        ObservableMap<Integer, BufferedImage> spritePalleteImages = FXCollections.observableMap(new HashMap<Integer, BufferedImage>());
        ObservableMap<String, Boolean> settings = FXCollections.observableMap(new HashMap<String, Boolean>());
        settings.put("Extract Sprites", false);
        this.loadedFiles = list;
        this.files = files;
        this.listSelected = sel;
        this.spriteImages = spriteImages;
        this.spritePalleteImages = spritePalleteImages;
        this.settings = settings;
    }

    public ObservableList<IFormat> getLoadedFiles()
    {
        return this.loadedFiles;
    }

    public ObservableList<TreeItem<IFormat>>  getListSelected()
    {
        return this.listSelected;
    }

        public ObservableMap<Integer, IFormat> getFiles()
    {
        return this.files;
    }


    public ObservableMap<Integer, BufferedImage> getSprtitesCanvases()
    {
        return this.spriteImages;
    }

    public ObservableMap<Integer, BufferedImage> getSpritePalleteCanvases()
    {
        return this.spritePalleteImages;
    }

    public ObservableMap<String, Boolean> getSettings()
    {
        return this.settings;
    }

    public void setLoadedFiles(IFormat f)
    {
        loadedFiles.add(f);
    }

    public void setListSelected(ObservableList<TreeItem<IFormat>>  f)
    {
        this.listSelected = f;
    }

    public int getFileId(IFormat f)
    {
        Set<Entry<Integer, IFormat>> entries = files.entrySet();
        for (Entry<Integer,IFormat> entry : entries) {
            if(entry.getValue().equals(f))
            {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    public Integer getFileIntegerId(IFormat f)
    {
        Set<Entry<Integer, IFormat>> entries = files.entrySet();
        for (Entry<Integer,IFormat> entry : entries) {
            if(entry.getValue().equals(f))
            {
                return entry.getKey();
            }
        }
        return -1;
    }
}
