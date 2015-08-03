/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.file.image;

import java.io.File;
import java.io.IOException;

/**
 * @author maqiang
 */
public class ImageProcessorTest
{
    public static void main(String[] args)
    {
        try
        {
            ImageProcessor test=new ImageProcessor(new File("/Users/maqiang/Pictures/桌面/1350635941791.jpg"));
            File file=test.setOutputFormat(ImageProcessor.ImageOutputFormat.JPG)
                    .setOutputName("test")
                    .setHandleMode(ImageProcessor.ImageHandleMode.COMPRESS_AND_SCALE)
                    .setCompressMode(ImageProcessor.ImageCompressMode.CUSTEM)
                    .setOutputQuality(1)
                    .setScaleMode(ImageProcessor.ImageScaleMode.CROP)
                    .toImageFile();
            file.createNewFile();
        }
        catch(IOException ex)
        {
            System.out.println(ex.toString());
        }
    }
}
