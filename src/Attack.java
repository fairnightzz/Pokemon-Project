//Zhehai Zhang
//ICS4U -01 Pokemon Project
//December 12th, 2018

//Attack Class
//Keeps track attacks and its info

public class Attack {
    public final String name;
    public final int energyCost;
    public final int damage;
    public final String special;

    public Attack(String name, String energyCost, String damage, String special){
        this.name = name;
        this.energyCost = Integer.parseInt(energyCost);
        this.damage = Integer.parseInt(damage);
        this.special = special;

    }
}
