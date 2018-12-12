//Zhehai Zhang
//ICS4U -01 Pokemon Project
//December 12th, 2018

//PokemonArena Class
//Runs the pokemon menu in order to become trainer supreme.
/*
Methods:

1. Main - Starts the game and runs menu()
2. Menu - Gives options of battle, choose pokemon, and view pokemon
3. Input - Takes in input of numbers and returns the number
4. ReturnM - Asks if the user wants to return back to main menu
5. healAll - Heals all friendly pokemon
6. Battle - Handles all the battle components
7. recoverAll - Recharges all energy of friendly pokemon
8. Pprint - Prints text neatly

 */
import java.util.*;

public class PokemonArena {
    //Colours for the text
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    //Deck
    private static ArrayList<Pokemon> deck = new ArrayList();//User's Pokemon
    private static ArrayList<Pokemon> bDeck = new ArrayList();//Bot's Pokemon

    public static void main(String[] args) {
        System.out.println("Pokemon Project");
        //Checks if loading the file is successful
        if (LoadPokemon.load()!= true){
            System.out.println("An error occurred in file loading. Please try again.");
        }
        else{
            //Fills up the deck for view deck and for choosing the deck (.set())
            for (int i = 0;i<4;i++){
                deck.add(new Pokemon(LoadPokemon.empty));
            }
            menu();
        }
    }

    //Input method - Takes in input, and checks if the input is within the start and end parameters
    public static int input(int start, int end){
        int option = -1;
        Scanner kb = new Scanner(System.in);
        while (true){
            option = kb.nextInt();
            if (start<=option && option<=end){//If the input is valid
                break;
            }
            else{
                System.out.println("Invalid selection, try again.");
            }
        }
        return option;
    }

    //Menu Method
    public static void menu(){
        System.out.println("Welcome to the Pokemon Project.");
        //A neat menu
        pprint(new String [][]{{"Main Menu"},{"Battle"},{"Choose Deck"},{"View Pokemon"}});

        //Loop for going back to the main menu
        while (true){
            int option = input(0,3);//There are only 3 options for input
            switch (option){
                case 1://Battle
                    //The deck needs to be chosen in order to start
                    if (LoadPokemon.pCheck(deck) == false){
                        System.out.println("Please select your deck first!");
                        break;
                    }
                    //Choose the 4 Pokemon early on so I don't need to randomize after a Pokemon from the bot dies (rip the band-aid off early)
                    bDeck = (ArrayList<Pokemon>)LoadPokemon.bChoose(bDeck).clone();
                    for (int i = 0;i<bDeck.size();i++){
                        bDeck.set(i,new Pokemon(bDeck.get(i)));//To prevent shallow copies
                        bDeck.get(i).toBot();//I use the Pokemon Arena for bots and players, so I need to distinguish which from which
                    }
                    battle();
                    returnM();
                    break;
                case 2://Choose Deck/Pokemon
                    System.out.println("Choose your deck!");
                    //Prevent shallow copies
                    deck = (ArrayList<Pokemon>)LoadPokemon.choose(deck).clone();
                    for (int i = 0;i<deck.size();i++){
                        deck.set(i,new Pokemon(deck.get(i)));
                    }
                    returnM();
                    break;
                case 3://View Deck
                    System.out.println("View your deck");
                    LoadPokemon.display(2,deck);//Displays Pokemon
                    returnM();
                    break;

                case 0://Exits the game
                    System.out.println("Game exited.");
                    break;

                default:
                    System.out.println("Invalid Response, try again");
            }
        }
    }

    //returnM - Asks if the user wants to return back to main menu
    private static void returnM(){
        Scanner kb = new Scanner(System.in);
        System.out.println(ANSI_RED+"Return to main menu? (-1)"+ANSI_RESET);
        int exit = 0;
        while (exit !=-1){
            exit = kb.nextInt();
        }
        pprint(new String [][]{{"Main Menu"},{"Battle"},{"Choose Deck"},{"View Pokemon"}});
    }

    //healAll - Heals all friendly pokemon
    private static void healAll(Pokemon current){
        //Heals the current Pokemon
        current.heal();
        for (int i = 0;i<deck.size();i++){
            //Heals all other Pokemon in the deck
            deck.get(i).heal();
        }
    }

    //Battle - Handles all the battle components
    private static void battle(){
        //Get the first bot Pokemon
        Pokemon currentB = bDeck.get(0);
        bDeck.remove(0);//Take the current bot Pokemon out the array
        System.out.println("Bot chooses "+currentB.name+"!");

        //User selects a Pokemon
        LoadPokemon.display(2,deck);
        System.out.println("Select a Pokemon from your team:");
        int option = input(1,4)-1;
        System.out.println(deck.get(option).name+", I choose you!");
        Pokemon currentP = deck.get(option);
        deck.remove(option);

        //Randomize who goes first
        int playerTurn = (int)(Math.random()*2);
        int round = 0;//At the end of each even round, energy gets added

        Scanner kb = new Scanner(System.in);

        //Continuous battle loop until one trainer runs of of Pokemon
        while (true){

            //User's turn
            if (playerTurn == 0){
                System.out.println(ANSI_CYAN+"Your turn:"+ANSI_RESET);
                pprint(new String [][]{{"Choose your move:"},{"Attack"},{"Retreat"},{"Pass"}});
                option = kb.nextInt();
                switch(option){
                    //Attack
                    case 1:
                        //If attack returns false, that means some conditions inside the attack method are not right
                        //e.g not enough energy, is stunned
                        if (!currentP.attack(currentB)){
                            break;
                        }

                        //After the attack, if the bot's Pokemon is knocked out
                        else if (currentB.KO()){
                            System.out.println(currentB.name+" is knocked out!");

                            //If the bot has already called 4 Pokemon, the bot is defeated.
                            if (bDeck.size() == 0){
                                System.out.println("Enemy Defeated!");

                                //Prevent shallow copies
                                deck = (ArrayList<Pokemon>)LoadPokemon.reset().clone();
                                for (int i = 0;i<deck.size();i++){
                                    deck.set(i,new Pokemon(deck.get(i)));
                                }
                                playerTurn = -1; //Done
                                break;
                            }
                            else{
                                //Bot chooses the next Pokemon
                                currentB = bDeck.get(0);
                                bDeck.remove(0);
                                System.out.println("Enemy chooses "+currentB.name+"!");

                                //New battle, so randomize turn again
                                playerTurn = (int)(Math.random()*2);
                                round = 0;
                            }
                            //Heal everyone friendlies 20 HP, and recover all energy.
                            healAll(currentP);
                            recoverAll(currentP,50);
                            break;
                        }
                        else{
                            //Switch to the bot
                            round+=1;
                            playerTurn = 1;
                        }
                        break;

                    //Retreat
                    case 2:
                        //Cannot retreat if stunned
                        if (currentP.isStunned()){
                            break;
                        }
                        //Show the deck with stats
                        LoadPokemon.display(3,deck);

                        //Return your Pokemon back and retrieve another Pokemon of the user's choice
                        int opt = input(1,deck.size())-1;
                        deck.add(currentP);
                        currentP = deck.get(opt);
                        deck.remove(opt);
                        System.out.println(currentP.name+", I choose you!");
                        //Switch to bot's turn
                        round+=1;
                        playerTurn = 1;
                        break;
                    case 3:
                        //Passed
                        round+=1;
                        playerTurn = 1;

                        //If the Pokemon is stunned, then it is now not stunned anymore
                        currentP.changeStun();
                        break;
                    default:
                        System.out.println("Invalid Selection, please try again");
                }
            }

            //The bot's turn
            else if (playerTurn == 1){
                System.out.println(ANSI_PURPLE+"Bot's turn:"+ANSI_RESET);

                //If the bot cannot attack due to the lack of energy
                if (!currentB.attack(currentP)){
                    System.out.println("Bot has passed.");
                    currentB.changeStun();
                }
                //If the user's Pokemon is knocked out
                else if (currentP.KO()){
                    System.out.println(currentP.name+" is knocked out!");

                    //If the user is out of Pokemon
                    if (deck.size() == 0){
                        System.out.println("You are defeated, try again next time!");
                        //Prevent shallow copies
                        deck = (ArrayList<Pokemon>)LoadPokemon.reset().clone();
                        for (int i = 0;i<deck.size();i++){
                            deck.set(i,new Pokemon(deck.get(i)));
                        }
                        playerTurn = -1; //Done
                        break;
                    }
                    else{
                        //User needs to choose another pokemon
                        LoadPokemon.display(3,deck);
                        System.out.println("Select a Pokemon from your team:");
                        int opt = input(1,deck.size())-1;
                        System.out.println(deck.get(opt).name+", I choose you!");
                        currentP = deck.get(opt);
                        deck.remove(opt);
                        round = 0;
                    }
                }
                //Switch to user
                playerTurn = 0;
                round+=1;
                System.out.println("The bot has finished.");
            }
            else{
                System.out.println("Battle is done!");
                break;
            }
            //At the end of a turn after a user and bot
            if (round%2 == 0){
                recoverAll(currentP,10);
                currentB.recharge(10);
            }
            //End of the game
            if (playerTurn == -1){
                break;
            }
        }
    }

    //recoverAll - Recharges all energy of friendly pokemon
    private static void recoverAll(Pokemon current,int value){
        //Recharge the current Pokemon
        current.recharge(value);
        //Recharge the rest in the deck
        for (int i = 0; i<deck.size();i++){
            deck.get(i).recharge(value);
        }
    }

    //Makes printing easer when it comes to arrays
    public static void pprint(String[][] parameters){
        String print = "";

        //The first parameter should be a header
        for (int i = 0;i<parameters.length;i++){
            if (i == 0){
                //System.out.println()
                System.out.println(ANSI_RED+parameters[0][0]+ANSI_RESET);
            }
            else{
                //For simple options
                if (parameters[i].length == 1){
                    System.out.println(ANSI_BLUE+i+ANSI_RESET+": "+parameters[i][0]);
                }

                //For more than one item being displayed in each option e.g stats
                else if (parameters[i].length>1){
                    System.out.print(ANSI_BLUE+i+ANSI_RESET+": "+parameters[i][0]);
                    for(int row = 1; row<parameters[i].length;row++){
                        System.out.print("  ---  "+parameters[i][row]);
                    }
                    //Go to the next line for the next option
                    System.out.println();
                }
            }
        }
    }
}
