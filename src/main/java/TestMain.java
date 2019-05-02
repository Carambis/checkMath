import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMain {
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\alepeshko\\Pictures\\123.bmp";
        BufferedImage image = ImageIO.read(new File(fileName));
        List<int[]> images = new ArrayList<>();
        int[][] arrays = ImageUtils.getBW(image);

        int[][] newImage;
        List<int[]> results = segment(arrays);
        for (int[] result : results) {
            newImage = new int[result[1] - result[0]][result[3] - result[2]];
            for (int x = result[0]; x < result[1]; x++) {
                for (int j = result[2]; j < result[3]; j++) {
                    newImage[x - result[0]][j - result[2]] = arrays[x][j];
                }
            }
            ImageUtils.printImageInStackTrace(newImage);


        }
    }


    public static List<int[]> segment(int[][] image) {
        int height = image[0].length;
        int width = image.length;
        int maxLabels = 100;

        int[][] labels = new int[width][height];
        int[] equiv = new int[maxLabels];
        boolean[] used = new boolean[maxLabels];

        int[][] bbs = new int[maxLabels][4];
        List<int[]> result = new ArrayList<int[]>();
        for (int i = 0; i < maxLabels; i++) {
            equiv[i] = i;
            used[i] = false;
        }

        int currentLabel = 0;
        int[] neigh;
        int nbNei;
        int minLabel;
        for (int y = 1; y < height - 1; y++)
            for (int x = 1; x < width - 1; x++) {
                if (image[x][y] == 1) {
                    neigh = new int[4];
                    nbNei = 0;

                    if (labels[x - 1][y] > 0) neigh[nbNei++] = labels[x - 1][y];
                    if (labels[x - 1][y - 1] > 0) neigh[nbNei++] = labels[x - 1][y - 1];
                    if (labels[x][y - 1] > 0) neigh[nbNei++] = labels[x][y - 1];
                    if (labels[x + 1][y - 1] > 0) neigh[nbNei++] = labels[x + 1][y - 1];

                    if (nbNei == 0)
                        labels[x][y] = ++currentLabel;
                    else {
                        minLabel = maxLabels;
                        for (int i = 0; i < nbNei; i++)
                            if (neigh[i] < minLabel)
                                minLabel = equiv[neigh[i]];
                        labels[x][y] = equiv[minLabel];
                        for (int i = 0; i < nbNei; i++)
                            if (equiv[neigh[i]] > minLabel)
                                equiv[neigh[i]] = equiv[minLabel];
                    }
                }
            }
        for (int i = 0; i < maxLabels; i++)
            while (equiv[i] > equiv[equiv[i]]) equiv[i] = equiv[equiv[i]];

        int actLabel;
        for (int x = 1; x < width; x++)
            for (int y = 1; y < height - 1; y++) {
                if (labels[x][y] > 0) {
                    actLabel = equiv[labels[x][y]];
                    if (!used[actLabel]) {
                        bbs[actLabel][0] = x;
                        bbs[actLabel][1] = x;
                        bbs[actLabel][2] = y;
                        bbs[actLabel][3] = y;
                        used[actLabel] = true;
                    } else {
                        if (bbs[actLabel][0] > x) bbs[actLabel][0] = x;
                        if (bbs[actLabel][1] < x) bbs[actLabel][1] = x;
                        if (bbs[actLabel][2] > y) bbs[actLabel][2] = y;
                        if (bbs[actLabel][3] < y) bbs[actLabel][3] = y;
                    }
                }
            }

        for (int i = 0; i < maxLabels; i++)
            if (used[i]) {
                result.add(bbs[i]);
            }

        return result;

    }
}