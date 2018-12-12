import java.io.*;
import java.util.*;

public class LoadPokemon {
    //private static String deck[] = new String[4];
    private static ArrayList<Pokemon> pdeck = new ArrayList();
    private static ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
    private static ArrayList<Pokemon> bdeck = new ArrayList();
    public static final ArrayList<String> empty = new ArrayList();

    public static boolean load() {
        try {
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
            String num = inFile.nextLine();//MAKE A FORLOOP
            while (inFile.hasNextLine()) {
                String[] p = inFile.nextLine().split(",");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(p));
                pokemons.add(new Pokemon(arrayList));
            }
            //	<name>,<hp>,<type>,<resistance>,<weakness>,<num attacks>,[<attack name>, <energycost>,<damage>,<special>

            empty.add("Empty");
            for (int i = 0; i < 4; i++) {
                pdeck.add(new Pokemon(empty));
                bdeck.add(new Pokemon(empty));
            }
            return true;
        } catch (IOException ex) {
            System.out.println("Umm, there is nums.txt");
            return false;
        }
    }

    public static ArrayList<Pokemon> bChoose(ArrayList<Pokemon> pokemon) {
        for (int i = 0; i < 4; i++) {
            int choice = (int) (Math.random() * pokemons.size());
            Pokemon p = new Pokemon(pokemons.get(choice));
            bdeck.set(i, p);
            pokemons.remove(choice);
        }
        return bdeck;
    }

    public static boolean pCheck(ArrayList<Pokemon> currentDeck) {//CHECK THIS OVER
        return !currentDeck.get(0).name.equals("Empty");
    }

    public static ArrayList<Pokemon> choose(ArrayList<Pokemon> pokemon) {
        Scanner kb = new Scanner(System.in);
        int option = 0;
        int selected = 0;
        ArrayList<Integer> index = new ArrayList();
        display(1, pdeck);
        //pdeck = new ArrayList();
        while (true){
            display(2, pdeck);
            option = PokemonArena.input(1,pokemons.size())-1;
            if (!index.contains(option)){
                index.add(option);
                pdeck.set(selected, new Pokemon(pokemons.get(option)));
                //pokemons.remove(option - 1);
                selected += 1;
            }
            else{
                System.out.println("Invalid selection, try again.");
            }
            if (selected == 4) {
                break;
            }
        }
        display(2, pdeck);
        Collections.sort(index);
        for (int i = 0;i<4;i++){
            pokemons.remove(index.get(i)-i);
        }
        return pdeck;
        //return pokemon;
    }

    public static ArrayList<Pokemon> reset() {
        //RESET BotDECK
        for (int i = 0; i < bdeck.size(); i++) {
            pokemons.add(bdeck.get(i));
        }
        return pdeck;
    }

    public static void display(int choice, ArrayList<Pokemon> currentDeck) {
        if (choice == 1) {
            String display[][] = new String[(pokemons.size() + 1)][1];
            display[0][0] = "Enter a number to choose your Pokemon";
            for (int i = 1; i < pokemons.size() + 1; i++) {
                display[i][0] = pokemons.get(i - 1).name;
            }
            PokemonArena.pprint(display);
        } else if (choice == 2) {//For displaying simply the deck
            String display[][] = new String[5][1];
            display[0][0] = "Current Deck:";
            for (int i = 1; i < 5; i++) {
                display[i][0] = currentDeck.get(i - 1).name;
            }
            PokemonArena.pprint(display);
        } else if (choice == 3) {
            String display[][] = new String[currentDeck.size() + 1][4];
            display[0][0] = "Choose:";
            for (int i = 1; i < currentDeck.size() + 1; i++) {
                String[] value = currentDeck.get(i - 1).values();
                display[i][0] = value[0];
                display[i][1] = "HP: " + value[1];
                display[i][2] = "Energy: " + value[2];
                display[i][3] = "Type: " + value[3];
            }
            PokemonArena.pprint(display);
        }
    }
}
