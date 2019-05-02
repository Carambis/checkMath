public class ImageSymbols {
    int[] image;
    String symbol;

    public ImageSymbols(int[] image, String symbol) {
        this.image = image;
        this.symbol = symbol;
    }

    public ImageSymbols() {
    }

    public int[] getImage() {
        return image;
    }

    public void setImage(int[] image) {
        this.image = image;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
