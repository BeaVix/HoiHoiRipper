package UNFPApp;

import java.io.IOException;
import java.util.ArrayList;

import UFPLib.Format;
import UFPLib.IFormat;
import UFPLib.UFP;

/**
 * GetSubFiles is a helper class for getting the files stored inside an archive format
 */
public class GetSubFiles {
    public static IFormat[] get(UFP archive) throws IOException
    {
        int fileNum = archive.getFileNum();
        ArrayList<String> names = archive.getFileNames();
        IFormat[] list = new IFormat[fileNum];
        for(int i = 0; i < fileNum; i++)
        {
            IFormat file = new Format(names.get(i), archive, archive.getFileStartOffsets()[i], archive.getFileEndOffsets()[i]);
            GetFormatService sv = new GetFormatService();
            list[i] = sv.scanFormat(file, archive);
        }
        return list;
    }
}
