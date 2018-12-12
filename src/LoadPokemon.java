//Zhehai Zhang
//ICS4U -01 Pokemon Project
//December 12th, 2018

//LoadPokemon Class
//Keeps track of the stats of Pokemon. Methods change specific values of the Pokemon
/*
Methods:

1. Load - Loads all the Pokemon in, creates Pokemon objects and adds them to an ArrayList
2. bChoose - Bot chosen deck
3. pCheck - Checks if the user deck is empty or not
4. choose - User picks Pokemon for his deck
5. reset - Reset the decks
6. display - Displays multiple versions of the deck

 */

import java.io.*;
import java.util.*;

public class LoadPokemon {
    //Original User Deck
    private static ArrayList<Pokemon> pdeck = new ArrayList();
    //All Pokemon
    private static ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
    //Original Bot Deck
    private static ArrayList<Pokemon> bdeck = new ArrayList();
    //Empty ArrayList for making an Empty Pokemon
    public static final ArrayList<String> empty = new ArrayList();

    //Load - Loads all the Pokemon in, creates Pokemon objects and adds them to an ArrayList
    public static boolean load() {
        try {
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
            int num = Integer.parseInt(inFile.nextLine());
            //For loop for adding all the Pokemon objects
            for (int i = 0;i<num;i++) {
                String[] p = inFile.nextLine().split(",");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(p));
                pokemons.add(new Pokemon(arrayList));
            }

            //Currently, user deck and bot deck are empty
            empty.add("Empty");
            for (int i = 0; i < 4; i++) {
                pdeck.add(new Pokemon(empty));
                bdeck.add(new Pokemon(empty));
            }
            return true;
        } catch (IOException ex) {
            System.out.println("nums.txt not available");
            return false;
        }
    }

    //bChoose - Bot chosen deck
    public static ArrayList<Pokemon> bChoose(ArrayList<Pokemon> pokemon) {
        for (int i = 0; i < 4; i++) {
            //Randomize choice of pokemon
            int choice = (int) (Math.random() * pokemons.size());
            Pokemon p = new Pokemon(pokemons.get(choice));
            bdeck.set(i, p);
            //Remove it from ArrayList to be prevented from being picked again
            pokemons.remove(choice);
        }
        return bdeck;
    }

    //pCheck - Checks if the user deck is empty of not
    public static boolean pCheck(ArrayList<Pokemon> currentDeck) {
        return !currentDeck.get(0).name.equals("Empty");
    }

    //choose - User picks Pokemon for his deck
    public static ArrayList<Pokemon> choose(ArrayList<Pokemon> pokemon) {
        int option = 0;

        //Once it reaches 4, break the while loop
        int selected = 0;

        //To prevent the same Pokemon from being chosen
        ArrayList<Integer> index = new ArrayList();

        //Show the Pokemon list
        display(1, pdeck);

        while (true){
            //Displays current deck
            display(2, pdeck);
            option = PokemonArena.input(1,pokemons.size())-1;

            //If the pokemon is not in the deck
            if (!index.contains(option)){
                index.add(option);
                pdeck.set(selected, new Pokemon(pokemons.get(option)));
                selected += 1;
            }
            else{
                System.out.println("Invalid selection, try again.");
            }
            if (selected == 4) {//4 Pokemon were chosen
                break;
            }
        }
        display(2, pdeck);

        //To now remove the Pokemon that were chosen from the Pokemon list
        Collections.sort(index);
        for (int i = 0;i<4;i++){
            pokemons.remove(index.get(i)-i);//Every removal shifts the list index down 1 which is why we subtract i
        }
        return pdeck;
    }

    //reset - Reset the decks
    public static ArrayList<Pokemon> reset() {
        //Add the bot deck Pokemon back to the list
        for (int i = 0; i < bdeck.size(); i++) {
            pokemons.add(bdeck.get(i));
        }
        //pdeck has the reset values for the Pokemon, so I return the deck for the user
        return pdeck;
    }

    //display - Displays multiple versions of the deck
    public static void display(int choice, ArrayList<Pokemon> currentDeck) {

        //Displays the whole list of Pokemon
        if (choice == 1) {
            String display[][] = new String[(pokemons.size() + 1)][1];
            display[0][0] = "Enter a number to choose your Pokemon";
            //Display the name
            for (int i = 1; i < pokemons.size() + 1; i++) {
                display[i][0] = pokemons.get(i - 1).name;
            }
            PokemonArena.pprint(display);

        //Displays the user deck
        } else if (choice == 2) {//For displaying simply the deck
            String display[][] = new String[5][1];
            display[0][0] = "Current Deck:";
            //Displays the name
            for (int i = 1; i < 5; i++) {
                display[i][0] = currentDeck.get(i - 1).name;
            }
            PokemonArena.pprint(display);

        //Displays the user deck with stats on each Pokemon
        } else if (choice == 3) {
            String display[][] = new String[currentDeck.size() + 1][4];
            display[0][0] = "Choose:";
            for (int i = 1; i < currentDeck.size() + 1; i++) {
                //Gets all values in a Pokemon
                String[] value = currentDeck.get(i - 1).values();

                //Add values onto display
                display[i][0] = value[0];
                display[i][1] = "HP: " + value[1];
                display[i][2] = "Energy: " + value[2];
                display[i][3] = "Type: " + value[3];
            }
            PokemonArena.pprint(display);
        }
    }
}
