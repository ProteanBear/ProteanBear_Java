package pb.file.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

/**
 * 本Java类是用于对图片的相关压缩、缩放、剪裁等处理。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2014/10/08
 */
public class ImageProcessor
{
    /*--------------------------开始：静态内容---------------------------*/

    /**
     * 静态域(公共)<br>
     * 名称:    ImageHandleMode<br>
     * 描述:    枚举类型-处理类型<br>
     */
    public static enum ImageHandleMode
    {
        COMPRESS,COMPRESS_AND_SCALE,SCALE
    }

    /**
     * 静态域(公共)<br>
     * 名称:    ImageCompressMode<br>
     * 描述:    枚举类型-压缩算法<br>
     */
    public static enum ImageCompressMode
    {
        TRANSFORM,CUSTEM
    }

    /**
     * 静态域(公共)<br>
     * 名称:    ImageOutputFormat<br>
     * 描述:    枚举类型-输出格式<br>
     */
    public static enum ImageOutputFormat
    {
        GIF
                {
                    @Override
                    public String getFormat()
                    {
                        return "gif";
                    }
                },
        JPG
                {
                    @Override
                    public String getFormat()
                    {
                        return "jpg";
                    }
                },
        PNG
                {
                    @Override
                    public String getFormat()
                    {
                        return "png";
                    }
                };

        public abstract String getFormat();
    }

    /**
     * 静态域(公共)<br>
     * 名称:    ImageScaleFormat<br>
     * 描述:    枚举类型-缩放模式<br>
     */
    public static enum ImageScaleMode
    {
        PROPORTION,CROP,CUSTEM
    }

    /**
     * 静态域(公共)<br>
     * 名称:    ImageScaleFormat<br>
     * 描述:    枚举类型-剪切模式<br>
     */
    public static enum ImageCropMode
    {
        TOPLEFT,CENTER
    }
    /*--------------------------结束：静态内容---------------------------*/

    /**
     * 域(私有)<br>
     * 名称:    file<br>
     * 描述:    记录图片文件的对象<br>
     */
    private File file;

    /**
     * 域(私有)<br>
     * 名称:    outputPath<br>
     * 描述:    记录图片文件的输出路径<br>
     */
    private String outputPath;

    /**
     * 域(私有)<br>
     * 名称:    outputName<br>
     * 描述:    记录图片文件的输出名称<br>
     */
    private String outputName;

    /**
     * 域(私有)<br>
     * 名称:    outputWidth<br>
     * 描述:    记录图片文件输出的宽度，默认为400<br>
     */
    private int outputWidth;

    /**
     * 域(私有)<br>
     * 名称:    outputHeight，默认为300<br>
     * 描述:    记录图片文件输出的高度<br>
     */
    private int outputHeight;

    /**
     * 域(私有)<br>
     * 名称:    outputQuality<br>
     * 描述:    记录图片文件输出的质量<br>
     */
    private float outputQuality;

    /**
     * 域(私有)<br>
     * 名称:    scaleWidth<br>
     * 描述:    记录图片文件输出的比例-宽<br>
     */
    private int scaleWidth;

    /**
     * 域(私有)<br>
     * 名称:    scaleHeight<br>
     * 描述:    记录图片文件输出的比例-高<br>
     */
    private int scaleHeight;

    /**
     * 域(私有)<br>
     * 名称:    handleMode<br>
     * 描述:    记录当前的图片处理模式<br>
     */
    private ImageHandleMode handleMode;

    /**
     * 域(私有)<br>
     * 名称:    compressMode<br>
     * 描述:    记录当前的图片压缩模式<br>
     */
    private ImageCompressMode compressMode;

    /**
     * 域(私有)<br>
     * 名称:    outputFormat<br>
     * 描述:    记录当前的图片输出模式<br>
     */
    private ImageOutputFormat outputFormat;

    /**
     * 域(私有)<br>
     * 名称:    scaleMode<br>
     * 描述:    记录图片缩放输出的模式<br>
     */
    private ImageScaleMode scaleMode;

    /**
     * 域(私有)<br>
     * 名称:    cropMode<br>
     * 描述:    记录图片剪切处理的模式<br>
     */
    private ImageCropMode cropMode;

    /**
     * 构造函数（无参数）<br>
     * 描述：   初始化私有域
     */
    public ImageProcessor()
    {
        this.file=null;
        this.outputPath=null;
        this.outputName=null;
        this.outputWidth=640;
        this.outputHeight=360;
        this.scaleWidth=16;
        this.scaleHeight=9;
        this.outputQuality=0.6f;
        this.handleMode=ImageHandleMode.COMPRESS_AND_SCALE;
        this.compressMode=ImageCompressMode.CUSTEM;
        this.outputFormat=ImageOutputFormat.JPG;
        this.scaleMode=ImageScaleMode.PROPORTION;
        this.cropMode=ImageCropMode.CENTER;
    }

    /**
     * 构造函数（一个参数）<br>
     * 描述：   初始化私有域
     *
     * @param file - 图片文件对象
     */
    public ImageProcessor(File file)
    {
        this();
        this.setFile(file);
    }

    /**
     * 构造函数（两个参数）<br>
     * 描述：   初始化私有域
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     */
    public ImageProcessor(String filePath,String fileName)
    {
        this();
        this.setFile(filePath,fileName);
    }

    /**
     * 方法（公共）<br>
     * 名称:    setFile<br>
     * 描述:    设置文件对象<br>
     *
     * @param file - 文件对象
     * @return ImageProcessor - 当前对象
     */
    public final ImageProcessor setFile(File file)
    {
        this.file=file;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setFile<br>
     * 描述:    设置文件路径和名称<br>
     *
     * @param filePath - 文件路径
     * @param fileName - 文件名称
     * @return ImageProcessor - 当前对象
     */
    public final ImageProcessor setFile(String filePath,String fileName)
    {
        this.file=new File(filePath,fileName);
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getOutputPath<br>
     * 描述:    获取输出路径<br>
     *
     * @return String - 输出路径
     */
    public String getOutputPath()
    {
        return outputPath;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOutputPath<br>
     * 描述:    设置输出文件路径<br>
     *
     * @param outputPath -  输出路径
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setOutputPath(String outputPath)
    {
        this.outputPath=outputPath;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getOutputName<br>
     * 描述:    获取输出文件名称<br>
     *
     * @return String - 输出路径
     */
    public String getOutputName()
    {
        return outputName;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOutputName<br>
     * 描述:    设置输出文件名称<br>
     *
     * @param outputName 输出文件名
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setOutputName(String outputName)
    {
        this.outputName=outputName;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getOutputWidth<br>
     * 描述:    获取输出图片宽度<br>
     *
     * @return int - 输出宽度
     */
    public int getOutputWidth()
    {
        return outputWidth;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOutputSize<br>
     * 描述:    设置输出图片尺寸<br>
     *
     * @param outputWidth  输出宽度
     * @param outputHeight 输出高度
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setOutputSize(int outputWidth,int outputHeight)
    {
        this.outputWidth=outputWidth;
        this.outputHeight=outputHeight;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOutputQuality<br>
     * 描述:    设置输出图片质量<br>
     *
     * @param outputQuality
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setOutputQuality(float outputQuality)
    {
        this.outputQuality=outputQuality;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getOutputHeight<br>
     * 描述:    获取输出图片高度<br>
     *
     * @return int - 输出高度
     */
    public int getOutputHeight()
    {
        return outputHeight;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getScaleWidth<br>
     * 描述:    获取输出图片是否等比例-宽度<br>
     *
     * @return int - 输出比例宽度
     */
    public int getScaleWidth()
    {
        return scaleWidth;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setScaleWidth<br>
     * 描述:    获取输出图片是否等比例-宽度<br>
     *
     * @param scaleWidth  裁剪后宽度
     * @param scaleHeight 裁剪后高度
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setScale(int scaleWidth,int scaleHeight)
    {
        this.scaleWidth=scaleWidth;
        this.scaleHeight=scaleHeight;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setHandleMode<br>
     * 描述:    设置处理模式<br>
     *
     * @param handleMode 处理模式
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setHandleMode(ImageHandleMode handleMode)
    {
        this.handleMode=handleMode;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setCompressMode<br>
     * 描述:    设置压缩模式<br>
     *
     * @param compressMode 压缩模式
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setCompressMode(ImageCompressMode compressMode)
    {
        this.compressMode=compressMode;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getOutputFormat<br>
     * 描述:    获取输出格式<br>
     *
     * @return String - 输出格式
     */
    public String getOutputFormat()
    {
        return this.outputFormat.getFormat();
    }

    /**
     * 方法（公共）<br>
     * 名称:    setOutputFormat<br>
     * 描述:    设置输出格式<br>
     *
     * @param outputFormat 输出格式
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setOutputFormat(ImageOutputFormat outputFormat)
    {
        this.outputFormat=outputFormat;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    setScaleMode<br>
     * 描述:    设置裁剪模式<br>
     *
     * @param scaleMode 裁剪模式
     * @return ImageProcessor - 当前对象
     */
    public ImageProcessor setScaleMode(ImageScaleMode scaleMode)
    {
        this.scaleMode=scaleMode;
        return this;
    }

    /**
     * 方法（公共）<br>
     * 名称:    getScaleHeight<br>
     * 描述:    获取输出图片是否等比例-高度<br>
     *
     * @return int - 输出比例高度
     */
    public int getScaleHeight()
    {
        return scaleHeight;
    }

    /**
     * 方法（公共）<br>
     * 名称:    toImage<br>
     * 描述:    生成图片对象<br>
     *
     * @return Image - 图片对象
     * @throws java.io.IOException
     */
    public BufferedImage toImage() throws IOException
    {
        //处理图片
        byte[] bytes=this.toImageBytes();
        //输入流
        ByteArrayInputStream in=new ByteArrayInputStream(bytes);
        return ImageIO.read(in);
    }

    /**
     * 方法（公共）<br>
     * 名称:    toImageFile<br>
     * 描述:    生成图片文件对象<br>
     *
     * @return Image - 图片对象
     * @throws java.io.IOException
     */
    public File toImageFile() throws IOException
    {
        //处理图片
        byte[] bytes=this.toImageBytes();

        //生成条件判断
        String path=this.file.getAbsolutePath();
        path=path.substring(0,path.indexOf(this.file.getName()));
        this.outputPath=this.outputPath==null || "".equals(this.outputPath.trim())?
                path:this.outputPath;
        if(this.outputPath==null || "".equals(this.outputPath.trim()))
        {
            throw new IOException("未设置文件输出路径！");
        }
        this.outputName=this.outputName==null || "".equals(this.outputName.trim())?
                this.file.getName():this.outputName;
        if(this.outputName==null || "".equals(this.outputName.trim()))
        {
            throw new IOException("未设置文件输出名称！");
        }

        File result=new File(this.outputPath,this.outputName+"."+this.outputFormat.getFormat());
        FileOutputStream fos=new FileOutputStream(result);
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        bos.write(bytes);
        fos.close();
        bos.close();

        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    toImageBytes<br>
     * 描述:    生成图片文件字节数组<br>
     *
     * @return byte[] - 图片字节数组
     * @throws java.io.IOException
     */
    public byte[] toImageBytes() throws IOException
    {
        //载入图片文件
        BufferedImage image=this.loadImageFile();
        ByteArrayOutputStream out=new ByteArrayOutputStream();

        //剪切图片
        if((this.handleMode!=ImageHandleMode.COMPRESS))
        {
            out=this.scaleImage(out,image);
            //System.out.println("toImageBytes:"+out.toByteArray().length);
            ByteArrayInputStream in=new ByteArrayInputStream(out.toByteArray());
            image=ImageIO.read(in);
        }

        //压缩图片
        byte[] result=null;
        if(this.handleMode!=ImageHandleMode.SCALE)
        {
            result=this.compressImageToBytes(out,image);
        }
        else
        {
            ImageIO.write(image,this.outputFormat.getFormat(),out);
            //System.out.println("toImageBytes"+out.toByteArray().length);
            result=out.toByteArray();
        }

        if(result==null)
        {
            throw new IOException("处理图片时出现错误！");
        }

        out.close();
        return result;
    }

    /**
     * 方法（公共）<br>
     * 名称:    loadImageFile<br>
     * 描述:    载入图片数据<br>
     *
     * @return byte[] - 图片字节数组
     * @throws java.io.IOException
     */
    private BufferedImage loadImageFile() throws IOException
    {
        //非空判断
        if(this.file==null)
        {
            throw new IOException("输入文件对象为空！");
        }
        if(!this.file.exists())
        {
            throw new IOException("指定的图片文件不存在！");
        }

        //读取图片并判断文件格式
        BufferedImage image=ImageIO.read(this.file);
        if(image==null&&image.getWidth(null)==-1)
        {
            throw new IOException("无法识别的图片文件格式！");
        }

        return image;
    }

    /**
     * 方法（私有）<br>
     * 名称:    compressImageToBytes<br>
     * 描述:    压缩图片<br>
     *
     * @return byte[] - 图片字节数组
     * @throws java.io.IOException
     */
    private byte[] compressImageToBytes(ByteArrayOutputStream out,BufferedImage image) throws IOException
    {
        //根据生成的图片格式进行压缩处理
        //gif:只能使用转换方式压缩
        //转换方式：用Format对应格式中ImageIO默认参数把IMAGE打包成BYTE[]
        out=(this.outputFormat==ImageOutputFormat.GIF)?this.compressImageForTransform(out,image):out;
        //jpg：可以使用自定义和编码器方式压缩
        if(this.outputFormat==ImageOutputFormat.JPG)
        {
            //转换方式：用Format对应格式中ImageIO默认参数把IMAGE打包成BYTE[]
            out=(this.compressMode==ImageCompressMode.TRANSFORM)?this.compressImageForTransform(out,image):out;
            //自定义：自己设置压缩质量来把图片压缩成byte[]
            out=(this.compressMode==ImageCompressMode.CUSTEM)?this.compressImageForCustem(out,image):out;
            //JPEGCodec：通过 com.sun.image.codec.jpeg.JPEGCodec提供的编码器来实现图像压缩
            //out=(this.compressMode==ImageCompressMode.JPEGCODEC)?this.compressImageForEncoder(out, image):out;
        }
        //png：
        if(this.outputFormat==ImageOutputFormat.PNG)
        {
        }

        if(out.toByteArray().length==0)
        {
            throw new IOException("压缩时出现错误或压缩方式错误！");
        }

        return out.toByteArray();
    }

    /**
     * 方法（私有）<br>
     * 名称:    compressImageForTransform<br>
     * 描述:    使用转换方式压缩图片<br>
     *
     * @return ByteArrayOutputStream - 字节输出流
     * @throws java.io.IOException
     */
    private ByteArrayOutputStream compressImageForTransform(ByteArrayOutputStream out,BufferedImage image)
            throws IOException
    {
        out.reset();
        ImageIO.write(image,this.outputFormat.getFormat(),out);
        //System.out.println("compressImageForTransform:"+out.toByteArray().length);
        return out;
    }

    /**
     * 方法（私有）<br>
     * 名称:    compressImageForCustem<br>
     * 描述:    使用自定义方式压缩图片<br>
     *
     * @return ByteArrayOutputStream - 字节输出流
     * @throws java.io.IOException
     */
    private ByteArrayOutputStream compressImageForCustem(ByteArrayOutputStream out,BufferedImage image)
            throws IOException
    {
        // 得到指定Format图片的writer
        Iterator<ImageWriter> iter=ImageIO
                .getImageWritersByFormatName("jpeg");// 得到迭代器
        ImageWriter writer=(ImageWriter)iter.next(); // 得到writer

        // 得到指定writer的输出参数设置(ImageWriteParam )
        ImageWriteParam iwp=writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
        iwp.setCompressionQuality(this.outputQuality); // 设置压缩质量参数
        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

        ColorModel colorModel=ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        iwp.setDestinationType(
                new javax.imageio.ImageTypeSpecifier(
                        colorModel,
                        colorModel.createCompatibleSampleModel(16,16)
                )
        );

        IIOImage iIamge=new IIOImage(image,null,null);
        // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
        // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
        writer.setOutput(ImageIO.createImageOutputStream(out));
        writer.write(null,iIamge,iwp);

        return out;
    }

    /**
     * 方法（私有）<br>
     * 名称:    compressImageForEncoder<br>
     * 描述:    使用JPEGEncoder方式压缩图片<br>
     *
     * @return BufferedImage - 图片字节数组
     * @throws java.io.IOException private ByteArrayOutputStream compressImageForEncoder(ByteArrayOutputStream out,BufferedImage image)
     *                             throws IOException
     *                             {
     *                             // 设置压缩参数
     *                             JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(image);
     *                             param.setQuality(this.outputQuality, false);
     *                             // 设置编码器
     *                             JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out,param);
     *                             encoder.encode(image);
     *                             <p>
     *                             return out;
     *                             }
     *                             <p>
     *                             /**方法（私有）<br>
     *                             名称:    scaleImage<br>
     *                             描述:    缩放图片<br>
     * @throws java.io.IOException
     */
    private ByteArrayOutputStream scaleImage
    (ByteArrayOutputStream out,BufferedImage image)
            throws IOException
    {
        int width=0, height=0;
        boolean isWidth=true;

        //图片小于指定的输出大小
        if(this.scaleMode==ImageScaleMode.PROPORTION
                &&image.getWidth()<outputWidth
                &&image.getHeight()<outputHeight)
        {
            width=image.getWidth();
            height=image.getHeight();
        }
        //根据指定的缩放方式进行图片处理
        //等比例缩放方式（按图片比例缩小）
        else
        {
            if(this.scaleMode==ImageScaleMode.PROPORTION
                    || this.scaleMode==ImageScaleMode.CROP)
            {
                //为等比缩放计算输出的图片宽度及高度
                double rateWidth=((double)image.getWidth())/(double)outputWidth;
                double rateHeight=((double)image.getHeight())/(double)outputHeight;
                //找到适合缩放的边
                isWidth=(rateWidth<rateHeight);
                double rate=isWidth?rateWidth:rateHeight;
                width=(int)(isWidth?this.outputWidth:image.getWidth()/rate);
                height=(int)(isWidth?image.getHeight()/rate:this.outputHeight);
            }
        }

        //自定义方式（生成指定比例）
        if(this.scaleMode==ImageScaleMode.CUSTEM)
        {
            width=this.outputWidth;
            height=this.outputHeight;
        }

        //缩放图片
        out=this.scaleImageBySize(out,image,width,height);

        //对缩放后的图片进行裁剪
        if(this.scaleMode==ImageScaleMode.CROP)
        {
            //计算裁剪位置
            int x=isWidth?0:(width-this.outputWidth)/2;
            int y=isWidth?(height-this.outputHeight)/2:0;
            //再根据指定尺寸进行裁剪
            out=this.cropImage(out,x,y,this.outputWidth,this.outputHeight);
        }

        return out;
    }

    /**
     * 方法（私有）<br>
     * 名称:    scaleImageBySize<br>
     * 描述:    根据指定的尺寸缩放图片<br>
     *
     * @return BufferedImage - 图片字节数组
     * @throws java.io.IOException
     */
    private ByteArrayOutputStream scaleImageBySize
    (ByteArrayOutputStream out,BufferedImage image,int width,int height)
            throws IOException
    {
        ImageIcon ii=new ImageIcon(image);
        Image i=ii.getImage();
        Image resizedImage=null;
        resizedImage=i.getScaledInstance(width,height,Image.SCALE_SMOOTH);

        // This code ensures that all the pixels in the image are loaded.
        Image temp=new ImageIcon(resizedImage).getImage();

        // Create the buffered image.
        BufferedImage bufferedImage=new BufferedImage(
                temp.getWidth(null),
                temp.getHeight(null),BufferedImage.TYPE_INT_RGB
        );

        // Copy image to buffered image.
        Graphics g=bufferedImage.createGraphics();

        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0,0,temp.getWidth(null),temp.getHeight(null));
        g.drawImage(temp,0,0,null);
        g.dispose();

        // Soften.
        float softenFactor=0.05f;
        float[] softenArray={0,softenFactor,0,softenFactor,
                             1-(softenFactor*4),softenFactor,0,softenFactor,0};
        Kernel kernel=new Kernel(3,3,softenArray);
        ConvolveOp cOp=new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP,null);
        bufferedImage=cOp.filter(bufferedImage,null);

        System.out.println("初始图片:"+this.currentImageSize(image));
        System.out.println(
                "修改图片:"+this.currentImageSize(
                        new BufferedImage(
                                i.getWidth(null),
                                i.getHeight(null),BufferedImage.TYPE_INT_RGB
                        )
                )
        );
        System.out.println(
                "修改图片:"+this.currentImageSize(
                        new BufferedImage(
                                resizedImage.getWidth(null),
                                i.getHeight(null),BufferedImage.TYPE_INT_RGB
                        )
                )
        );
        System.out.println("修改图片:"+this.currentImageSize(bufferedImage));

        //System.out.println("scaleImageBySize Before Reset:"+out.toByteArray().length);
        out.reset();
        ImageIO.write(bufferedImage,this.outputFormat.getFormat(),out);
        System.out.println("scaleImageBySize:"+out.toByteArray().length);
        return out;
    }

    /**
     * 方法（私有）<br>
     * 名称:    cropImage<br>
     * 描述:    根据指定的位置和尺寸剪切图片<br>
     *
     * @return BufferedImage - 图片字节数组
     * @throws java.io.IOException
     */
    private ByteArrayOutputStream cropImage
    (ByteArrayOutputStream out,int x,int y,int width,int height)
            throws IOException
    {
        Iterator iterator=ImageIO.getImageReadersByFormatName(this.outputFormat.getFormat());
        ImageReader reader=(ImageReader)iterator.next();
        InputStream in=new ByteArrayInputStream(out.toByteArray());
        ImageInputStream iis=ImageIO.createImageInputStream(in);
        reader.setInput(iis,true);
        ImageReadParam param=reader.getDefaultReadParam();
        Rectangle rect=new Rectangle(x,y,width,height);
        param.setSourceRegion(rect);
        BufferedImage bi=reader.read(0,param);
        out.reset();
        ImageIO.write(bi,this.outputFormat.getFormat(),out);
        //System.out.println("dropImage:"+out.toByteArray().length);
        return out;
    }

    /**
     * 方法（私有）<br>
     * 名称:    currentImageSize<br>
     * 描述:    获取当前文件的大小<br>
     *
     * @return int - 当前文件的字节数
     * @throws java.io.IOException
     */
    private int currentImageSize(BufferedImage image) throws IOException
    {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ImageIO.write(image,this.outputFormat.getFormat(),out);
        return out.toByteArray().length;
    }
}