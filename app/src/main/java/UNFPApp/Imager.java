package UNFPApp;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import javax.imageio.ImageIO;

import UFPLib.FileData;
import UFPLib.PSI;
import UFPLib.PSI3;

public class Imager {
    public static BufferedImage makeImage(PSI psi) throws IOException
    {
        BufferedImage image = new BufferedImage(psi.getWidth(), psi.getHeigth(), BufferedImage.TYPE_INT_ARGB);
        render(psi, image);
        return image;
    }

    public static BufferedImage makeImage(PSI3 psi, BufferedImage spriteSheet, int index) throws IOException
    {
        int xOffset = psi.getSpritesCoordinates().get(index)[0]; //Horizontal offset
        int yOffset = psi.getSpritesCoordinates().get(index)[1]; //Vertical offset
        int width = psi.getImageSizes().get(index)[0];
        int height = psi.getImageSizes().get(index)[1];
        return render(spriteSheet, xOffset, yOffset, width, height);
    }

    private static void render(PSI psi, BufferedImage image) throws IOException
    {
        Graphics g = image.getGraphics();
        SeekableByteChannel data = psi.getData();
        double width = psi.getWidth();
        double height = psi.getHeigth();
        byte[] b = new byte[(int)(width*height)];
        long totalOffset = psi.getImageOffset()+PSI.imageHeaderSize;
        ByteBuffer bb = ByteBuffer.wrap(b);
        FileData.readBytes(data, bb, totalOffset, psi.getImageEndOffset());
        FileData.flipByteArr(b);
        int[][]colorIndex = psi.getColors();
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.Src);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) { //Renders horizontally
                int byteVal = (b[(int)((width*i)+j)] & 0xFF);
                int[] col = colorIndex[byteVal];
                int alpha = col[3]>0?(col[3]*2)-1:col[3];
                Color color = new Color(col[0], col[1], col[2], alpha);
                g.setColor(color);
                g.drawRect(j, i, j, i);
            }
        }
        g.dispose();
    }

    /* Copies the section of the spritesheet canvas that contains the sprite to the sprite canvas*/
    private static BufferedImage render(BufferedImage spriteSheet, int xOffset, int yOffset, int width, int height) throws IOException
    {
        //TODO:FIX
        BufferedImage spriteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        try{
            spriteImage.getGraphics().drawImage(spriteSheet, 0, 0, width, height, xOffset, yOffset, xOffset+width, yOffset+height, null);
        }catch (RasterFormatException e){
            System.out.println(e);
            spriteImage = ImageIO.read(new File("/mnt/7d219cef-3afb-4d3b-ac12-d0bd64996aab/HoiHoiSanRIPPED/scripts/UN-FP/app/src/main/resources/errImg.png"));
        }
        return spriteImage;
    }


    public static BufferedImage makePallete(PSI psi)
    {
        BufferedImage image = new BufferedImage(160, 160, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        int[][] colorIndex = psi.getColors();
        int i = 0;
        int j = 0;
        for (int[] color : colorIndex) {
            int alpha = color[3]>0?(color[3]*2)-1:color[3];
            Color col = new Color(color[0], color[1], color[2], alpha);
            g.setColor(col);
            g.fillRect(j*10, i*10, 10, 10);
            if(j>=15)
            {
                j= 0;
                i++;
            }else
            {
                j++;
            }
        }
        return image;
    }
}
