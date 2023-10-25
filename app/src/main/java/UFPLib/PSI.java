package UFPLib;

import java.io.IOException;
import java.nio.file.Path;

public class PSI extends Format{
    public static int sizeOffset = 8;
    private int fileSize;
    private long colorOffset;
    private int[][] colors;
    private long imageOffset;
    private long imageEndOffset;
    private int imageSize;
    private int width;
    private int heigth;
    public static long imageHeaderSize = 12;

    public PSI(){}

    public PSI(Path filename) throws IOException {
        super(filename);
        read();
    }

    public PSI(String name, IFormat file, long startOffset, long endOffset) throws IOException 
    {
        super(name, file, startOffset, endOffset);
        read();
    }
    
    public int getFileSize()
    {
        return fileSize;
    }

    public long getColorOffset()
    {
        return colorOffset;
    }
    public int[][] getColors()
    {
        return colors;
    }
    public long getImageOffset()
    {
        return imageOffset;
    }

    public long getImageEndOffset()
    {
        return imageEndOffset;
    }

    public int getImageSize()
    {
        return imageSize;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeigth()
    {
        return heigth;
    }

    public void setFileSize(int a)
    {
        fileSize = a;
    }

    public void setColorOffset(long a)
    {
        colorOffset = a;
    }
    public void setColors(int[][] a)
    {
        colors = a;
    }
    public void setImageOffset(long a)
    {
        imageOffset = a;
    }

    public void setImageSize(int a)
    {
        imageSize = a;
    }

    public void setImageEndOffset(long a)
    {
        this.imageEndOffset = a;
    }

    public void setWidth(int a)
    {
        this.width = a;
    }

    public void setHeigth(int a)
    {
        this.heigth = a;
    }

    protected void read() throws IOException
    {
        this.fileSize = readFileSize();
        this.colorOffset = readColorOffset();
        this.colors = readColors();
        this.imageOffset = readImageOffset();
        this.imageSize = readImageSize();
        this.imageEndOffset = readImageEndOffset();
        this.width = readWidth();
        this.heigth = readHeigth();
    }

    private int readFileSize() throws IOException{
        int a = FileData.readInt(data, 4, this.getStartOffset() + 8);
        return a;
    }

    private long readColorOffset() throws IOException{
        byte[] b = {0x47, 0x41, 0x4D, 0x49}; // First instance of the byte sequence GAMI
        long[] arr = new long[1];
        long[] endArr = new long[1];
        FileData.getMagicOffset(data, arr, b, this.getStartOffset(), this.endOffset);
        int len = FileData.readInt(data, 4, arr[0]+4, this.endOffset); //Reads length of color pallete (minus header)
        FileData.getMagicOffset(data, endArr, b, arr[0]+4, this.endOffset); //Searchs second instance of GAMI
        return endArr[0]-len; //Returns start of color data
    }

    private int[][] readColors() throws IOException{
        int[][] a = new int[256][3];
        for (int i = 0; i < a.length; i++) { // Read every color index
            int[] indx = new int[4];
            for (int w = 0; w < 4; w++) { // Read the rgba values
                int c = FileData.readInt(data, 1, this.colorOffset+(i*4)+w, this.getEndOffset());
                indx[w] = c;
            }
            if(i%32 < 8 || i%32 > 23)
            {
                a[i] = indx;
            }else if(i%32 < 16)
            {
                a[i+8] = indx;
            }else{
                a[i-8] = indx;
            }
        }
        return a;
    }

    private long readImageOffset() throws IOException{
        byte[] b = {0x47, 0x41, 0x4D, 0x49}; //GAMI
        long[] arr = new long[1];
        FileData.getMagicOffset(data, arr, b, this.colorOffset, this.getEndOffset());
        return arr[0]+4;
    }

    private int readImageSize() throws IOException
    {
        return FileData.readInt(data, 4, this.getImageOffset(), this.getEndOffset());
    }

    private long readImageEndOffset() throws IOException
    {
        return this.imageOffset+12 + this.imageSize;
    }

    private int readWidth() throws IOException
    {
        return FileData.readInt(data, 2, this.getImageOffset()+8, this.getImageEndOffset());
    }

    private int readHeigth() throws IOException
    {
        return FileData.readInt(data, 2, this.getImageOffset()+10, this.getImageEndOffset());
    }
}
