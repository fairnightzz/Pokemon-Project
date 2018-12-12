import java.util.*;
public class Pokemon {
    public final String name;
    private int health;
    public final String type;
    public final String res;
    public final String weak;
    private int stun = 0;
    private int energy = 50;
    private int kStatus = 0;
    private int disable = 0;
    private int bot = 0;
    public final int maxH;
    private ArrayList<Attack> attacks= new ArrayList();

    public Pokemon(ArrayList<String> pokemon){
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
            for (int i = 6;i<pokemon.size();i+=4){
                Attack attack = new Attack(pokemon.get(i),pokemon.get(i+1),pokemon.get(i+2),pokemon.get(i+3));
                //System.out.println(attack);
                attacks.add(attack);
            }
            //System.out.print(attacks);
        }
    }

    public Pokemon(Pokemon pokemon){
        name = pokemon.name;
        health = pokemon.health;
        type = pokemon.type;
        res = pokemon.res;
        weak = pokemon.weak;
        attacks = pokemon.attacks;
        maxH = health;
    }

    public boolean attack(Pokemon enemy){
        if (checkAttack() && bot == 0){
            System.out.println("You do not have enough energy to make an attack!");
            return false;
        }
        if (isStunned()){
            return false;
        }
        int option = -2;
        //System.out.println("Choose an attack");
        if (bot == 0){
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

        Scanner kb = new Scanner(System.in);
        while (true){
            if (bot == 0){
                System.out.println("Choose an attack");
                option = kb.nextInt()-1;
            }
            else{
                for (int i = 0; i<attacks.size();i++){
                    if (attacks.get(i).energyCost<=energy){
                        option = i;
                        break;
                    }
                }
            }

            if (0<=option && option<attacks.size() && energy-attacks.get(option).energyCost>=0){
                energy-=attacks.get(option).energyCost;
                //System.out.println("Special:"+attacks.get(option).special);
                int damage = attacks.get(option).damage-disable;
                if (damage<0){
                    damage = 10;
                }
                if (type.equals(enemy.weak)){
                    System.out.printf("%s is weak to %s!\n",enemy.name,name);
                    damage*=2;
                }
                if (type.equals(enemy.res)){
                    System.out.printf("%s is resistant to %s!\n",enemy.name,name);
                    damage/=2;
                }
                if (attacks.get(option).special.equals(" ")){
                    takeD(enemy,damage);
                    return true;
                }
                else if (attacks.get(option).special.equals("stun")){
                    System.out.print("Stun");//Make a code for stunned
                    takeD(enemy,damage);
                    if (randint()){
                        enemy.stun = 1;
                        System.out.println("Stun Successful");
                    }
                    else{
                        System.out.println("Stun failed");
                    }
                    return true;
                }
                else if (attacks.get(option).special.equals("wild card")){
                    if (randint()){
                        takeD(enemy,damage);
                        System.out.println("Wild Card!");
                    }
                    else{
                        System.out.println("Wild Card Failed.");
                    }
                    return true;
                }
                else if (attacks.get(option).special.equals("wild storm")){
                    //System.out.print("Wild Storm");
                    while (true){
                        if (randint()){
                            takeD(enemy,damage);
                            System.out.println("Wild Storm Success!");
                        }
                        else{
                            System.out.println("Wild Storm Failed.");
                            break;
                        }
                    }
                    return true;
                }
                else if (attacks.get(option).special.equals("disable")){
                    System.out.printf("%s is disabled.\n",enemy.name);
                    disable(enemy);
                    takeD(enemy,damage);
                    return true;
                }
                else if (attacks.get(option).special.equals("recharge")){
                    takeD(enemy,damage);
                    recharge(20);
                    System.out.printf("%s is now at %d energy!\n",name,20);
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

    private void disable(Pokemon enemy){
        if (enemy.disable!=10){
            enemy.disable+=10;
        }
    }

    public void toBot(){
        bot = 1;
    }

    private void takeD(Pokemon enemy, int damage){
        enemy.health-=damage;

        if (enemy.health<=0){
            enemy.health = 0;
        }
        System.out.println(name+" has dealt "+damage+".");
        System.out.println(enemy.name+" is now at "+enemy.health+" HP");
    }

    private boolean randint(){
        int chance = (int)(Math.random()*2);
        return chance != 0;
    }

    public boolean isStunned(){
        if (stun == 1){
            System.out.println(name+" is stunned!");
            return true;
        }
        else{
            return false;
        }
    }
    public void changeStun(){
        stun = 0;
    }

    public boolean dead(){
        return health == 0;
    }
    private boolean checkAttack(){
        int min = 9999;
        //System.out.println("Checking attack");
        for (int i = 0;i<attacks.size();i++){
            if (attacks.get(i).energyCost<min){
                min = attacks.get(i).energyCost;
                //System.out.println(min);
            }
        }
        //System.out.println("True");
        return min > energy;
    }
    public boolean recharge(int amount){
        energy+=amount;
        if (energy > 50) {
            energy = 50;
        }
        //System.out.printf("%s is now at %d energy!\n",name,energy);
        return true;
    }
    public String[] values(){
        return new String[] {name,Integer.toString(health),Integer.toString(energy),type,res,weak,Integer.toString(stun),Integer.toString(kStatus)};
    }
    public void heal(){
        health+=20;
        if (health>maxH){
            health = maxH;
        }
    }

}
