import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomNeuralNetwork {
    private static final int COUNT_LEARN = 10;
    private static final int SIZE_IMAGE = 9900;
    private static final int NUMBER_SET = 3;
    private static final int BIAS = 13;
    private static final String PATH = "src\\main\\resources\\study_file\\";
    private static final String PATH_TO_WEIGHTS = "src\\main\\resources\\weight";
    private static final String PATH_TEST = "src\\main\\resources\\test_file\\";
    private static final String PNG = ".PNG";
    private static final String TXT = ".TXT";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        List<SymbolWeight> weightList = new ArrayList<>();
        weightList = learn();
        test(weightList);
    }

    private static boolean proceed(int[] number, int[] weights) {
        int net = 0;
        for (int i = 0; i < number.length; i++) {
            net += number[i] * weights[i];
        }
        return net >= BIAS;
    }

    private static void decrease(int[] number, int[] weights) {
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                weights[i]--;
            }
        }
    }

    private static void increase(int[] number, int[] weights) {
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                weights[i]++;
            }
        }
    }

    private static int[] readImage(String fileName) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        int[][] arrays = ImageUtils.getBW(image);
        arrays = ImageUtils.deleteWhiteLine(arrays);

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

        BufferedImage scaled = new BufferedImage(90, 110,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(bi, 0, 0, 90, 110, null);
        g.dispose();

        arrays = ImageUtils.getBW(scaled);

        int[] points = new int[arrays.length * arrays[0].length];
        int k = 0;
        for (int[] array : arrays) {
            for (int anArray : array) {
                points[k] = anArray;
                k++;
            }
        }

        return points;
    }

    private static List<SymbolWeight> learn() throws IOException {
        System.out.println("Start learning");
        List<SymbolWeight> weightList = new ArrayList<>();
        List<ImageSymbols> listImage = new ArrayList<>();
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\-.bmp"), "-"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\+.bmp"), "+"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\2.bmp"), "2"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\a.bmp"), "a"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\b.bmp"), "b"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\c.bmp"), "c"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\x.bmp"), "x"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\y.bmp"), "y"));
        listImage.add(new ImageSymbols(readImage("C:\\Users\\alepeshko\\Pictures\\z.bmp"), "z"));


        String[] symbols = new String[]{"-", "+", "2", "a", "b", "c", "x", "y", "z",};
        for (String symbol : symbols) {
            int[] weights = new int[SIZE_IMAGE];
            for (int i = 0; i < 2000; i++) {
                for (ImageSymbols imageSymbols : listImage) {
                    if (!imageSymbols.symbol.equals(symbol)) {
                        if (proceed(imageSymbols.image, weights)) {
                            System.out.println("Symol - " + symbol + " iteration - " + i + "decrease");
                            decrease(imageSymbols.image, weights);
                        }
                    } else {
                        if (!proceed(imageSymbols.image, weights)) {
                            System.out.println("Symol - " + symbol + " iteration - " + i + "increase");
                            increase(imageSymbols.image, weights);
                        }
                    }
                }

            }
            writeFile(weights, symbol);
            weightList.add(new SymbolWeight(symbol, weights));
        }
        System.out.println("Finish");
        return weightList;
    }

    private static void writeFile(int[] weights, String number) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(PATH_TO_WEIGHTS + number + TXT));
        for (int weight : weights) {
            fileWriter.append(String.valueOf(weight));
            fileWriter.append("\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }

    private static int[] readFile(int number) throws IOException {
        int[] weights = new int[SIZE_IMAGE];
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_TO_WEIGHTS + number + TXT))) {
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {
                weights[i] = Integer.parseInt(str);
                i++;
            }
        }
        return weights;
    }

    private static void test(List<SymbolWeight> weightList) throws IOException {
        String fileName = "C:\\Users\\alepeshko\\Pictures\\123.bmp";
        BufferedImage image = ImageIO.read(new File(fileName));
        List<int[]> images = new ArrayList<>();
        int[][] arrays = ImageUtils.getBW(image);
        List<int[]> results = TestMain.segment(arrays);
        for (int[] result : results) {
            int[][] newImage = new int[result[1] - result[0]][result[3] - result[2]];
            for (int x = result[0]; x < result[1]; x++) {
                for (int j = result[2]; j < result[3]; j++) {
                    newImage[x - result[0]][j - result[2]] = arrays[x][j];
                }
            }

            ImageUtils.printImageInStackTrace(newImage);

            BufferedImage bi = new BufferedImage(newImage[0].length, newImage.length, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < newImage.length; i++) {
                for (int j = 0; j < newImage[i].length; j++) {
                    if (newImage[i][j] == 1) {
                        bi.setRGB(j, i, 0);
                    } else {
                        bi.setRGB(j, i, 1);
                    }
                }
            }

            BufferedImage scaled = new BufferedImage(90, 110,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaled.createGraphics();
            g.drawImage(bi, 0, 0, 90, 110, null);
            g.dispose();

            newImage = ImageUtils.getBW(scaled);

            ImageUtils.printImageInStackTrace(newImage);

            int[] points = new int[newImage.length * newImage[0].length];
            int k = 0;
            for (int[] array : newImage) {
                for (int anArray : array) {
                    points[k] = anArray;
                    k++;
                }
            }
            images.add(points);
        }
        for (int[] imageByte : images) {
            for (SymbolWeight weight : weightList) {
                if (proceed(imageByte, weight.getWeight())) {
                    System.out.println("Symbol - " + weight.getNameSymbol());
                    break;
                }
            }
        }
    }


}
