import java.util.Random;

public class Main {
    public static int bossHealth = 650;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 280, 250, 300, 320, 200}; // Медик, Голем, Удачливый, Ведьмак, Тор
    public static int[] heroesDamage = {20, 15, 10, 5, 25, 0}; // Урон Медика, Голема, Удачливого, Ведьмака и Тора
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};
    public static int roundNumber = 0;
    public static Random random = new Random();

    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int health : heroesHealth) {
            if (health > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossAttack();
        heroesAttack();
        medicHeal();
        printStatistics();
    }

    public static void chooseBossDefence() {
        int randomIndex = random.nextInt(heroesAttackType.length - 3); // Выбор атаки, исключая новых героев
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossAttack() {
        boolean medicAlive = false;
        int medicIndex = -1;

        // Найти медика
        for (int i = 0; i < heroesAttackType.length; i++) {
            if (heroesAttackType[i].equals("Medic") && heroesHealth[i] > 0) {
                medicAlive = true;
                medicIndex = i;
                break;
            }
        }

        // Атаковать медика с увеличенным уроном, если он жив
        if (medicAlive) {
            if (heroesHealth[medicIndex] - (bossDamage * 2) < 0) { // Увеличенный урон
                heroesHealth[medicIndex] = 0;
            } else {
                heroesHealth[medicIndex] -= (bossDamage * 2);
            }
        }

        // Атаковать остальных героев
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i != medicIndex && heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] -= bossDamage;
                }
            }
        }
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && !heroesAttackType[i].equals("Medic") && !heroesAttackType[i].equals("Witcher")) {
                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    int coeff = random.nextInt(9) + 2; // Critical damage
                    damage *= coeff;
                    System.out.println("Critical Damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
    }

    public static void medicHeal() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesAttackType[i].equals("Medic") && heroesHealth[i] > 0) {
                for (int j = 0; j < heroesHealth.length; j++) {
                    if (heroesHealth[j] > 0 && heroesHealth[j] < 100) {
                        heroesHealth[j] += 20; // Лечение на 20 единиц
                        if (heroesHealth[j] > 100) {
                            heroesHealth[j] = 100; // Максимум 100 единиц здоровья
                        }
                        break; // Лечим только одного героя за раунд
                    }
                }
                break; // Медик лечит только один раз за раунд
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND: " + roundNumber + " ------------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }
}
