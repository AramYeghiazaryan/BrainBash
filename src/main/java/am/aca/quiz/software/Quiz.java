package am.aca.quiz.software;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Quiz {

    public static void main(String[] args) {

        SpringApplication.run(Quiz.class, args);

    }

    private static void menu() {

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {

            System.out.println("Please choose your role :");
            System.out.println("(u) User");
            System.out.println("(a) Admin");
            System.out.println("(q) Quit the application");

            String s = scanner.nextLine();

            switch (s.toLowerCase()) {

                case "u":
                    System.out.print("Enter your email : ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password : ");
                    String password = scanner.nextLine();

                    System.out.println("Authentication successful");


                    boolean userQuit = false;


                    while (!userQuit) {

                        System.out.println("Please enter your command : ");

                        System.out.println("\t (t) Test History");
                        System.out.println("\t (c) Categories");
                        System.out.println("\t (p) profile");
                        System.out.println("\t (l) Log out");
                        System.out.println("\t (q) Quit the application");

                        String userButton = scanner.nextLine();

                        switch (userButton.toLowerCase()) {

                            case "c":
                                System.out.println("\t\t (p) Programming");
                                System.out.println("\t\t (d) Databases");

                                String categoryButton = scanner.nextLine();

                                switch (categoryButton.toLowerCase()) {

                                    case "p":
                                        System.out.println("Programming category.\n");
                                        System.out.println("\t\t\t (c) c#");
                                        System.out.println("\t\t\t (v) vim");

                                        String subCategoryButton = scanner.nextLine();

                                        switch (subCategoryButton.toLowerCase()) {

                                            case "c":
                                                System.out.println("C# SubCategory.\n");
                                                System.out.println(" (c)  C# core.\n");
                                                System.out.println(" (b)  C# backend.\n");
                                                String topicButton = scanner.nextLine();

                                                switch (topicButton.toLowerCase()) {

                                                    case "c":
                                                        System.out.println("\t\t\t\t C# core test viewpage.\n");
                                                        break;
                                                    case "b":
                                                        System.out.println("c# backend test viewpage");
                                                        break;
                                                }
                                                break;
                                            case "v":
                                                System.out.println("Vim SubCategory.\n");
                                                break;
                                        }
                                        break;
                                }
                                break;

                            case "t":
                                System.out.println("Test history viewpage.\n");
                                break;

                            case "p":
                                System.out.println("Profile viewpage.\n");
                                break;

                            case "l":
                                System.out.println("Log out successful.\n");
                                userQuit = true;
                                break;

                            case "q":
                                System.out.println("Shutting down...");
                                quit = true;
                                userQuit = true;
                                break;
                        }

                    }
                    break;

                case "a":

                    boolean adminQuit = false;

                    while (!adminQuit) {
                        System.out.println("\n====================================\n");
                        System.out.println("All of user functionality is available");
                        System.out.println("\n====================================\n");
                        System.out.println("(d) view question database");
                        System.out.println("(e) edit databases");
                        System.out.println("(q) quit the application");
                        System.out.println("(l) Log out");

                        String adminButton = scanner.nextLine();
                        switch (adminButton.toLowerCase()) {

                            case "d":
                                System.out.println("Listing the whole database structure with all test,question and answer entries.\n");
                                break;

                            case "e":
                                System.out.println("Functionality to add,remove and edit questions.\n");
                                break;

                            case "q":
                                quit = true;
                                adminQuit = true;
                                System.out.println("Shutting down...");
                                break;

                            case "l":
                                System.out.println("Log out succesful");
                                adminQuit = true;
                                break;
                        }
                    }
                    break;
                case "q":
                    System.out.println("Quitting the application");
                    break;

            }
        }

    }

}