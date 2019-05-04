package hi.json.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author LiaoJL
 * @Date: 2019/5/4 20:00
 * @Description:
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * @param fileName 文件路径
     * @return
     */
    public static synchronized String readToString(String fileName) {
        return readToString(fileName, "UTF-8");
    }

    /**
     * @param fileName 文件路径
     * @param encoding 字符集
     * @return
     */
    public static synchronized String readToString(String fileName, String encoding) {
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("The OS does not support " + encoding);
            return null;
        }
    }

    public static void readNIO(String pathname) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(pathname));
            FileChannel channel = fin.getChannel();

            int capacity = 1000;// 字节
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            logger.debug("限制是：" + bf.limit() + ",容量是：" + bf.capacity() + " ,位置是：" + bf.position());
            int length = -1;

            while ((length = channel.read(bf)) != -1) {

                /*
                 * 注意，读取后，将位置置为0，将limit置为容量, 以备下次读入到字节缓冲中，从0开始存储
                 */
                bf.clear();
                byte[] bytes = bf.array();
                logger.debug("start..............");

                String str = new String(bytes, 0, length);
                logger.debug(str);
                //System.out.write(bytes, 0, length);

                logger.debug("end................");

                logger.debug("限制是：" + bf.limit() + "容量是：" + bf.capacity() + "位置是：" + bf.position());

            }

            channel.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeNIO(String filename, String context) {
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(new File(filename));
            FileChannel channel = fos.getChannel();
            ByteBuffer src = Charset.forName("utf8").encode(context);
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            logger.debug("初始化容量和limit：" + src.capacity() + ","
                    + src.limit());
            int length = 0;

            while ((length = channel.write(src)) != 0) {
                /*
                 * 注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                 */
                logger.debug("写入长度:" + length);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeNIO(String filename, String context, String encoding) {
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(new File(filename));
            FileChannel channel = fos.getChannel();
            ByteBuffer src = Charset.forName(encoding).encode(context);
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            logger.debug("初始化容量和limit：" + src.capacity() + ","
                    + src.limit());
            int length = 0;

            while ((length = channel.write(src)) != 0) {
                /*
                 * 注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                 */
                logger.debug("写入长度:" + length);
            }

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void testReadAndWriteNIO(String pathname, String filename) {
        FileInputStream fin = null;
        FileOutputStream fos = null;
        try {
            fin = new FileInputStream(new File(pathname));
            FileChannel channel = fin.getChannel();

            int capacity = 100;// 字节
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            logger.debug("限制是：" + bf.limit() + "容量是：" + bf.capacity() + "位置是：" + bf.position());
            int length = -1;

            fos = new FileOutputStream(new File(filename));
            FileChannel outchannel = fos.getChannel();


            while ((length = channel.read(bf)) != -1) {

                //将当前位置置为limit，然后设置当前位置为0，也就是从0到limit这块，都写入到同道中
                bf.flip();

                int outlength = 0;
                while ((outlength = outchannel.write(bf)) != 0) {
                    logger.debug("读，" + length + "写," + outlength);
                }

                //将当前位置置为0，然后设置limit为容量，也就是从0到limit（容量）这块，
                //都可以利用，通道读取的数据存储到
                //0到limit这块
                bf.clear();

            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
