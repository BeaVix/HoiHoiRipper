package UNFPApp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import UFPLib.FileData;
import UFPLib.IFormat;

public class Extractor {
    public static void extractFileRaw(IFormat file, Path destination) throws IOException
    {   
        File out = new File(destination.toString());
        if(!out.exists()){
            out.createNewFile();
            out.setWritable(true);
            FileOutputStream writer = new FileOutputStream(out, false);
            byte[] b = new byte[(int)(file.getEndOffset()-file.getStartOffset())];
            ByteBuffer bb = ByteBuffer.wrap(b);
            FileData.readBytes(file.getData(), bb, file.getStartOffset(), file.getEndOffset());
            FileData.flipByteArr(b); //Flips byte order to original
            writer.write(b);
            writer.close();
        }else{
            throw new FileAlreadyExistsException(destination.toString());
        }
    }

    public static void extractFileRaw(IFormat[] files, Path destination) throws IOException{
        for (IFormat file : files) {
            extractFileRaw(file, destination);
        }
    }

    public static void extractImage(BufferedImage img, Path destination)
    {
        if(destination.toFile().exists())
        {
            destination.toFile().delete();
        }
        try{
            File  file = new File(destination.toString());
            ImageIO.write(img, "png", file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
