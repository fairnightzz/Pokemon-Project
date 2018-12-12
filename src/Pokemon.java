//Zhehai Zhang
//ICS4U -01 Pokemon Project
//December 12th, 2018

//Pokemon Class
//Keeps track of the stats of Pokemon. Methods change specific values of the Pokemon
/*
Methods:

1. Pokemon - Initializes the object. There is an overload function that takes in a Pokemon object as a parameter
2. Attack - Deals damage, stuns, disables
3. takeD - Takes the actual damage
4. randint - Randomizes based on a parameter
5. isStunned - Checks if the Pokemon is stunned
6. changeStun - Takes off the stun effect
7. KO - Checks if the Pokemon is knocked out (hp = 0)
8. noAttack - Checks if the user can make any attack with its current energy
9. recharge - recharges the Pokemon's energy
10. values - returns Pokemon stats
11. heal - heal the Pokemon 20 HP
12. toBot - checks if the Pokemon is a bot
 */

import java.util.*;
public class Pokemon {

    //Essential Values
    //Some variables can be made public because it is final e.g name
    public final String name;
    private int health;
    public final String type;
    public final String res;
    public final String weak;
    private int stun = 0; // Stun is not a boolean because in the values method, I want to return an integer to string
    private int energy = 50;
    private int disable = 0;
    private boolean bot = false;
    public final int maxH;
    private ArrayList<Attack> attacks= new ArrayList();

    public Pokemon(ArrayList<String> pokemon){
        //For an empty Pokemon for the array
        if (pokemon.size() == 1){
            name = pokemon.get(0);
            type = "NONE";
            res = "NONE";
            weak = "NONE";
            maxH = 0;
        }
        else{
            name = pokemon.get(0);
            health = Integer.parseInt(pokemon.get(1));
            maxH = health;
            type = pokemon.get(2);
            res = pokemon.get(3);
            weak = pokemon.get(4);
            //Add attacks to an attack object
            for (int i = 6;i<pokemon.size();i+=4){
                Attack attack = new Attack(pokemon.get(i),pokemon.get(i+1),pokemon.get(i+2),pokemon.get(i+3));
                attacks.add(attack);
            }
        }
    }

    //To prevent shallow copies
    public Pokemon(Pokemon pokemon){
        name = pokemon.name;
        health = pokemon.health;
        type = pokemon.type;
        res = pokemon.res;
        weak = pokemon.weak;
        attacks = pokemon.attacks;
        maxH = health;
    }

    //Attack - Deals damage, stuns, disables
    public boolean attack(Pokemon enemy){

        //Check if Pokemon can attack at all
        if (noAttack() && !bot){
            System.out.println("You do not have enough energy to make an attack!");
            return false;
        }
        //For the bot so there is no print statement
        else if (noAttack()){
            return false;
        }
        //If the Pokemon is stunned
        if (isStunned()){
            return false;
        }

        //If the user is attacking, display options
        if (!bot){
            String display[][] = new String[attacks.size()+1][4];
            display[0][0] = "Choose an attack:";
            for (int i = 1;i<attacks.size()+1;i++){
                display[i][0] = "Name: "+attacks.get(i-1).name;
                display[i][1] = "Cost: " + Integer.toString(attacks.get(i-1).energyCost);
                display[i][2] = "Damage: " + Integer.toString(attacks.get(i-1).damage);
                display[i][3] = "Special: " + attacks.get(i-1).special;
            }
            PokemonArena.pprint(display);
        }

        int option = -2;
        Scanner kb = new Scanner(System.in);
        while (true){

            //If the user is attacking, take user input
            if (!bot){
                System.out.println("Choose an attack");
                option = kb.nextInt()-1;
            }
            else{
                //Randomize the attack
                ArrayList<Integer> options = new ArrayList();
                for (int i = 0; i<attacks.size();i++){
                    //Add all attacks that are valid
                    if (attacks.get(i).energyCost<=energy){
                        options.add(i);
                    }
                }
                option = options.get(randint(options.size()));
                //System.out.println(option+"rebel"+options);
                System.out.println("Bot chose "+attacks.get(option).name+"!");
            }

            //If the attack option is valid
            if (0<=option && option<attacks.size() && energy-attacks.get(option).energyCost>=0){

                //Deduct energy
                energy-=attacks.get(option).energyCost;
                //If Pokemon was disabled, subtract 10
                int damage = attacks.get(option).damage-disable;

                //Prevent damage from being below 0
                if (damage<0){
                    damage = 0;
                }

                //If the Pokemon type is the weak of the enemy, double the damage
                if (type.equals(enemy.weak)){
                    System.out.printf("%s is weak to %s!\n",enemy.name,name);
                    damage*=2;
                }

                //If the Pokemon type is the resistance of the enemy, half damage
                if (type.equals(enemy.res)){
                    System.out.printf("%s is resistant to %s!\n",enemy.name,name);
                    damage/=2;
                }

                //If it's a normal attack
                if (attacks.get(option).special.equals(" ")){
                    takeD(enemy,damage);
                    return true;
                }

                //If it's a stun attack
                else if (attacks.get(option).special.equals("stun")){
                    //If the stun was successful, change the stun variable to 1 (aka true)
                    if (randint(2) == 1){
                        enemy.stun = 1;
                        System.out.println("Stun Successful");
                    }
                    else{
                        System.out.println("Stun failed");
                    }
                    takeD(enemy,damage);
                    return true;
                }

                //Wild Card
                else if (attacks.get(option).special.equals("wild card")){
                    //Only succeeds 50% of the time.
                    if (randint(2) == 1){
                        takeD(enemy,damage);
                        System.out.println("Wild Card!");
                    }
                    else{
                        System.out.println("Wild Card Failed.");
                    }
                    return true;
                }

                //Wild Storm
                else if (attacks.get(option).special.equals("wild storm")){
                    //If attack works, keep on doing it (50% chance)
                    while (true){
                        if (randint(2) == 1){
                            System.out.println("Wild Storm Success!");
                            takeD(enemy,damage);
                        }
                        else{
                            System.out.println("Wild Storm Failed.");
                            break;
                        }
                    }
                    return true;
                }

                //Disable
                else if (attacks.get(option).special.equals("disable")){

                    //Makes sure disable only happens once
                    if (enemy.disable!=10){
                        System.out.printf("%s is disabled.\n",enemy.name);
                        enemy.disable+=10;
                    }
                    else{
                        System.out.println(enemy.name+" is already disabled!");
                    }
                    takeD(enemy,damage);
                    return true;
                }

                //Recharge
                else if (attacks.get(option).special.equals("recharge")){
                    takeD(enemy,damage);
                    recharge(20);
                    System.out.printf("Due to recharge, %s is now at %d energy!\n",name,20);
                    return true;
                }
            }
            else{
                if (energy-attacks.get(option).energyCost<0){
                    System.out.println("Not enough energy, select another attack.");
                }
                else{
                    System.out.println("Incorrect selection, try again.");
                }
            }
        }
    }

    //toBot - checks if the Pokemon is a bot
    public void toBot(){
        bot = true;//Turns it into a bot
    }

    //takeD - Takes the actual damage
    private void takeD(Pokemon enemy, int damage){
        //Take damage to health
        enemy.health-=damage;
        if (enemy.health<=0){
            enemy.health = 0;
        }
        System.out.println(name+" has dealt "+damage+".");
        System.out.println(enemy.name+" is now at "+enemy.health+" HP");
    }

    //randint - Randomizes based on a parameter
    private int randint(int length){
        int chance = (int)(Math.random()*length);
        return chance;
    }

    //isStunned - Checks if the Pokemon is stunned
    public boolean isStunned(){
        if (stun == 1){
            System.out.println(name+" is stunned!");
            return true;
        }
        else{
            return false;
        }
    }

    //changeStun - Takes off the stun effect
    public void changeStun(){
        stun = 0;
    }

    //KO - Checks if the Pokemon is knocked out (hp = 0)
    public boolean KO(){
        return health == 0;
    }

    //noAttack - Checks if the user can make any attack with its current energy
    private boolean noAttack(){
        //Takes the attack that costs the least, and checks if there is enough energy to do that attack (then larger attacks won't work)
        int min = 9999;
        for (int i = 0;i<attacks.size();i++){
            if (attacks.get(i).energyCost<min){
                min = attacks.get(i).energyCost;
            }
        }
        return (min > energy);
    }

    //recharge - recharges the Pokemon's energy
    public boolean recharge(int amount){
        energy+=amount;
        //To make sure energy never goes over 50
        if (energy > 50) {
            energy = 50;
        }
        return true;
    }

    //values - returns Pokemon stats
    public String[] values(){
        return new String[] {name,Integer.toString(health),Integer.toString(energy),type,res,weak,Integer.toString(stun)};
    }

    //heal - heal the Pokemon 20 HP
    public void heal(){
        health+=20;
        //Makes sure health does not go over max amount
        if (health>maxH){
            health = maxH;
        }
    }

}
