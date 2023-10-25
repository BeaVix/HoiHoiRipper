package UFPLib;
/**
 * FileData class has all the methods used for reading binary data
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

public class FileData{
    private static Charset defaultCharset = Charset.availableCharsets().get("windows-31j"); // Shows japanese characters

    /**
     * Reads the raw bytes from a given SeekableByteChannel and stores them to a given ByteBuffer.
     * The size of the ByteBuffer determines the amount of bytes read.
     * @param data the SeekableByteChannel to read bytes from
     * @param bb ByteBuffer used to store the read bytes
     * @return The number of bytes read
     * @throws IOException
     */
    public static int readBytes(SeekableByteChannel data, ByteBuffer bb) throws IOException
    {
        int read = data.read(bb);
        orderBB(bb, read);
        return read;
    }

    public static int readBytes(SeekableByteChannel data, ByteBuffer bb, long startOffset) throws IOException
    {
        int read = data.position(startOffset).read(bb);
        orderBB(bb, read);
        return read;
    }

    public static int readBytes(SeekableByteChannel data, ByteBuffer bb, long startOffset, long EndOffset) throws IOException
    {
        int cap = bb.capacity();
        int read;
        if((startOffset + cap) > EndOffset)
        {
            bb.limit(cap - (int)((startOffset + cap)-EndOffset));
            read = data.position(startOffset).read(bb);
            bb.limit(cap);
        }else
        {
            read = data.position(startOffset).read(bb);
        }
            orderBB(bb, read);
            return read;
    }

    public static String readString(SeekableByteChannel data, int bytes, long pos, long end) throws IOException
    {
        data.position(pos);
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb, pos, end);
        flipByteArr(b);
        return new String(b, defaultCharset);
    }


    public static String readString(SeekableByteChannel data, int bytes, long pos) throws IOException
    {
        data.position(pos);
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb, pos);
        flipByteArr(b);
        return new String(b, defaultCharset);
    }

    public static String readString(SeekableByteChannel data, int bytes) throws IOException
    {
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb);
        flipByteArr(b);
        return new String(b, defaultCharset);
    }

    public static int readInt(SeekableByteChannel data, int bytes) throws IOException
    {
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb);
        return bytesToInt(b);
    }

    public static int readInt(SeekableByteChannel data, int bytes, long pos) throws IOException
    {
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb, pos);
        return bytesToInt(b);
    }

        public static int readInt(SeekableByteChannel data, int bytes, long pos, long end) throws IOException
    {
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb, pos, end);
        return bytesToInt(b);
    }


    /**
     * Searches for a sequence of bytes on a SeekableByteChannel and saves them to an array.
     * The length of the array determines the amount of matches it will search for.
     * @param data The SeekableByteChannel to search on
     * @param arr The array where the matches's offsets will be stored
     * @param magic The byte sequence to search for
     * @return Number of matches or -1 if no matches where found
     * @throws IOException
     */
    //Rabin-Karp algorithm
    public static int getMagicOffset(SeekableByteChannel data, long[] arr, byte[] magic) throws IOException 
    {
        int matches = 0;

        int m = magic.length;
        long n = data.size();

        byte[] bOld = new byte[m];
        ByteBuffer bb = ByteBuffer.wrap(bOld);
        readBytes(data, bb);
        
        long hashS = Hasher.byteHash(bOld);
        long hashP = Hasher.byteHash(magic);
        long pow = (long)Math.pow(255, m-1);
        for(long i = data.position(); i < (n - m); i = data.position())
        {
            if(hashS == hashP)
            {
                if(Arrays.equals(magic, bOld))
                {
                    arr[matches] = (i-m);
                    if(matches == arr.length)
                    {
                        return matches;
                    }

                }
            }
            if(i < n-m)
            {
                byte[] bN = new byte[1];
                ByteBuffer bbN = ByteBuffer.wrap(bN);
                readBytes(data, bbN);
                byte newByte = bN[0];
                hashS = Hasher.byteReHash(bOld, hashS, newByte, pow);
                System.arraycopy(bOld, 1, bOld, 0, m-1);
                bOld[m-1] = newByte;
            }
        }
        return -1;
    }

    public static int getMagicOffset(SeekableByteChannel data, long[] arr,byte[] magic, long startOff, long endOff) throws IOException 
    {
        int matches = 0;

        int m = magic.length;
        long n = endOff;

        byte[] bOld = new byte[m];
        ByteBuffer bb = ByteBuffer.wrap(bOld);
        readBytes(data, bb, startOff, endOff);
        
        long hashS = Hasher.byteHash(bOld);
        long hashP = Hasher.byteHash(magic);
        long pow = (long)Math.pow(255, m-1);
        for(long i = startOff; i < (n - m); i = data.position())
        {
            if(hashS == hashP)
            {
                if(Arrays.equals(magic, bOld))
                {
                    arr[matches] = (i-m);
                    matches++;
                    if(matches == arr.length)
                    {
                        return matches;
                    }
                }
            }
            if(i < n-m)
            {
                byte[] bN = new byte[1];
                ByteBuffer bbN = ByteBuffer.wrap(bN);
                readBytes(data, bbN, i, endOff);
                byte newByte = bN[0];
                hashS = Hasher.byteReHash(bOld, hashS, newByte, pow);
                System.arraycopy(bOld, 1, bOld, 0, m-1);
                bOld[m-1] = newByte;
            }
        }
        return -1;
    }

    public static boolean compareNextBytes(SeekableByteChannel data, int bytes, byte[] compare) throws IOException
    {
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb);
        flipByteArr(b);
        return Arrays.equals(compare, b);
    }

    public static boolean compareNextBytes(SeekableByteChannel data, int bytes, byte[] compare, long pos, long end) throws IOException
    {
        byte[] b= new byte[bytes];
        ByteBuffer bb = ByteBuffer.wrap(b);
        readBytes(data, bb, pos, end);
        flipByteArr(b);
        return Arrays.equals(compare, b);
    }

    public static int bytesToInt(byte[] bytes)
    {
        int value = 0;
        for(int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            value = (value << 8) + (b & 0xFF);
        }
        return value;
    }

    public static void orderBB(ByteBuffer bb, int newBytes)
    {
        int start = bb.position()-newBytes;
        byte[] newB = new byte[newBytes];
        bb.position(0);
        bb.get(newB, start, newBytes);
        flipByteArr(newB);
        bb.put(start, newB);
    }

    /**
     * Flips the order of the bytes stored on a byte array
     * @param arr The array to flip
     */
    public static void flipByteArr(byte[] arr)
    {
        byte[] arrCp = arr.clone();
        for (int i = arr.length; i > 0; i--) {
            arr[i-1] = arrCp[arr.length-i];
        }
    }
}