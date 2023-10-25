package UFPLib;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileHandler {
    public static SeekableByteChannel readFile(Path filename) throws IOException
    {
        try{
            SeekableByteChannel data =  Files.newByteChannel(filename, StandardOpenOption.READ);
            return data;
        }catch(IOException e)
        {
            throw e;
        }
    }
}