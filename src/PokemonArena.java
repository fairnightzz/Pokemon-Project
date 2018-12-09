import java.util.*;
import java.io.*;

public class PokemonArena {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static ArrayList<Pokemon> deck = new ArrayList();
    private static ArrayList<Pokemon> bDeck = new ArrayList();


    public static void main(String[] args) {
        System.out.println("Pokemon Project");
        if (LoadPokemon.load()!= true){
            System.out.println("An error occurred in file loading. Please try again.");
        }
        else{
            for (int i = 0;i<4;i++){
                deck.add(new Pokemon(LoadPokemon.empty));
            }
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

                    if (LoadPokemon.pCheck(deck) == false){
                        System.out.println("Please select your deck first!");
                        break;
                    }
                    System.out.println("Loading...");
                    bDeck = (ArrayList<Pokemon>)LoadPokemon.bChoose(bDeck).clone();
                    System.out.println(bDeck);
                    System.out.println("Bot Ready..");
                    battle();
                    returnM();
                    break;
                case 2://Choose Deck
                    //Choose the Deck
                    System.out.println("Choose your deck!");
                    deck = (ArrayList<Pokemon>)LoadPokemon.choose(deck).clone();
                    returnM();
                    break;
                case 3://View Deck
                    System.out.println("View your deck");
                    LoadPokemon.display(2,deck);
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

    private static void battle(){
        Pokemon currentB = bDeck.get(0);
        bDeck.remove(0);
        System.out.println("Bot chooses "+currentB.name+"!");
        Scanner kb = new Scanner(System.in);
        int option = -2;
        while (true){
            LoadPokemon.display(2,deck);
            System.out.println("Select a Pokemon from your team:");
            option = kb.nextInt()-1;
            if (option>=0 && option<4){
                System.out.println(deck.get(option).name+", I choose you!");
                break;
            }
            else{
                System.out.println("Invalid Selection, try again");
            }
        }
        Pokemon currentP = deck.get(option);
        deck.remove(option);
        int playerTurn = 0;

        while (true){
            if (playerTurn == 0){
                pprint(new String [][]{{"Choose your move:"},{"Attack"},{"Retreat"},{"Pass"}});
                option = kb.nextInt();
                switch(option){
                    case 1:
                        System.out.println("Attack!");
                        currentP.attack(currentB);
                        if (currentB.dead()){
                            System.out.println(currentB.name+" is knocked out!");
                            if (bDeck.size() == 0){
                                System.out.println("Enemy Defeated!");
                                deck = (ArrayList<Pokemon>)LoadPokemon.reset().clone();
                                System.out.println(deck);
                                playerTurn = -1; //Done
                                break;
                            }
                            else{
                                currentB = bDeck.get(0);
                                bDeck.remove(0);
                                System.out.println("Enemy chooses "+currentB.name+"!");
                            }

                        }
                        playerTurn = 1;
                        break;
                    case 2:
                        currentP = retreat(currentP);
                        System.out.println(currentP.name+", I choose you!");
                        //Retreat
                        playerTurn = 1;
                        break;
                    case 3:
                        for (int i = 0;i<deck.size();i++){
                            deck.get(i).recharge();
                        }
                        currentP.recharge();
                        playerTurn = 1;
                        break;
                    default:
                        System.out.println("Invalid Selection, please try again");
                }
            }

            else if (playerTurn == 1){
                System.out.println("On Bot, switch to player");

                playerTurn = 0;
            }
            else{
                System.out.println("Battle is done!");
                break;
            }
        }
    }

    private static Pokemon retreat(Pokemon current){
        LoadPokemon.display(3,deck);
        int option = -2;
        Scanner kb = new Scanner(System.in);
        while (option ==-2){
            option = kb.nextInt()-1;
            if (option>=0 && option<3){
                deck.add(current);
                current = deck.get(option);
                deck.remove(option);
                return current;
            }
            else{
                System.out.println("Invalid, try again.");
                option = -2;
            }
        }
        return current;
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
                        System.out.print("  ---  "+parameters[i][row]);
                    }
                    System.out.println("");
                }

            }
        }
        return print;
    }
}
