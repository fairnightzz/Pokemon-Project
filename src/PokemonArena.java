import java.util.*;
import java.io.*;

public class PokemonArena {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static String[] deck = new String[4];
    public static void main(String[] args) {
        System.out.println("Pokemon Project");
        if (LoadPokemon.load()!= true){
            System.out.println("An error occurred in file loading. Please try again.");
        }
        else{
            menu();
        }
    }

    public static void menu(){
        System.out.println("Welcome to the Pokemon Project.");
        Scanner kb = new Scanner(System.in);
        int option = 0;
        while (option==0){
            pprint(new String [][]{{"Main Menu"},{"Battle"},{"Choose Deck"},{"View Pokemon"}});
            option = kb.nextInt();
            switch (option){
                case 1://Battle
                    //Run the function
                    System.out.println("Battle!");

                    break;
                case 2://Choose Deck
                    //Choose the Deck
                    System.out.println("Choose your deck!");


                    LoadPokemon.choose(deck);

                    break;
                default:
                    System.out.println("Invalid Response, try again");
                    option = 0;

            }
        }
    }
    public static String pprint(String[][] parameters){
        String print = "";
        for (int i = 0;i<parameters.length;i++){
            if (i == 0){
                //System.out.println()
                System.out.println(ANSI_RED+parameters[0][0]+ANSI_RESET);
            }
            else{
                if (parameters[i].length == 1){
                    System.out.println(ANSI_BLUE+i+ANSI_RESET+": "+parameters[i][0]);
                }
                else if (parameters[i].length>1){
                    System.out.print(ANSI_BLUE+i+ANSI_RESET+": "+parameters[i][0]);
                    for(int row = 1; row<parameters[i].length;row++){
                        System.out.print("  ---  "+parameters[row]);
                    }
                    System.out.println("");
                }

            }
        }
        return print;
    }

    private void choose(){

    }
}
