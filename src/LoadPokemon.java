import java.io.*;
import java.util.*;
public class LoadPokemon {
    //private static String deck[] = new String[4];
    private static ArrayList<ArrayList<String>> pdeck = new ArrayList();
    private static ArrayList<ArrayList<String>> pokemons = new ArrayList<ArrayList<String>>();
    private static ArrayList<ArrayList<String>> bdeck = new ArrayList();
    private static final ArrayList<String>empty = new ArrayList();
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

            empty.add("Empty");
            for (int i = 0;i<4;i++){
                pdeck.add(empty);
                bdeck.add(empty);
            }
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
    public static ArrayList<ArrayList<String>> bChoose(ArrayList<ArrayList<String>> pokemon){
        for (int i = 0;i<4;i++){
            int choice = (int)Math.random()*pokemons.size();
            bdeck.set(i,pokemons.get(choice));
            pokemons.remove(choice);
        }
        return pokemon;
    }

    public static boolean pCheck(){
        if (pdeck.contains(empty)){
            return false;
        }
        return true;
    }

    public static ArrayList<ArrayList<String>> choose(ArrayList<ArrayList<String>> pokemon){
        Scanner kb = new Scanner(System.in);
        int option = 0;
        int selected = 0;
        //pdeck = new ArrayList();
        while (option!=-1){
            display(1);
            display(2);
            option = kb.nextInt();
            if (option-1>=0 && option-1<=pokemons.size()){
                pdeck.set(selected,pokemons.get(option-1));
                pokemons.remove(option-1);
                selected+=1;
                if (selected == 4){
                    display(2);
                    return pdeck;
                }
            }
            else{
                System.out.println("Invalid Selection, try again.");
                option = 0;
            }
        }

        return pokemon;
    }
    public static void display(int choice){
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
            for (int i = 1; i<5;i++){

                display[i][0] = pdeck.get(i-1).get(0);


            }
            PokemonArena.pprint(display);
        }


    }





}
