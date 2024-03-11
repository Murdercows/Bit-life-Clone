import javax.swing.*;
import java.util.Random;

public class BitLifeGame {
    private static final String[] NPC_NAMES = loadNPCNamesFromJSON();
    private static final String[] NPC_STORIES = {
        "You encounter %s, a mysterious traveler. They beckon you to follow them into a dimly lit alleyway. Do you follow?",
        "A strange figure dressed as a clown introduces themselves as %s. They invite you to join their circus. Do you accept their invitation?",
        "%s, a self-proclaimed wizard, approaches you with a peculiar offer. They claim to have the power to grant you a wish. Do you trust them?",
        "You meet %s, a shady individual with a sly grin. They propose a get-rich-quick scheme that may or may not be legal. Do you take the risk?",
        "%s, a quirky artist, invites you to their studio to witness their latest creation. They promise a life-changing experience. Do you go with them?",
        "A strange old woman named %s stops you on the street. She says she can see your future but needs a small donation first. Do you pay her fee?"
    };
    private static final String[] EVENTS = {
        "You find $%d on the ground.",
        "You get mugged and lose $%d.",
        "You win the lottery and receive $%d.",
        "You get a job offer with a salary of $%d per year. Do you accept?",
        "You get fined $%d for jaywalking.",
        "You go to the hospital and have to pay $%d in medical bills."
    };

    private static int age;
    private static double money;
    private static boolean parentsAlive;
    private static boolean[] friendsAlive;
    private static double jobSalary;

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        int choice = JOptionPane.showOptionDialog(null, "Welcome to Bit Life Game!", "Bit Life Game", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Start", "Quit"}, "Start");

        if (choice == 0) {
            String playerName = JOptionPane.showInputDialog(null, "Enter your name:");
            if (playerName == null) {
                return; // User canceled the input dialog
            }

            age = 0;
            money = 0;
            jobSalary = 0;
            parentsAlive = true;
            friendsAlive = new boolean[5]; // Assuming 5 friends
            for (int i = 0; i < friendsAlive.length; i++) {
                friendsAlive[i] = true;
            }

            playGame(playerName);
        } else {
            System.exit(0);
        }
    }

    private static void playGame(String playerName) {
        Random random = new Random();

        while (age < 100) {
            String npcName = NPC_NAMES[random.nextInt(NPC_NAMES.length)];
            int storyIndex = random.nextInt(NPC_STORIES.length);
            int choice = JOptionPane.showOptionDialog(null, String.format(NPC_STORIES[storyIndex], npcName), "Bit Life Game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No", "Check Money/Age", "Quit"}, "Yes");

            if (choice == 0) {
                handleEvent(random);
                age++;
            } else if (choice == 1) {
                JOptionPane.showMessageDialog(null, "You decline their offer and walk away.");
                age++;
            } else if (choice == 2) {
                checkMoneyAndAge();
            } else if (choice == 3) {
                int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit Game", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            if (age == 21) {
                JOptionPane.showMessageDialog(null, "You can now visit the casino.");
                playCasinoGame(random);
            }

            // Add job salary to balance
            if (jobSalary > 0) {
                money += jobSalary;
                JOptionPane.showMessageDialog(null, "You received your annual salary of $" + jobSalary + ".");
            }

            killRandomPeople(random);
        }

        JOptionPane.showMessageDialog(null, "Game Over!");
        System.exit(0);
    }

    private static void playCasinoGame(Random random) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Welcome to the casino!\nYour current balance: $" + money + "\nEnter the amount you want to bet (or 0 to exit):");
            if (input == null) {
                return; // User canceled the input dialog
            }

            double bet;
            try {
                bet = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                continue;
            }

            if (bet == 0) {
                break;
            } else if (bet > money) {
                JOptionPane.showMessageDialog(null, "You don't have enough money to make that bet.");
                continue;
            }

            boolean coinFlip = random.nextBoolean();

            if (coinFlip) {
                money += bet;
                JOptionPane.showMessageDialog(null, "You won! Your new balance: $" + money);
            } else {
                money -= bet;
                JOptionPane.showMessageDialog(null, "You lost. Your new balance: $" + money);
            }
        }
    }

    private static void killRandomPeople(Random random) {
        if (random.nextInt(100) < 2 && parentsAlive) {
            parentsAlive = false;
            JOptionPane.showMessageDialog(null, "Your parents have passed away due to an illness.");
        }

        for (int i = 0; i < friendsAlive.length; i++) {
            if (friendsAlive[i] && random.nextInt(100) < 1) {
                friendsAlive[i] = false;
                JOptionPane.showMessageDialog(null, "One of your friends has passed away due to a disease.");
            }
        }
    }

    private static String[] loadNPCNamesFromJSON() {
        // Code to load NPC names from a JSON file
        // For simplicity, we'll return a hardcoded array of names
        return new String[]{"Alice", "Bob", "Charlie", "David", "Eve"};
    }

    private static void handleEvent(Random random) {
        int eventIndex = random.nextInt(EVENTS.length);
        int amount = random.nextInt(1000) + 100; // Random amount between $100 and $1099

        switch (eventIndex) {
            case 0:
            case 2:
                money += amount;
                JOptionPane.showMessageDialog(null, String.format(EVENTS[eventIndex], amount));
                break;
            case 1:
            case 4:
            case 5:
                money -= amount;
                JOptionPane.showMessageDialog(null, String.format(EVENTS[eventIndex], amount));
                break;
            case 3:
                int response = JOptionPane.showConfirmDialog(null, String.format(EVENTS[eventIndex], amount), "Bit Life Game", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    jobSalary = amount;
                    JOptionPane.showMessageDialog(null, "You accepted the job offer!");
                } else {
                    JOptionPane.showMessageDialog(null, "You declined the job offer.");
                }
                break;
        }
    }

    private static void checkMoneyAndAge() {
        String message = "Your current age: " + age + " years\nYour current balance: $" + money;
        if (jobSalary > 0) {
            message += "\nYour current job salary: $" + jobSalary + " per year.";
        }
        JOptionPane.showMessageDialog(null, message);
    }
}