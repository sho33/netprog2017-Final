import java.util.ArrayList;

public class Pokemon {
    /*
    public static void main(String[] args) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName("電大太郎");
    }*/
    private String previous;
    private String name;
    private ArrayList<String> pokemonList = new ArrayList();
    private boolean winFlag = true;

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getPrevious() {
        return previous;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPokemonList(String name) {
        this.pokemonList.add(name);
    }

    public ArrayList<String> getPokemonList() {
        return pokemonList;
    }

    public boolean getWinFlag() { return winFlag; }

    public void setWinFlag(boolean winFlag){ this.winFlag = winFlag;}

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                "pokemonList='" + pokemonList + '\'' +
                "winFlag'" + winFlag + '\'' +
                '}';
    }
}
