package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private int seed;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};
    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.rand = new Random(seed);
        System.out.println(this.CHARACTERS);
        System.out.println(this.CHARACTERS[0]);

        StdDraw.setCanvasSize(this.width * 20, this.height * 20);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();



    }



    public String generateRandomString(int n) {

        String s = "";
        for(int i = 0; i<n; i++) {
            int rand_index = rand.nextInt(this.CHARACTERS.length);
            s = s + this.CHARACTERS[rand_index];
        }
        return s;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen

        int width_center = Math.round(this.width/2);
        int height_center = Math.round(this.height/2);
        StdDraw.clear(Color.BLACK); // initially clears the canvas

        if (!gameOver) {

            Font textfont = new Font("sans serif", Font.BOLD, 15);
            StdDraw.setFont(textfont);
            StdDraw.setPenColor(Color.cyan);
            StdDraw.textLeft(0.0, height - 1, "Round: " + round);
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[this.rand.nextInt(ENCOURAGEMENT.length)]);
            StdDraw.text(width_center, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.line(0, height - 2, width, height - 2);

        }

        Font font = new Font("sans serif", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(width_center, height_center, s);
        StdDraw.enableDoubleBuffering(); // you need these two variables
        StdDraw.show(); // you need this vairable




    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        StdDraw.clear(Color.BLACK);
        for (int i = 0; i< letters.length(); i++){

            String letter = letters.substring(i, i+1);
            drawFrame(letter);
            StdDraw.pause(750);
            drawFrame(" ");
            StdDraw.pause(750);

        }




    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String input = "";
        drawFrame(input);
        while(input.length() < n){
            if (!StdDraw.hasNextKeyTyped()){
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);
            drawFrame(input);
        }

        StdDraw.pause(500);
        return input;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Engine loop
        this.gameOver = false;
        this.playerTurn = false;
        this.round = 1;

        while (!this.gameOver){
            System.out.println(StdDraw.mouseX());
            String random_string = generateRandomString(this.round);
            flashSequence(random_string);
            String input = solicitNCharsInput(this.round);
            if(input.equals(random_string)){
                this.gameOver = false;
                this.round = round + 1;
            }
            else{
                this.gameOver = true;
                drawFrame(" Game over, you made it to Round " + this.round);
            }
        }
        }







    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        int seed = Integer.parseInt(args[0]);
        String h = "hello";
        System.out.println(h.substring(0,h.length()));



        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }
}
