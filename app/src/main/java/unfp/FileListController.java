package unfp;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import UFPLib.Folder;
import UFPLib.Format;
import UFPLib.IFormat;
import UFPLib.UFP;
import UFPLib.PSI;
import UFPLib.PSI3;
import UNFPApp.GetFormatService;
import UNFPApp.Imager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class FileListController extends Controller{
    @FXML private TreeTableView<IFormat> fileListTV;
    @FXML private TreeTableColumn<IFormat, String> nameColumn;
    @FXML private TreeTableColumn<IFormat, String> typeColumn;
    @FXML private TreeTableColumn<IFormat, Number> sizeColumn;

    @FXML public void initialize()
    {
        fileListTV.getSelectionModel().getSelectedCells().addListener((ListChangeListener.Change<? extends TreeTablePosition<IFormat,?>> c)->{
            while(c.next())
            {
                if(c.wasAdded())
                {
                    ObservableList<TreeItem<IFormat>> selectedList = model.getListSelected();
                    if(selectedList.size() > 0)
                    {
                        selectedList.remove(0);
                    }
                    selectedList.add(c.getAddedSubList().get(0).getTreeItem());
                }
            }
        });
        fileListTV.setRoot(new TreeItem<IFormat>());
        fileListTV.setShowRoot(false);
        fileListTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allows multiple cell selection

        fileListTV.setRowFactory(p->{
            return new TreeTableRow<IFormat>(){
                @Override
                protected void updateItem(IFormat item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(empty){
                        setContentDisplay(null);
                    }else{
                        ContextMenuList ct = new ContextMenuList();
                        try
                        {
                            ct.initModel(model);
                            ct.makeContextMenu(item, this);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
        });

        nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getName()));
        typeColumn.setCellValueFactory(p -> {
            String type = p.getValue().getValue().getClass().getSimpleName();
            if(type.equals("Format"))
            {
                type = "Unknown";
            }
            return new SimpleStringProperty(type);
        });

        sizeColumn.setCellValueFactory(p -> {
            IFormat file = p.getValue().getValue();
            if(file.getEndOffset() >= 0){
                return new SimpleDoubleProperty(file.getEndOffset()-file.getStartOffset());
            }
            return new SimpleDoubleProperty(-1);
        });

        sizeColumn.setCellFactory(p ->{
            return  new TreeTableCell<IFormat, Number>() {
                @Override
                protected void updateItem(Number item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(item == null || empty || item.intValue() == -1)
                    {
                        setText(null);
                    } else
                    {
                        String unit = "MB";
                        DecimalFormat df = new DecimalFormat("#.##");
                        Double val = item.doubleValue()*Math.pow(10, -6);
                        if(Math.floor(val) == 0)
                        {
                            unit = "kB";
                            val = item.doubleValue()*Math.pow(10, -3);
                        }
                        setText(df.format(val) + unit);
                    }
                } 
            };
        });
    }

    @Override public void initModel(FilesModel model) throws IOException
    {
        if(this.model != null)
        {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        loadData(model.getLoadedFiles());
        /* Adds listener for adding and removing files */
        model.getLoadedFiles().addListener((ListChangeListener.Change<? extends IFormat> c)->
        {
            while(c.next())
            {
                if(c.wasAdded())
                {
                    try {
                        loadData((List<IFormat>) c.getAddedSubList());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadData(List<IFormat> fileList) throws IOException
    {
        for (IFormat file : fileList) {
            int id = model.getFiles().size();
            TreeItem<IFormat> root = new TreeItem<IFormat>(file);
            String type = file.getClass().getSimpleName(); 
            model.getFiles().put(id, file);
            if(type.equals("UFP")){ //renders internal file structure
                UFP rootFile = (UFP)file;
                Folder fs = rootFile.getFileSystem();
                for (Folder child : fs.getFolderChildren()) {
                    goThroughFolder(child, root, rootFile);
                }
                for (Format child : fs.getFileChildren()) {
                    GetFormatService sv = new GetFormatService();
                    IFormat cFile = sv.scanFormat(child, rootFile);
                    TreeItem<IFormat> fileItem = new TreeItem<IFormat>(cFile);
                    root.getChildren().add(fileItem);
                }
            }else if(type.equals("PSI")){
                getImage((PSI)file);
            }else if(type.equals("PSI3")){
                PSI3 psiFile = (PSI3)file;
                getImage(psiFile);
            }else{
                root.getChildren().add(new TreeItem<IFormat>(file));
            }
            root.setExpanded(true);
            fileListTV.getRoot().getChildren().add(root);
        }
    }

    /* Recursively goes through folders and copies the structure into the parent item */
    private void goThroughFolder(Folder node, TreeItem<IFormat> parentItem, IFormat parentFile) throws IOException{
        TreeItem<IFormat> folderItem = new TreeItem<IFormat>(node);
        for (Folder child : node.getFolderChildren()) {
            goThroughFolder(child, folderItem, parentFile);
        }
        for (Format child : node.getFileChildren()) {
            GetFormatService sv = new GetFormatService();
            IFormat cFile = sv.scanFormat(child, parentFile);
            TreeItem<IFormat> fileItem = new TreeItem<IFormat>(cFile);
            model.getFiles().put(model.getFiles().size(), cFile);
            folderItem.getChildren().add(fileItem);
            if(cFile.getClass().getSimpleName().equals("PSI"))
            {
                getImage((PSI)cFile);
            }else if(cFile.getClass().getSimpleName().equals("PSI3"))
            {
                getImage((PSI3)cFile);
            }
        }
        parentItem.getChildren().add(folderItem);
    }

    private void getImage(PSI f) throws IOException
    {
        PSI psiFile = (PSI)f;
        BufferedImage spriteImg = Imager.makeImage(psiFile);
        BufferedImage imagePallete = Imager.makePallete(psiFile);
        int id = model.getFileId(f)*1000;
        model.getSprtitesCanvases().put(id, spriteImg);
        model.getSpritePalleteCanvases().put(id, imagePallete);
    }

    private void getImage(PSI3 f) throws IOException
    {
        int id = model.getFileId(f)*1000;
        getImage((PSI)f);
        BufferedImage sheetImg = model.getSprtitesCanvases().getOrDefault(id, null);
        id++;
        for(int i = 0; i < f.getFileNum(); i++)
        {
            BufferedImage spriteImg = Imager.makeImage(f, sheetImg, i);
            model.getSprtitesCanvases().put(id, spriteImg);
            id++;
        }
    }
}
