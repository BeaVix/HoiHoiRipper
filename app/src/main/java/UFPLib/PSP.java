package UFPLib;
/**
 * Abstraction of the PSP file format that holds 3D model data.
 */

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class PSP extends Format{
    private int fileNum;
    private ArrayList<String>fileNames;
    private Folder fileSystem; //files contained within the PSP
    
    public PSP(Path path) throws IOException
    {
        super(path);
        read();
    }

    public PSP(String name, IFormat parent, Long startOffset, Long endOffset) throws IOException
    {
        super(name, parent, startOffset, endOffset);
        read();
    }

    private void read() throws IOException
    {
        readFileNames();
        readFileSystem();
    }

    public int getFileNum()
    {
        return fileNum;
    }

    public Folder getFileSystem()
    {
        return fileSystem;
    }

    public ArrayList<String> getFileNames()
    {
        return fileNames;
    }

    private void readFileNames() throws IOException
    {
        byte[] magic = {0x53, 0x52, 0x54, 0x53}; //SRTS marks block of name string entries
        long[] offsetArr = new long[1];
        long offset;
        int len, nro;
        ArrayList<String>fileNames;
        FileData.getMagicOffset(data, offsetArr, magic, this.startOffset, this.endOffset); //gets block offset
        offset = offsetArr[0]+4;
        len = FileData.readInt(data, 4, offset, this.endOffset); //length of entries block
        nro = FileData.readInt(data, 8, offset+4, this.endOffset); //number of entries
        fileNames = new ArrayList<String>(nro);
        //Gets all name entries in one string and splits them
        String bigStr = FileData.readString(data, len, offset+12, this.endOffset);
        String[] names = new String[nro];
        names = bigStr.split("\0", 0); //entries separated by null char
        Collections.addAll(fileNames, names);
        this.fileNum = nro;
        this.fileNames = fileNames;
    }

    /**
     * Reads the files within the PSP and orders them on a filesystem-like structure
     */
    private void readFileSystem()
    {

    }
}
