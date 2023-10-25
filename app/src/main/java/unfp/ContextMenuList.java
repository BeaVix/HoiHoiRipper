package unfp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import UFPLib.Folder;
import UFPLib.IFormat;
import UFPLib.PSI;
import UFPLib.PSI3;
import UNFPApp.Extractor;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ContextMenuList extends Controller{

    public void makeContextMenu(IFormat item, TreeTableRow<IFormat> tree)
    {
        MenuItem extractRaw = new MenuItem("Extract Raw file");
        MenuItem extractType = new MenuItem("");
        ArrayList<MenuItem> items = new ArrayList<MenuItem>(5);
        items.add(extractRaw);
        items.add(extractType);
        ContextMenu ct;
        String itemType = item.getClass().getSimpleName();
        if(itemType.equals("Folder"))
        {
            extractType.setText("Extract folder (converted files)");
            extractRaw.setText("Extract folder (raw files)");
            extractRaw.setOnAction(e->folderActionRaw(item, tree));
            extractType.setOnAction(e->folderActionConvert(item, tree));
        }else{
            extractRaw.setOnAction(e->extractRaw(item, tree));
        }
        if(itemType.equals("PSI") || itemType.equals("PSI3"))
        {
            MenuItem extractPallete = new MenuItem("Extract pallete");
            items.add(extractPallete);
            String nameImg = item.getName().split("\\.", 2)[0]+".png";
            String namePlt = item.getName().split("\\.", 2)[0]+"_pallete.png";
            int id = model.getFileId(item)*1000;
            BufferedImage sprtshtimg = model.getSprtitesCanvases().getOrDefault(id, null);
            BufferedImage pltimg = model.getSpritePalleteCanvases().getOrDefault(id, null);
            extractType.setOnAction(e->extractImage(nameImg, sprtshtimg, tree));
            extractPallete.setOnAction(e->extractImage(namePlt, pltimg, tree));
            extractType.setText("Extract spritesheet");
            if(itemType.equals("PSI"))
            {
                extractType.setText("Extract sprite");
            }else{
                MenuItem extractSprites = new MenuItem("Extract Sprites...");
                items.add(extractSprites);
                extractSprites.setOnAction(e->extractSprites());
            }
        }else if(itemType.equals("UFP"))
        {
            MenuItem extractConverted = new MenuItem("Extract files(converted)");
            extractType.setText("Extract files (raw)");
            extractConverted.setOnAction(e->UFPActionConvert(item, tree));
            extractType.setOnAction(e->UFPActionRaw(item, tree));
            items.add(extractConverted);
        }
        ct = new ContextMenu(items.toArray(new MenuItem[items.size()]));
        tree.setContextMenu(ct);
    }

    private void extractRaw(IFormat item, TreeTableRow<IFormat> tree)
    {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(item.getName());
        File dir = fc.showSaveDialog(tree.getScene().getWindow());
        Path path = Paths.get(dir.getAbsolutePath());
        try {
            Extractor.extractFileRaw(item, path);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void folderActionRaw(IFormat item, TreeTableRow<IFormat> tree)
    {
        ObservableList<TreeItem<IFormat>> children = tree.getTreeItem().getChildren();
        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(tree.getScene().getWindow());
        Path path = Paths.get(dir.getAbsolutePath());
        path = Paths.get(path.toString()+"/"+tree.getTreeItem().getValue().getName());
        try {
            for(TreeItem<IFormat> c : children)
            {
                if(c.getValue().getClass().getSimpleName().equals("Folder")){
                    extractFolderRaw(path, c, (Folder)c.getValue());
                }else
                {
                    File dirs = path.toFile();
                    if(!dirs.exists())
                    {
                        dirs.mkdirs();
                    }
                    Extractor.extractFileRaw(c.getValue(), Paths.get(path.toString()+"/"+c.getValue().getName()));
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void folderActionConvert(IFormat item, TreeTableRow<IFormat> tree)
    {
        ObservableList<TreeItem<IFormat>> children = tree.getTreeItem().getChildren();
        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(tree.getScene().getWindow());
        Path path = Paths.get(dir.getAbsolutePath());
        path = Paths.get(path.toString()+"/"+tree.getTreeItem().getValue().getName());
        try {
            for(TreeItem<IFormat> c : children)
            {
                String type = c.getValue().getClass().getSimpleName();
                File dirs = path.toFile();
                if(!dirs.exists() && !type.equals("Folder"))
                {
                    dirs.mkdirs();
                }
                switch (type){
                    case "Folder":
                        extractFolderConverted(path, c, (Folder)c.getValue());
                        break;
                    case "PSI3":
                    case "PSI":
                        int id = model.getFileId(c.getValue())*1000;
                        BufferedImage image = model.getSprtitesCanvases().getOrDefault(id, null);
                        String name = c.getValue().getName();
                        Extractor.extractImage(image, Paths.get(path.toString()+"/"+name.split("\\.", 2)[0]+".png"));
                        break;
                    default:
                        Extractor.extractFileRaw(c.getValue(), Paths.get(path.toString()+"/"+c.getValue().getName()));
                        break;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void extractSprites()
    {
        FXMLLoader spriteSelLoader = new FXMLLoader(getClass().getClassLoader().getResource("sprite_select_view.fxml"));
        try {
            Parent root = spriteSelLoader.load();
            Stage spriteSelStage = new Stage();
            SpriteSelectViewController sc = spriteSelLoader.getController();
            sc.initModel(this.model);
            Scene scene = new Scene(root, 200, 300);
            spriteSelStage.setTitle("Select Sprites");
            spriteSelStage.setScene(scene);
            spriteSelStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void extractImage(String initialName, BufferedImage image, TreeTableRow<IFormat> tree)
    {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(initialName);
        File f = fc.showSaveDialog(tree.getScene().getWindow());
        if(f != null)
        {
            Extractor.extractImage(image, Paths.get(f.getAbsolutePath()));
        }
    }

    private void extractFolderRaw(Path destination, TreeItem<IFormat> parentItem, Folder item)
    {
        Path path = Paths.get(destination.toString()+"/"+item.getName());
        try {
            for(TreeItem<IFormat> c : parentItem.getChildren())
            {
                if(c.getValue().getClass().getSimpleName().equals("Folder")){
                    extractFolderRaw(path, c, (Folder)c.getValue());
                }else
                {
                    File dirs = path.toFile();
                    if(!dirs.exists())
                    {
                        dirs.mkdirs();
                    }
                    Extractor.extractFileRaw(c.getValue(), Paths.get(path.toString()+"/"+c.getValue().getName()));
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void extractFolderConverted(Path destination, TreeItem<IFormat> parentItem, Folder item)
    {
        Path path = Paths.get(destination.toString()+"/"+item.getName());
        for(TreeItem<IFormat> c : parentItem.getChildren())
        {
            extractFileConverted(c, path);
        }
    }

    private void UFPActionRaw(IFormat item, TreeTableRow<IFormat> tree)
    {
        ObservableList<TreeItem<IFormat>> children = tree.getTreeItem().getChildren();
        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(tree.getScene().getWindow());
        Path path = Paths.get(dir.getAbsolutePath());
        String name = item.getName();
        path = Paths.get(path.toString()+"/"+name.split("\\.", 2)[0]);
        for (TreeItem<IFormat> c : children) {
            if(c.getValue().getClass().getSimpleName().equals("Folder"))
            {
                extractFolderRaw(path, c, (Folder)c.getValue());
            }else{
                try{
                    Extractor.extractFileRaw(c.getValue(), Paths.get(path.toString()+"/"+c.getValue().getName()));
                }catch(IOException e)
                {
                    System.out.println(e.getStackTrace());
                }
            }
        }
    }

    private void UFPActionConvert(IFormat item, TreeTableRow<IFormat> tree)
    {
        ObservableList<TreeItem<IFormat>> children = tree.getTreeItem().getChildren();
        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(tree.getScene().getWindow());
        Path path = Paths.get(dir.getAbsolutePath());
        String name = item.getName();
        path = Paths.get(path.toString()+"/"+name.split("\\.", 2)[0]);
        for (TreeItem<IFormat> c : children) {
            extractFileConverted(c, path);
        }
    }

    private void extractFileConverted(TreeItem<IFormat> item, Path path)
    {
        IFormat file = item.getValue();
        String type = file.getClass().getSimpleName();
        File dirs = path.toFile();
        int id = model.getFileId(file)*1000;
        if(!dirs.exists() && !type.equals("Folder"))
        {
            dirs.mkdirs();
        }
        if(type.equals("Folder"))
        {
            extractFolderConverted(path, item, (Folder)file);
        }else if(type.equals("Format"))
        {
            try{
                    Extractor.extractFileRaw(file, Paths.get(path.toString()+"/"+file.getName()));
            }catch(IOException e)
            {
                System.out.println(e.getStackTrace());
            }
        }
        if(model.getSettings().get("Extract Sprites").booleanValue())
        {
            switch (type) {
                case "PSI3":
                    id++;
                    extractPSI3((PSI3)file, id, path);
                break;
                case "PSI":
                    extractPSI((PSI)file, id, path);
                break;
            }
        }else if(type.equals("PSI") || type.equals("PSI3"))
        {
            extractPSI((PSI)file, id, path);
        }
    }
    private void extractPSI3(PSI3 file, int id, Path path)
    {
        for(String name : file.getFileNames()){
            BufferedImage image = model.getSprtitesCanvases().get(id);
            Extractor.extractImage(image, Paths.get(path.toString()+"/"+name+".png"));
            id++;
        }
    }
    private void extractPSI(PSI file, int id, Path path){
        BufferedImage image = model.getSprtitesCanvases().getOrDefault(id, null);
        String name = file.getName();
        Extractor.extractImage(image, Paths.get(path.toString()+"/"+name.split("\\.", 2)[0]+".png"));
    }
}
