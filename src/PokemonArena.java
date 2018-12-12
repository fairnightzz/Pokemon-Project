//Zhehai Zhang
//ICS4U -01 Pokemon Project
//December 12th, 2018

//PokemonArena Class
//Runs the pokemon menu in order to become trainer supreme.

import java.util.*;

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

    public static int input(int start, int end){
        int option = -1;
        Scanner kb = new Scanner(System.in);
        while (true){
            option = kb.nextInt();
            if (start<=option && option<=end){
                break;
            }
            else{
                System.out.println("Invalid selection, try again.");
            }
        }
        return option;
    }

    public static void menu(){
        System.out.println("Welcome to the Pokemon Project.");
        pprint(new String [][]{{"Main Menu"},{"Battle"},{"Choose Deck"},{"View Pokemon"}});

        while (true){
            int option = input(1,3);
            switch (option){
                case 1://Battle
                    //Run the function
                    if (LoadPokemon.pCheck(deck) == false){
                        System.out.println("Please select your deck first!");
                        break;
                    }
                    System.out.println("Loading...");
                    bDeck = (ArrayList<Pokemon>)LoadPokemon.bChoose(bDeck).clone();
                    for (int i = 0;i<bDeck.size();i++){
                        bDeck.set(i,new Pokemon(bDeck.get(i)));
                        bDeck.get(i).toBot();
                    }
                    System.out.println("Bot Ready..");
                    battle();
                    returnM();
                    break;
                case 2://Choose Deck
                    //Choose the Deck
                    System.out.println("Choose your deck!");
                    deck = (ArrayList<Pokemon>)LoadPokemon.choose(deck).clone();
                    for (int i = 0;i<deck.size();i++){
                        deck.set(i,new Pokemon(deck.get(i)));
                    }
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
        pprint(new String [][]{{"Main Menu"},{"Battle"},{"Choose Deck"},{"View Pokemon"}});
    }
    private static void healAll(Pokemon current){
        current.heal();
        for (int i = 0;i<deck.size();i++){
            deck.get(i).heal();
        }
    }

    private static void battle(){
        Pokemon currentB = bDeck.get(0);
        bDeck.remove(0);
        System.out.println("Bot chooses "+currentB.name+"!");
        LoadPokemon.display(2,deck);
        System.out.println("Select a Pokemon from your team:");
        int option = input(1,4)-1;
        System.out.println(deck.get(option).name+", I choose you!");
        Pokemon currentP = deck.get(option);
        deck.remove(option);
        int playerTurn = (int)(Math.random()*2);
        int round = 0;
        Scanner kb = new Scanner(System.in);

        while (true){
            if (playerTurn == 0){
                System.out.println("Your turn");
                pprint(new String [][]{{"Choose your move:"},{"Attack"},{"Retreat"},{"Pass"}});
                option = kb.nextInt();
                switch(option){
                    case 1:
                        if (!currentP.attack(currentB)){
                            break;
                        }

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
                                playerTurn = (int)(Math.random()*2);
                                round = 0;
                            }
                            healAll(currentP);
                            recoverAll(currentP,50);
                            break;
                        }
                        else{
                            round+=1;
                            playerTurn = 1;
                        }
                        break;
                    case 2:
                        if (currentP.isStunned()){
                            //System.out.println("Stunned, cannot retreat");
                            break;
                        }
                        currentP = retreat(currentP);
                        System.out.println(currentP.name+", I choose you!");
                        //Retreat
                        round+=1;
                        playerTurn = 1;
                        break;
                    case 3:
                        //Passed
                        round+=1;
                        playerTurn = 1;
                        currentP.changeStun();
                        break;
                    default:
                        System.out.println("Invalid Selection, please try again");
                }
            }

            else if (playerTurn == 1){
                System.out.println("Bot's turn!");

                if (currentB.attack(currentP) == false && currentB.isStunned() == false){
                    //pass!
                    System.out.println("Bot has passed.");
                }
                else if (currentP.dead()){
                    System.out.println(currentP.name+" is knocked out!");
                    if (deck.size() == 0){
                        System.out.println("You are Defeated!");
                        deck = (ArrayList<Pokemon>)LoadPokemon.reset().clone();
                        //System.out.println(deck);
                        playerTurn = -1; //Done
                        break;
                    }
                    else{
                        LoadPokemon.display(3,deck);
                        System.out.println("Select a Pokemon from your team:");
                        int opt = input(1,deck.size())-1;
                        System.out.println(deck.get(opt).name+", I choose you!");
                        currentP = deck.get(opt);
                        deck.remove(opt);
                        round = 0;
                    }
                }
                playerTurn = 0;
                round+=1;
                System.out.println("The bot has finished.");
            }
            else{
                System.out.println("Battle is done!");
                break;
            }
            if (round%2 == 0){
                recoverAll(currentP,10);
                currentB.recharge(10);
                if (playerTurn == 0){
                    currentB.changeStun();
                }
            }
            if (playerTurn == -1){
                break;
            }
        }
    }

    private static void recoverAll(Pokemon current,int value){
        current.recharge(value);
        //enemy.recharge(10);
        for (int i = 0; i<deck.size();i++){
            deck.get(i).recharge(value);
        }
    }

    private static Pokemon retreat(Pokemon current){
        LoadPokemon.display(3,deck);
        int option = -2;
        Scanner kb = new Scanner(System.in);
        while (option ==-2){
            option = kb.nextInt()-1;
            if (option>=0 && option<deck.size()){
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
                    System.out.println();
                }
            }
        }
        return print;
    }
}
