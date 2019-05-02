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

    public static BufferedImage toImage(int[][] image) {
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

    public static int[][] deleteWhiteLine(int[][] image) {
        while (checkRow(image, 0)) {
            deleteFirstRow(image);
        }
        while (checkRow(image, image.length - 1)) {
            image = deleteLastRow(image);
        }
        while (checkColumn(image, 0)) {
            deleteLeftColumn(image);
        }
        while (checkColumn(image, image[0].length - 1)) {
            image = deleteRightColumn(image);
        }
        return image;
    }

    private static boolean checkRow(int[][] image, int row) {
        int width = image[row].length;
        for (int i = 0; i < width; i++) {
            if (image[row][i] == 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkColumn(int[][] image, int column) {
        for (int[] anImage : image) {
            if (anImage[column] == 1) {
                return false;
            }
        }
        return true;
    }

    private static int[][] deleteFirstRow(int[][] image) {
        if (image.length - 1 >= 0) {
            System.arraycopy(image, 1, image, 0, image.length - 1);
        }
        return image;
    }

    private static int[][] deleteLastRow(int[][] image) {
        int[][] newImage = new int[image.length - 1][image[0].length];
        System.arraycopy(image, 0, newImage, 0, newImage.length);
        return newImage;
    }

    private static int[][] deleteLeftColumn(int[][] image) {
        for (int[] anImage : image) {
            if (image[0].length - 1 >= 0) {
                System.arraycopy(anImage, 1, anImage, 0, image[0].length - 1);
            }
        }
        return image;
    }

    private static int[][] deleteRightColumn(int[][] image) {
        int[][] newImage = new int[image.length][image[0].length - 1];
        for (int i = 0; i < newImage.length; i++) {
            if (newImage[0].length - 1 >= 0) {
                System.arraycopy(image[i], 0, newImage[i], 0, newImage[0].length);
            }
        }
        return newImage;
    }

}
