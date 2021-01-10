import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Mathdoku mathdoku = new Mathdoku();
        //System.out.println("Enter Input File");
        //Scanner sc=new Scanner(System.in);
        Scanner sc=new Scanner("/Users/souvikdas/IdeaProjects/souvik/Mathdoku/src/Input_Assignment4.txt");
        //Scanner sc2=new Scanner("/Users/souvikdas/IdeaProjects/souvik/Mathdoku/src/Input_8E.txt");
//        Scanner sc=new Scanner("/Users/souvikdas/IdeaProjects/souvik/Mathdoku/src/Input_8H.txt");
//        Scanner sc=new Scanner("/Users/souvikdas/IdeaProjects/souvik/Mathdoku/src/Input_7Error.txt");
        //Scanner sc=new Scanner("/Users/souvikdas/IdeaProjects/souvik/Mathdoku/src/Input_8H.txt");

        //Scanner sc2=new Scanner("/Users/souvikdas/IdeaProjects/souvik/Mathdoku/src/Input_7Error.txt");
        String next = sc.next();
        //String next2 = sc2.next();
        if(next!=null){
            try {
                //FileReader fileReader= new FileReader(next);
                FileReader fileReader= new FileReader(next);
                BufferedReader bufferedReader=new BufferedReader(fileReader);
                System.out.println("Loading of puzzle: "+mathdoku.loadPuzzle(bufferedReader));
            } catch (FileNotFoundException e) {
                //System.out.println("File Not found");
            }
        }

        //For checking if the puzzle is solvable
        System.out.println("Ready to solve: "+mathdoku.readyToSolve());

        long t = System.currentTimeMillis();
        //For calling the solve method
        System.out.println("Mathdoku solve status: "+ mathdoku.solve());

        System.out.println("time of execution: " + (System.currentTimeMillis() - t));

        //To print the puzzle
        System.out.println(mathdoku.print());

        //To print the number of choices
        System.out.println("Choices made: "+mathdoku.choices());

    }
}