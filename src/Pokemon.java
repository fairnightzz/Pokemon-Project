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
    private ArrayList<Attack> attacks= new ArrayList();

    public Pokemon(ArrayList<String> pokemon){
        if (pokemon.size() == 1){
            name = pokemon.get(0);
            type = "NONE";
            res = "NONE";
            weak = "NONE";
        }
        else{
            name = pokemon.get(0);
            health = Integer.parseInt(pokemon.get(1));
            type = pokemon.get(2);
            res = pokemon.get(3);
            weak = pokemon.get(4);
            //Attack attack = new Attack();
            //System.out.println(pokemon);
            //System.out.println(pokemon);
            for (int i = 6;i<pokemon.size();i+=4){
                Attack attack = new Attack(pokemon.get(i),pokemon.get(i+1),pokemon.get(i+2),pokemon.get(i+3));
                //System.out.println(attack);
                attacks.add(attack);
            }
            //System.out.print(attacks);
        }
    }
    public boolean attack(Pokemon enemy){
        if (checkAttack()){
            System.out.println("You do not have enough energy to make an attack!");
            return false;
        }
        int option = -2;
        System.out.println("Choose an attack");
        Scanner kb = new Scanner(System.in);
        while (true){
            System.out.println("Choose an attack");

            option = kb.nextInt()-1;
            if (0<=option && option<attacks.size() && energy-attacks.get(option).energyCost>=0){
                energy-=attacks.get(option).energyCost;
                //System.out.println("Special:"+attacks.get(option).special);
                int damage = attacks.get(option).damage-disable;
                if (damage<10){
                    damage = 10;
                }
                if (type.equals(enemy.weak)){
                    damage*=2;
                }
                if (type.equals(enemy.res)){
                    damage/=2;
                }
                if (attacks.get(option).special.equals(" ")){
                    takeD(enemy,damage);
                    return true;
                }
                else if (attacks.get(option).special.equals("stun")){
                    System.out.print("Stun");//Make a code for stunned
                    takeD(enemy,damage);
                    enemy.stun = randint() ? 1:0;
                    return true;
                }
                else if (attacks.get(option).special.equals("wild card")){
                    System.out.print("Wild Card");
                    return true;
                }
                else if (attacks.get(option).special.equals("wild storm")){
                    System.out.print("Wild Storm");
                    return true;
                }
                else if (attacks.get(option).special.equals("disable")){
                    System.out.print("Disable his gender");
                    disable(enemy);
                    takeD(enemy,damage);
                    return true;
                }
                else if (attacks.get(option).special.equals("recharge")){
                    takeD(enemy,damage);
                    energy+=20;
                    System.out.print("recharge my guy");
                    return true;
                }
                else{
                    System.out.println("Error? Should never reach.");
                    return true;
                }
            }
            else{
                System.out.println("Incorrect selection, try again.");
            }
        }

    }

    private void disable(Pokemon enemy){
        enemy.disable+=10;
    }


    private void takeD(Pokemon enemy, int damage){
        enemy.health-=damage;

        if (enemy.health<=0){
            enemy.health = 0;
        }
        System.out.println(enemy.name+": Now at "+enemy.health);
    }

    private boolean randint(){
        int chance = (int)Math.random()*2;
        if (chance == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean dead(){
        if (health == 0){
            return true;
        }
        return false;
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
        if (min>energy){
            //System.out.println("True");
            return true;
        }
        else{
            return false;
        }

    }
    public boolean recharge(){
        energy+=20;
        if (energy > 50) {
            energy = 50;
        }
        System.out.printf("%s is now at %d energy!\n",name,energy);
        return true;
    }
    public String[] values(){
        return new String[] {name,Integer.toString(health),Integer.toString(energy),type,res,weak,Integer.toString(stun),Integer.toString(kStatus)};
    }

}
