import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageUtils {
    public static int[][] getBW(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int[][] arrays = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                Color color = new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                arrays[i][j] = color.equals(Color.BLACK) ? 1 : 0;
            }
        }
        return arrays;
    }

    public static BufferedImage scaleImage(int[][] arrays) {
        BufferedImage bi = new BufferedImage(arrays[0].length, arrays.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < arrays.length; i++) {
            for (int j = 0; j < arrays[i].length; j++) {
                if (arrays[i][j] == 1) {
                    bi.setRGB(j, i, 0);
                } else {
                    bi.setRGB(j, i, 1);
                }
            }
        }

        BufferedImage scaled = new BufferedImage(90, 90,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(bi, 0, 0, 90, 90, null);
        g.dispose();

        ImageUtils.printImageInStackTrace(getBW(scaled));
        return scaled;
    }

    public static BufferedImage toImage(int[][]image){
        BufferedImage bi = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 1) {
                    bi.setRGB(j, i, 0);
                } else {
                    bi.setRGB(j, i, 1);
                }
            }
        }
        return bi;
    }

    public static void printImageInStackTrace(int[][] image) {
        for (int[] ints : image) {
            StringBuilder line = new StringBuilder();
            for (int anInt : ints) {
                line.append(anInt);
            }

            System.out.println(line.toString());
        }
    }

}
