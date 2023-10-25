package UFPLib;
/***
 *  The Folder class is used to represent generic folders on archive files
 */
import java.io.IOException;
import java.util.ArrayList;

public class Folder extends Format{
    private ArrayList<Folder> folderChildren;
    private ArrayList<Format> fileChildren;

    public Folder(String name, ArrayList<Folder> folderChildren, ArrayList<Format> fileChildren) throws IOException{
        super();
        this.name = name;
        this.folderChildren = folderChildren;
        this.fileChildren = fileChildren;
        this.startOffset = 0;
        this.endOffset = 0;
        for (Folder folder : folderChildren) {
            setEndOffset(getEndOffset()+folder.getEndOffset());
        }
        for (Format file : fileChildren) {
            setEndOffset(getEndOffset()+(file.getEndOffset()-file.getStartOffset()));
        }
    }

    public Folder(String name) throws IOException{
        super();
        this.name = name;
        this.folderChildren = new ArrayList<Folder>();
        this.fileChildren = new ArrayList<Format>();
        this.startOffset = 0;
        this.endOffset = 0;
    }

    public void setFolderChildren(ArrayList<Folder> folderChildren){
        this.folderChildren = folderChildren;
    }

    public void setFolderChildren(Folder... folderChildren){
        ArrayList<Folder> l = new ArrayList<Folder>();
        for (Folder child : folderChildren) {
            l.add(child);
        }
        this.folderChildren = l;
    }

    public void setFileChildren(ArrayList<Format> FileChildren){
        this.fileChildren = FileChildren;
    }

    public void setFileChildren(Format... fileChildren){
        ArrayList<Format> l = new ArrayList<Format>();
        for (Format child: fileChildren){
            l.add(child);
        }
        this.fileChildren = l;
    }

    public ArrayList<Folder> getFolderChildren()
    {
        return this.folderChildren;
    }

    public void addFolderChildren(Folder f)
    {
        this.folderChildren.add(f);
        setEndOffset(getEndOffset()+f.getEndOffset());
    }

    public ArrayList<Format> getFileChildren()
    {
        return this.fileChildren;
    }

    public void addFileChildren(Format f)
    {
        this.fileChildren.add(f);
        setEndOffset(getEndOffset()+(f.getEndOffset()-f.getStartOffset()));
    }
}
