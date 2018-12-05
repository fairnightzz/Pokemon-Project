import java.io.*;
import java.util.*;
public class LoadPokemon {
    private static String deck[] = new String[4];
    private static ArrayList<ArrayList<String>> pokemons = new ArrayList<ArrayList<String>>();
    public static boolean load(){
        try{
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
            String num = inFile.nextLine();
            while(inFile.hasNextLine()){
                ArrayList<String> pokemon = new ArrayList<String>();
                String[] p = inFile.nextLine().split(",");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(p));
                pokemons.add(arrayList);
            }

            //	<name>,<hp>,<type>,<resistance>,<weakness>,<num attacks>,[<attack name>, <energycost>,<damage>,<special>
            return true;
        }
        catch(IOException ex){
            System.out.println("Umm, there is nums.txt");
            return false;
        }
    }

    public ArrayList playerChoose(String pName) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).get(0).equals(pName)) {
                return pokemons.get(i);
            }
        }
        ArrayList invalid = new ArrayList();
        invalid.add("Invalid");
        return invalid;
    }

    public static String[] choose(String[] pokemon){
        Scanner kb = new Scanner(System.in);
        int option = 0;
        int selected = 0;
        deck = new String[4];
        while (option!=-1){
            display(1);
            display(2);
            option = kb.nextInt();
            if (option-1>=0 && option-1<=pokemons.size()){
                deck[selected] = pokemons.get(option-1).get(0);
                selected+=1;
                if (selected == 4){
                    display(2);
                    return deck;
                }

            }
            else if (option!=-1){
                System.out.println("Invalid Selection, try again.");
            }
        }
        return pokemon;
    }

    private static void display(int choice){
        if (choice == 1){
            String display[][] = new String[(pokemons.size()+1)][1];
            display[0][0] = "Enter a number to choose your Pokemon";
            //System.out.println(pokemons);
            for (int i = 1; i<pokemons.size()+1;i++){
                display[i][0] = pokemons.get(i-1).get(0);

            }
            PokemonArena.pprint(display);
        }
        else if (choice == 2){
            String display[][] = new String[5][1];
            display[0][0] = "Current Deck:";
            for (int i = 1; i<deck.length+1;i++){
                if (deck[i-1] == null){
                    display[i][0] = "Empty";
                }
                else{
                    display[i][0] = deck[i-1];
                }

            }
            PokemonArena.pprint(display);
        }


    }





}
