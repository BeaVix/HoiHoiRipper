package UFPLib;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class UFP extends Format{
    private int headerEnd;
    private int fileNum; // Number of files
    private int extraNum; //Directories, names & extensions
    private long stringsOffset;
    private long stringsTableOffset;
    private long[] stringsOffsetsEntries;
    private long filesMetadataOffset;
    private long metadataTableOffset;
    private long[] metadataOffsetsEntries;
    private ArrayList<String> fileNames = new ArrayList<String>();
    private ArrayList<String> folderNames = new ArrayList<String>();
    private Folder fileSystem;
    private int[] fileSizes;
    private long[] fileStartOffsets; // Start offsets of each file
    private long[] fileEndOffsets; // End Ofssets of each file

    public UFP(){}

    public UFP(Path filename) throws IOException
    {
        super(filename);
        read();
    }

    public UFP(String name, IFormat file, long startOffset, long endOffset) throws IOException
    {
        super(name, file, startOffset, endOffset);
        read();
    }

    private void read() throws IOException
    {
        this.headerEnd = readHeaderSize();
        this.fileNum = readFileNum();
        this.extraNum = readExtraNum();
        this.stringsTableOffset = readStringsTableOffset();
        this.stringsOffsetsEntries = readStringsOffsetsEntries();
        this.stringsOffset = readStringsOffset();
        this.filesMetadataOffset = readFilesMetadataOffset();
        this.metadataTableOffset = readMetadataTableOffset();
        this.metadataOffsetsEntries = readMetadataOffsetsEntries();
        this.fileNames = readFileNames();
        this.folderNames = readFolderNames();
        this.fileSizes = readFileSizes();
        this.fileStartOffsets = readFileStartOffsets();
        this.fileEndOffsets = readFileEndOffsets();
        this.fileSystem = readFileSystem();
    }

    public int getHederEnd(){
        return this.headerEnd;
    }

    public int getFileNum(){
        return this.fileNum;
    }

    public int getExtraNum(){
        return this.extraNum;
    }

    public long getStringsTableOffset()
    {
        return this.stringsTableOffset;
    }

    public long[] getStringsOffsetsEntries()
    {
        return this.stringsOffsetsEntries;
    }

    public long getstringsOffset(){
        return this.stringsOffset;
    }

    public long getFilesMetadataOffset()
    {
        return this.filesMetadataOffset;
    }

    public long getMetadataTableOffset()
    {
        return this.metadataTableOffset;
    }

    public long[] getMetadataOffsetsEntries()
    {
        return this.metadataOffsetsEntries;
    }

    public ArrayList<String> getFileNames(){
        return this.fileNames;
    }

    public ArrayList<String> getFolderNames()
    {
        return this.folderNames;
    }

    public Folder getFileSystem()
    {
        return this.fileSystem;
    }

    public long[] getFileStartOffsets()
    {
        return fileStartOffsets;
    }

    public long[] getFileEndOffsets()
    {
        return fileEndOffsets;
    }

    public int[] getFileSizes()
    {
        return fileSizes;
    }

    public void setHeaderEnd(int s){
        this.headerEnd = s;
    
    }
    public void setFileNum(int s){
        this.fileNum = s;
    }

    public void setExtraNum(int s){
        this.extraNum = s;
    }

    public void setStringsTableOffset(long s)
    {
        this.stringsTableOffset = s;
    }

    public void setStringsOffsetsEntries(long[] s)
    {
        this.stringsOffsetsEntries = s;
    }

    public void setStringsOffset(long s){
        this.stringsOffset = s;
    }

    public void setFilesMetadataOffset(long s)
    {
        this.filesMetadataOffset = s;
    }

    public void setMetadataTableOffset(long s)
    {
        this.metadataTableOffset = s;
    }

    public void setMetadataOffsetsEntries(long[] s)
    {
        this.metadataOffsetsEntries = s;
    }

    public void setFileNames(ArrayList<String> s){
        for (String string : s) {
            this.fileNames.add(string);
        }
    }

    public void setFolderNames(ArrayList<String> s)
    {
        for (String string : s) {
            this.folderNames.add(string);
        }
    }

    public void setFileSystem(Folder s)
    {
        this.fileSystem = s;
    }

    public void setFileStartOffsets(long[] s)
    {
        fileStartOffsets = s;
    }

    public void setFileEndOffsets(long[] s)
    {
        fileEndOffsets = s;
    }

        public void setFileSizes(int[] s)
    {
        this.fileSizes = s;
    }

    private int readHeaderSize() throws IOException
    {
        int  i = FileData.readInt(data, 4, this.getStartOffset() + 8, this.getEndOffset());
        return i;
    }

    private int readFileNum() throws IOException
    {
        int  i = FileData.readInt(data, 4, this.getStartOffset() + 60, this.getEndOffset());
        return i;
    }

    private int readExtraNum() throws IOException
    {
        int  i = FileData.readInt(data, 4, this.getStartOffset() + 72, this.getEndOffset());
        return i;
    }

    private long readStringsTableOffset() throws IOException
    {
        byte[] b = {0x41, 0x41, 0x44, 0x50}; //AADP
        long[] arr = new long[1];
        FileData.getMagicOffset(data, arr, b, this.getStartOffset(),this.getEndOffset());
        return arr[0]+8;
    }

    private long[] readStringsOffsetsEntries() throws IOException
    {
        int stringNum = this.extraNum;
        long[] offsetEnties = new long[stringNum];
        for(int i = 0; i < stringNum; i++)
        {
            offsetEnties[i] = FileData.readInt(data, 4, this.stringsTableOffset+(4*i), this.endOffset);
        }
        return offsetEnties;
    }

    private long readStringsOffset() throws IOException
    {
        byte[] b = {0x31, 0x41, 0x44, 0x50}; //1ADP
        long[] arr = new long[1];
        FileData.getMagicOffset(data, arr, b, this.getStartOffset(),this.getEndOffset());
        return arr[0]+8;
    }

    private long readFilesMetadataOffset() throws IOException
    {
        byte[] b = {0x31, 0x41, 0x48, 0x46}; //1AHF
        long[] arr = new long[1];
        FileData.getMagicOffset(data, arr, b, this.getStartOffset(),this.getEndOffset());
        return arr[0]+8;
    }

    private long readMetadataTableOffset() throws IOException
    {
        byte[] b = {0x41, 0x41, 0x48, 0x46}; //AAHF
        long[] arr = new long[1];
        FileData.getMagicOffset(data, arr, b, this.startOffset, this.endOffset);
        return arr[0]+8;
    }

    private long[] readMetadataOffsetsEntries() throws IOException
    {
        int fileNum = this.fileNum;
        long[] offsetEnties = new long[fileNum];
        for(int i = 0; i < fileNum; i++)
        {
            offsetEnties[i] = FileData.readInt(data, 4, this.metadataTableOffset+(4*i), this.endOffset);
        }
        return offsetEnties;
    }

    private ArrayList<String> readFileNames() throws IOException
    {
        ArrayList<String> s = new ArrayList<String>();

        for (int i = 0; i < this.metadataOffsetsEntries.length; i++) {
            long entry = this.metadataOffsetsEntries[i];
            long position = entry + this.filesMetadataOffset;
            /*Reads the number of data fields, each 2 bytes long*/
            long nroFields = FileData.readInt(this.data, 1, position+13, this.endOffset);
            long fieldsPosition = position+14; //Position of the fields is offset 14 bytes from the start of the entry
            String strName;

            int nameEntryOffset = FileData.readInt(this.data, 2,  fieldsPosition+(nroFields-2)*2,this.endOffset); //second to last field: name
            long nameEntry = this.stringsOffset+this.stringsOffsetsEntries[nameEntryOffset];
            int nameLen = FileData.readInt(this.data, 1, nameEntry, this.metadataTableOffset);  //length of name
            strName = FileData.readString(this.data, nameLen, nameEntry+1, this.metadataTableOffset);

            int extEntryOffset = FileData.readInt(this.data, 2, fieldsPosition+(nroFields-1)*2, this.endOffset); //last field: extension
            long extEntry = this.stringsOffset+this.stringsOffsetsEntries[extEntryOffset];
            int extLen = FileData.readInt(this.data, 1, extEntry, this.metadataTableOffset);    //length of extension
            String strExt = FileData.readString(this.data, extLen, extEntry+1, this.metadataTableOffset);
            s.add(strName+strExt);
        }
        return s;
    }

    private ArrayList<String> readFolderNames() throws IOException
    {
        ArrayList<String> s = new ArrayList<String>();

        for (int i = 0; i < this.metadataOffsetsEntries.length; i++) {
            long entry = this.metadataOffsetsEntries[i];
            long position = entry + this.filesMetadataOffset;
            /*Reads the number of data fields, each 2 bytes long*/
            long nroFields = FileData.readInt(this.data, 1, position+13, this.endOffset);
            long fieldsPosition = position+14;
            
            for(int p = 0; p < nroFields-2; p++){
                int folderEntryOffset = FileData.readInt(this.data, 2, fieldsPosition+(p*2), this.endOffset);
                long folderEntry = this.stringsOffset+this.stringsOffsetsEntries[folderEntryOffset];
                int folderLen = FileData.readInt(this.data, 1, folderEntry, this.metadataTableOffset);  //length of folder name
                String strFolder = FileData.readString(this.data, folderLen, folderEntry+1, this.metadataTableOffset);

                if(!s.contains(strFolder))
                {
                    s.add(strFolder);
                }
            }
        }
        return s;
    }

    private Folder readFileSystem() throws IOException
    {
        Folder fileSystem = new Folder("");
        for (int i = 0; i < this.metadataOffsetsEntries.length; i++) {
            Format file = new Format(this.getFileNames().get(i), this, this.getFileStartOffsets()[i], this.getFileEndOffsets()[i]);
            Folder lastFolder = fileSystem; //if it isnt inside any folder just put it at the root of the fileSystem
            long entry = this.metadataOffsetsEntries[i];
            long position = entry + this.filesMetadataOffset;
            /*Reads the number of folder name fields, each 2 bytes long*/
            int nroFields = (FileData.readInt(this.data, 1, position+13, this.endOffset)) - 2;
            long fieldsPosition = position+14;
            int[] indexFolders = new int[nroFields];
            for(int p = 0; p < nroFields; p++){
                int folderEntryOffset = FileData.readInt(this.data, 2, fieldsPosition+(p*2), this.endOffset);
                long folderEntry = this.stringsOffset+this.stringsOffsetsEntries[folderEntryOffset];
                int folderLen = FileData.readInt(this.data, 1, folderEntry, this.metadataTableOffset);  //length of folder name
                String folderName = FileData.readString(this.data, folderLen, folderEntry+1, this.metadataTableOffset);
                Folder folder = new Folder(folderName);
                Folder rootFile = fileSystem;
                boolean skip = false;
                for (int w = 0; w < p; w++) {
                    rootFile = rootFile.getFolderChildren().get(indexFolders[w]);
                    if(p == nroFields-1){
                        rootFile.setEndOffset(rootFile.getEndOffset()+(file.getEndOffset()-file.getStartOffset())); //Adds the size of the file to each parent folder
                    }
                }
                for (Folder child : rootFile.getFolderChildren()) {
                    if(folderName.equals(child.getName())){
                        skip = true;
                        folder = child;
                        break;
                    }
                }
                if(!skip){
                    rootFile.addFolderChildren(folder);
                }
                lastFolder = folder;
                indexFolders[p] = rootFile.getFolderChildren().indexOf(folder);
            }
            lastFolder.addFileChildren(file);
        }
        return fileSystem;
    }
    
    private int[] readFileSizes() throws IOException
    {
        int[] a = new int[this.getFileNum()];
        for(int i = 0; i < a.length; i++)
        {
            a[i] = FileData.readInt(data, 4, this.getFilesMetadataOffset()+this.metadataOffsetsEntries[i]+4, getHederEnd());
        }
        return a;
    }

    private long[] readFileStartOffsets() throws IOException
    {
        long[] a = new long[this.getFileNum()];
        for(int i = 0; i < a.length; i++)
        {
            a[i] = FileData.readInt(data, 4, this.getFilesMetadataOffset()+this.metadataOffsetsEntries[i], endOffset);
        }
        return a;
    }

    private long[] readFileEndOffsets() throws IOException
    {
        long[] a = new long[this.fileNum];
        for(int i = 0; i < a.length; i++)
        {
            a[i] = getFileStartOffsets()[i] + getFileSizes()[i];
        }
        return a;
    }
}