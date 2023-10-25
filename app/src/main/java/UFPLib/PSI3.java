package UFPLib;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class PSI3 extends PSI{
    private int fileNum;
    private long metadataOffset;
    private int metadataSize;
    private ArrayList<String> fileNames = new ArrayList<String>();
    private ArrayList<Integer[]> imageSizes = new ArrayList<Integer[]>();
    private ArrayList<Integer[]> spritesCoordinates = new ArrayList<Integer[]>();
    private int metadataEntryLen;

    public PSI3(){}

    public PSI3(Path filename) throws IOException
    {
        super(filename);
        read();
    }

    public PSI3(String name, IFormat file, long startOffset, long endOffset) throws IOException
    {
        super(name, file, startOffset, endOffset);
        read();
    }

    @Override
    protected void read() throws IOException
    {
        super.read();
        setMetadataEntryLen(40);
        this.fileNum = readFileNum();
        this.metadataOffset = readMetadataOffset();
        this.metadataSize = readMetadataSize();
        this.fileNames = readFileNames();
        this.imageSizes = readImageSizes();
        this.spritesCoordinates = readSpritesCoordinates();
    }

    public int getFileNum()
    {
        return fileNum;
    }

    public long getMetadataOffset()
    {
        return metadataOffset;
    }

    public int getMetadataSize()
    {
        return metadataSize;
    }

    public ArrayList<String> getFileNames()
    {
        return fileNames;
    }
    public ArrayList<Integer[]> getImageSizes()
    {
        return imageSizes;
    }

    public ArrayList<Integer[]> getSpritesCoordinates()
    {
        return spritesCoordinates;
    }

    public void setFileNum(int a)
    {
        fileNum = a;
    }

    public void setMetadataOffset(long a)
    {
        metadataOffset = a;
    }

    public void setMetadataSize(int a)
    {
        metadataSize = a;
    }

    private void setMetadataEntryLen(int a)
    {
        metadataEntryLen = a;
    }

    public void setFileNames(ArrayList<String> a)
    {
        for (String string : a) {
            fileNames.add(string);
        }
    }
    public void setImageSizes(ArrayList<Integer[]> a)
    {
        for (Integer[] string : a) {
            imageSizes.add(string);
        }
    }

    public void setSpritesCoordinates(ArrayList<Integer[]> a)
    {
        spritesCoordinates = a;
    }

    public int readFileNum() throws IOException{
        int a = FileData.readInt(data, 8, this.startOffset + 40, this.endOffset);
        return a;
    }

    public long readMetadataOffset() throws IOException{
        byte[] b = {0x4F, 0x46, 0x4E, 0x49}; //OFNI
        long[] arr = new long[1];
        FileData.getMagicOffset(data, arr,b, this.startOffset, this.endOffset);
        return arr[0]+16;
    }

    public int readMetadataSize() throws IOException{
        return 40*fileNum;
    }

    public ArrayList<String> readFileNames() throws IOException
    {
        long[] arr = new long[1];
        byte[] b = {0x47, 0x41, 0x4D, 0x49}; //GAMI
        FileData.getMagicOffset(this.data, arr, b, this.metadataOffset, this.endOffset);
        long metadataEnd = arr[0];
        ArrayList<String> s = new ArrayList<String>();
        int oldOffset = 0;
        int oldLen = 0;
        for (long i = 0; i < metadataSize; i += metadataEntryLen) {
            if(metadataOffset+i >= metadataEnd || i/metadataEntryLen == fileNum)
            {
                break;
            }
            int nameOffset = FileData.readInt(data, 4, metadataOffset+i, metadataEnd);
            if(oldOffset != 0 && nameOffset != oldOffset+oldLen+1)
            {
                setMetadataEntryLen(32);
                i -= 8;
                nameOffset = FileData.readInt(data, 4, metadataOffset+i, metadataEnd);
            }
            int nameLen = FileData.readInt(data, 4);
            String name = FileData.readString(data, nameLen, startOffset+nameOffset, metadataEnd);
            s.add(name);
            oldOffset = nameOffset;
            oldLen = nameLen;
        }
        return s;
    }

    public ArrayList<Integer[]> readImageSizes() throws IOException
    {
        ArrayList<Integer[]> s = new ArrayList<Integer[]>();
        for (long i = 16; i < metadataSize; i += metadataEntryLen) {
            if(i/metadataEntryLen == fileNum)
            {
                break;
            }
            int width = FileData.readInt(data, 4, metadataOffset+i, this.endOffset);

            int heigth = FileData.readInt(data, 4, metadataOffset+i+4, this.endOffset);
            Integer[] size = {width, heigth};
            s.add(size);
        }
        return s;
    }

    private ArrayList<Integer[]> readSpritesCoordinates() throws IOException
    {
        ArrayList<Integer[]> s = new ArrayList<Integer[]>();
        for (long i = 8; i < metadataSize; i += metadataEntryLen) {
            if(i/metadataEntryLen == fileNum)
            {
                break;
            }
            int x = FileData.readInt(data, 4, metadataOffset+i, this.getImageEndOffset());
            int y = FileData.readInt(data, 4, metadataOffset+i+4, this.getImageEndOffset());
            Integer[] coordinates = {x, y};
            s.add(coordinates);
        }
        return s;
    }
}
