public class SymbolWeight {
    private String nameSymbol;
    private int[] weight;

    public SymbolWeight(String nameSymbol, int[] weight) {
        this.nameSymbol = nameSymbol;
        this.weight = weight;
    }

    public SymbolWeight() {
    }

    public String getNameSymbol() {
        return nameSymbol;
    }

    public void setNameSymbol(String nameSymbol) {
        this.nameSymbol = nameSymbol;
    }

    public int[] getWeight() {
        return weight;
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }
}
