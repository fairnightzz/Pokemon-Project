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
    public static ArrayList<ArrayList<String>> deck = new ArrayList();
    public static ArrayList<ArrayList<String>> botdeck = new ArrayList();
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
        while (option!=-1){
            pprint(new String [][]{{"Main Menu"},{"Battle"},{"Choose Deck"},{"View Pokemon"}});
            option = kb.nextInt();
            switch (option){
                case 1://Battle
                    //Run the function
                    System.out.println("Battle!");
                    if (LoadPokemon.pCheck() == false){
                        System.out.println("Please select your deck first!");
                        break;
                    }
                    botdeck = LoadPokemon.bChoose(botdeck);
                    returnM();
                    break;
                case 2://Choose Deck
                    //Choose the Deck
                    System.out.println("Choose your deck!");
                    deck = LoadPokemon.choose(deck);
                    returnM();
                    break;
                case 3://View Deck
                    System.out.println("View your deck");
                    LoadPokemon.display(2);
                    returnM();
                    break;

                case -1://break
                    System.out.println("Game exited.");
                    break;

                default:
                    System.out.println("Invalid Response, try again");

            }
        }
    }

    private static void returnM(){
        Scanner kb = new Scanner(System.in);
        System.out.println(ANSI_RED+"Return to main menu? (-1)"+ANSI_RESET);
        int exit = 0;
        while (exit !=-1){
            exit = kb.nextInt();
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


}
