package Settings;

import java.util.ArrayList;

public class Pokemon {
    /*
    public static void main(String[] args) {
        Settings.Pokemon pokemon = new Settings.Pokemon();
        pokemon.setName("電大太郎");
    }*/
    private String previous;
    private String name;
    private ArrayList<String> pokemonList = new ArrayList();
    private int battleFlag = 0;

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

    public int getWinFlag() { return battleFlag; }

    public void setWinFlag(int winFlag){ this.battleFlag = winFlag;}

    @Override
    public String toString() {
        return "Settings.Pokemon{" +
                "name='" + name + '\'' +
                "pokemonList='" + pokemonList + '\'' +
                "winFlag'" + battleFlag + '\'' +
                '}';
    }
}
