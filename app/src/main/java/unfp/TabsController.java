package unfp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import UFPLib.IFormat;
import UFPLib.PSI;
import UFPLib.PSI3;
import UFPLib.UFP;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;

public class TabsController extends Controller{
    @FXML private TabPane tabPane;

    private HashMap<Tab, Integer> tabs;

    @Override public void initModel(FilesModel model)
    {
        if(this.model != null)
        {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        this.tabs = new HashMap<Tab, Integer>();
        /* file selected from file list */
        model.getListSelected().addListener((ListChangeListener.Change<? extends TreeItem<IFormat>> c) ->
        {
            while(c.next())
            {
                if(c.wasAdded())
                {
                    IFormat f = c.getAddedSubList().get(0).getValue();
                    String type = f.getClass().getSimpleName();
                    Integer id = model.getFileIntegerId(f);
                    if(!tabs.containsValue(id))
                    {
                        switch(type)
                        {
                            case"UFP":
                                try
                                {
                                    makeTab(f.getName(), id).updateUFPData((UFP)f);
                                }catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                            break;
                            case"PSI":
                            try
                                {
                                    makeTab(f.getName(), id).updatePSIData((PSI)f, id*1000);
                            }catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                            break;
                            case"PSI3":
                            try
                                {
                                    makeTab(f.getName(), id).updatePSI3Data((PSI3)f, id*1000);
                                }catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                            break;
                            default:
                            
                            break;
                        }
                    }
                    for (Entry<Tab, Integer> entry : tabs.entrySet()) {
                        if(entry.getValue() == id)
                        {
                            tabPane.getSelectionModel().select(entry.getKey());
                            break;
                        }
                }
                }

            }
        });
        tabPane.getTabs().addListener((ListChangeListener<? super Tab>) c->{
            while(c.next())
            {
                if(c.wasRemoved())
                {
                    tabs.remove(c.getRemoved().get(0));
                }
            }
        });
    }

    private TabContentController makeTab(String text, Integer id) throws IOException
    {
        Tab node = new Tab(text);
        FXMLLoader tabContentLoader = new FXMLLoader(getClass().getClassLoader().getResource("tab_content.fxml"));
        Parent content = tabContentLoader.load();
        TabContentController c = tabContentLoader.getController();
        c.initModel(this.model);
        node.setContent(content);
        tabPane.getTabs().add(node);
        tabs.put(node, id);
        return c;
    }
}
