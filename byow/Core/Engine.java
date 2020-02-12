package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.SaveDemo.Editor;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    private TERenderer ter;
    private Random rand;
    private TETile[][] tiles;
    private TETile[][] spookytiles;
    private String input_seed;
    private int[] position;
    private String directions;
    private int number_of_foods;
    private int total_moves;



    public Engine(){

        this.ter = new TERenderer();
        this.input_seed = "";
        this.directions = "";
        this.number_of_foods = 4;
        Random r = new Random();
        this.total_moves = r.nextInt(50) + 100;


    }

    public void initialize_frame(){
        StdDraw.setCanvasSize(this.WIDTH * 20, this.HEIGHT * 20);
        int width = this.WIDTH;
        int height = this.HEIGHT;
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(width/2, 2*height/3, "CS61B Game");

        Font font_two = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font_two);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(width/2, height/3, "New Game(N)");
        StdDraw.text(width/2, height/3 - 2, "Load Game (L)");
        StdDraw.text(width/2, height/3 - 4, "Quit Game (Q)");
        StdDraw.text(width/2, height/3 - 6, "Instructions (I)");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();


    }


    private void draw_frame(String s){

        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(this.WIDTH/2, 2*this.HEIGHT/3, "Please Input Seed Below and Press S!");
        StdDraw.line(this.WIDTH/2 - 10, this.HEIGHT/2, this.WIDTH/2 + 10, this.HEIGHT/2);
        StdDraw.text(this.WIDTH/2, this.HEIGHT/2 + 0.5, s);

        StdDraw.enableDoubleBuffering();
        StdDraw.show();


    }

    private void draw_frame_seed(){

        StdDraw.clear(Color.BLACK);
        draw_frame(this.input_seed);

        while (this.input_seed.length() < 20){

            if(!StdDraw.hasNextKeyTyped()){
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            try{
                if(key == 's'){
                    break;
                }
                else {
                    String temp = String.valueOf(key);
                    int num = Integer.parseInt(temp); // used as a test to make sure that only numbers are inputed into the seed.
                    input_seed = input_seed + temp;
                    draw_frame(input_seed);
                }

            }
            catch (Exception e){

            }
        }
    }

    private void drawworld(){

        StdDraw.clear(Color.BLACK);
        this.ter.renderFrame(this.tiles);
        StdDraw.show();
        StdDraw.enableDoubleBuffering();



    }



    private int[] generate_avatar_location(){

        int x;
        int y;

         x = this.rand.nextInt(this.tiles[0].length);
         y = this.rand.nextInt(this.tiles[1].length);

        while(!check_valid(x,y)){

            x = this.rand.nextInt(this.tiles[0].length);
            y = this.rand.nextInt(this.tiles[1].length);
        }

        this.tiles[x][y] = Tileset.AVATAR;
        int[] avatar_location = new int[2];
        avatar_location[0] = x;
        avatar_location[1] = y;
        return avatar_location;
    }

    private boolean check_valid(int x, int y){

        if (this.tiles[x][y] == Tileset.FLOWER || this.tiles[x][y] == Tileset.WATER){
            return true;
        }
        else if (this.tiles[x][y] == Tileset.AVATAR){
            return true;
        }

        else{
            return false;
        }
    }

    private boolean check_food(int x, int y){
        return this.tiles[x][y] == Tileset.WATER;

    }




    private void move(char s, String input){

        int[] pos = this.position;
        if((s == 'W'|| s == 'w') && check_valid(pos[0], pos[1] + 1)){

            if(check_food(pos[0], pos[1] + 1)){
                this.number_of_foods -= 1;
            }

            this.tiles[pos[0]][pos[1]] = Tileset.FLOWER;
            this.tiles[pos[0]][pos[1] + 1] = Tileset.AVATAR;
            this.position[1] = pos[1] + 1; // move value up
            this.directions += 'W';
            this.total_moves -= 1;
            if(input.equals("keyboard")){
                drawworld();
            }


        }
        else if((s == 'S'|| s== 's') && check_valid(pos[0], pos[1] - 1)){

            if(check_food(pos[0], pos[1] -1)){
                this.number_of_foods -= 1;
            }
            this.tiles[pos[0]][pos[1]] = Tileset.FLOWER;
            this.tiles[pos[0]][pos[1] - 1] = Tileset.AVATAR;
            this.position[1] = pos[1] - 1;
            this.directions += 'S';
            this.total_moves -= 1;
            if(input.equals("keyboard")){
                drawworld();
            }
        }
        else if((s == 'D' || s == 'd') && check_valid(pos[0] + 1, pos[1])){ // move right
            if(check_food(pos[0] + 1, pos[1])){
                this.number_of_foods -= 1;
            }

            this.tiles[pos[0]][pos[1]] = Tileset.FLOWER;
            this.tiles[pos[0] + 1][pos[1]] = Tileset.AVATAR;
            this.position[0] = pos[0] + 1;
            this.directions += 'D';
            this.total_moves -= 1;
            if(input.equals("keyboard")){
                drawworld();
            }
        }
        else if((s == 'A' || s == 'a') && check_valid(this.position[0] -1, this.position[1])){

            if(check_food(pos[0]-1, pos[1])){
                this.number_of_foods -= 1;
            }
            this.tiles[pos[0]][pos[1]] = Tileset.FLOWER;
            this.tiles[pos[0] - 1][pos[1]] = Tileset.AVATAR;
            this.position[0] = pos[0] - 1;
            this.directions += 'A';
            this.total_moves -= 1;
            if(input.equals("keyboard")){
                drawworld();
            }
        }
        else{

        }
    }



    public void start_game(String s) {

        //this.total_moves = this.rand.nextInt(50) + 100;

        if (s.equals("New")) {
            Long seed = Long.parseLong(this.input_seed);
            this.rand = new Random(seed); // not sure if this is correct way to approach this problem?
            Createworld cr = new Createworld(seed);
            this.tiles = cr.returnTile();
            int[] start_location = generate_avatar_location();
            ArrayList<int[]> start_food = generate_foods();
            this.position = start_location;
        }

        String cont = "continue";
        this.ter.renderFrame(this.tiles);
        InputSource inputSource = new KeyboardInputSource();




            while (cont.equals("continue") && this.total_moves > 0 && this.number_of_foods > 0) {

                while (!StdDraw.hasNextKeyTyped()) {

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.filledRectangle(this.tiles[0].length / 2 + 30, this.tiles[1].length - 2, 10, 2);
                    StdDraw.filledRectangle(this.tiles[0].length / 2, this.tiles[1].length - 2, 10, 2);
                    StdDraw.filledRectangle(this.tiles[0].length / 2 + 47, this.tiles[1].length - 2, 10, 2);
                    TETile type = getTileat((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
                    Font textfonttwo = new Font("sans serif", Font.BOLD, 25);
                    StdDraw.setFont(textfonttwo);
                    StdDraw.setPenColor(Color.WHITE);
                    String descriptiontwo = "Description: " + type.description();
                    String description = "Moves: " + total_moves;
                    String descriptionthree = "Foods Remaining: " + this.number_of_foods;
                    StdDraw.text(this.tiles[0].length / 2 + 30, this.tiles[1].length - 2, description);
                    StdDraw.text(this.tiles[0].length / 2, this.tiles[1].length - 2, descriptiontwo);
                    StdDraw.text(this.tiles[0].length/2 + 47, this.tiles[1].length -2, descriptionthree);
                    StdDraw.show();
                    StdDraw.enableDoubleBuffering();
                }


                char c = inputSource.getNextKey();
                move(c, "keyboard");


                if (c == ':' && inputSource.getNextKey() == 'Q') { // Breaks it if we press Q

                    saveEditor("seed", this.input_seed);
                    saveEditor("movements", this.directions);
                    System.out.println(this.directions);
                    System.exit(0);
                    break;
                }
            }
        if(this.total_moves == 0){
            sorry();
        }
        else if(this.number_of_foods == 0){
            congrats();

        }
    }





    private TETile getTileat(int x, int y){

        return this.tiles[x][y];

    }

   // }


    private ArrayList generate_foods(){

        ArrayList<int[]> foods = new ArrayList<>();


        for(int i = 0; i< this.number_of_foods; i++) {


            int x = this.rand.nextInt(this.tiles[0].length);
            int y = this.rand.nextInt(this.tiles[1].length);

            while (!check_valid(x, y)) {

                x = this.rand.nextInt(this.tiles[0].length);
                y = this.rand.nextInt(this.tiles[1].length);
            }

            this.tiles[x][y] = Tileset.WATER;
            int[] avatar_location = new int[2];
            avatar_location[0] = x;
            avatar_location[1] = y;
            foods.add(avatar_location);
        }

        return foods;
    }




    private void saveEditor(String s, String entry) {

        File f = new File(s + ".txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(entry);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private String loadEditor(File f) {

        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return "";
    }
    public void instructions_page(){

        StdDraw.clear(Color.BLACK);
        Font font_two = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font_two);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(this.WIDTH/2 , this.HEIGHT - 2, "Instructions to play the game" );

        Font font_three = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font_three);
        StdDraw.setPenColor(Color.green);
        StdDraw.picture(this.WIDTH/2, this.HEIGHT/2 - 2, "new image 2.PNG",38,32 );
        //StdDraw.text(5 , this.HEIGHT - 10, "User is able to move around the world. The objective of the game is to collect as many tokens as possible before time runs out. if the user would like to save game, he is able to quit and reload it upon request." );

        StdDraw.enableDoubleBuffering();
        StdDraw.show();

    }



    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        initialize_frame();
        InputSource inputSource = new KeyboardInputSource();
        char c = inputSource.getNextKey();
        while(inputSource.possibleNextInput() && c != 'Q') {
            if (c == 'N') {
                // instructions_page();
                draw_frame_seed();
                start_game("New");
                break;
            } else if (c == 'L') {
                File seed = new File("seed.txt");
                File directions = new File("movements.txt");
                String old_seed = loadEditor(seed);
                String old_directions = loadEditor(directions);
                load_and_move(old_seed, old_directions);
                start_game("Load");
            } else if (c == 'I') {
                instructions_page();
                if (inputSource.getNextKey() == 'B') {
                    interactWithKeyboard();
                }
            }
        }

    }

    private void sorry(){

        StdDraw.clear(Color.BLACK);
        Font font_two = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font_two);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(this.WIDTH/2 + 10 , this.HEIGHT /2 + 10, "Sorry Tough loss :(" );

        Font font_three = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font_three);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(this.WIDTH/2, this.HEIGHT/2 + 10 - 5, "Quit Game (q)");
        StdDraw.text(this.WIDTH/2, this.HEIGHT/2 + 10 - 7, "Play again (p)");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();

        InputSource inputSource = new KeyboardInputSource();
        char c = inputSource.getNextKey();

        while(inputSource.possibleNextInput()) {
            if (c == 'Q') {
                System.exit(0);
            } else if (c == 'P') {
                Engine ry = new Engine();
                ry.interactWithKeyboard();
                break;
            }
        }


    }

    private void congrats(){

            StdDraw.clear(Color.BLACK);
            Font font_two = new Font("Monaco", Font.BOLD, 50);
            StdDraw.setFont(font_two);
            StdDraw.setPenColor(Color.green);
            StdDraw.text(this.WIDTH/2 + 10 , this.HEIGHT /2 + 10, "Congrats you won!!" );

            Font font_three = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font_three);
            StdDraw.setPenColor(Color.green);
            StdDraw.text(this.WIDTH/2, this.HEIGHT/2 + 10 - 5, "Quit Game (q)");
            StdDraw.text(this.WIDTH/2, this.HEIGHT/2 + 10 - 7, "Play again (p)");
            StdDraw.enableDoubleBuffering();
            StdDraw.show();

            InputSource inputSource = new KeyboardInputSource();
            char c = inputSource.getNextKey();

            while(inputSource.possibleNextInput()) {
                if (c == 'Q') {
                    System.exit(0);
                } else if (c == 'P') {
                    Engine ry = new Engine();
                    ry.interactWithKeyboard();
                    break;
                }
            }

        }



    public static void main(String[] args) {
       // Createworld jerod = new Createworld(5L);
        Engine ry = new Engine();
        ry.interactWithKeyboard();
        // ry.interactWithKeyboard();

    }



    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {

        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        //String seedstring = input.substring(1, input.length() - 1);
        if (input.substring(0,1).equals("n")) {
            String seed = "";
            String movements = "";
            for (int i = 0; i < input.length(); i++) {

                    try { // I might be missing a step.
                        int num = Integer.parseInt(input.substring(i, i + 1));
                        seed += input.substring(i, i + 1);

                    } catch (Exception e) {

                        if ((input.substring(i, i+1).equals(":")) && input.substring(i, i+2).equals(":q")) {
                            System.out.println("saved");
                            saveEditor("seed", seed);
                            saveEditor("movements", movements);
                            break;
                        } else if (input.substring(i, i + 1).equals("w") || input.substring(i, i + 1).equals("s") || input.substring(i, i + 1).equals("a")
                                || input.substring(i, i + 1).equals("d")) {
                            movements += input.substring(i, i + 1);
                        }
                    }
                }


            load_and_move(seed,movements);
            ter.renderFrame(this.tiles);
            return this.tiles;
        }

        else if(input.substring(0, 1).equals("l")) {
            File seed_file = new File("seed.txt");
            File movements_file = new File("movements.txt");
            String seed = loadEditor(seed_file);
            String movements = loadEditor(movements_file);
            this.rand = new Random(Long.parseLong(seed));
            for (int i = 0; i < input.length(); i++) {
                if ((input.substring(i, i+1).equals(":")) && input.substring(i, i+2).equals(":q")) {
                    System.out.println("saved");
                    saveEditor( "seed", seed);
                    saveEditor("movements", movements);
                    break;
                } else if (input.substring(i, i + 1).equals("w") || input.substring(i, i + 1).equals("s") || input.substring(i, i + 1).equals("a")
                        || input.substring(i, i + 1).equals("d")) {
                    movements += input.substring(i, i + 1);
                }
            }

            load_and_move(seed,movements);
            ter.renderFrame(this.tiles);
            return this.tiles;
        }
        else{
            return null;
        }

    }

    private void load_and_move(String seed, String movements){

        this.directions = movements;
        this.input_seed = seed;
        Long seed_random = Long.parseLong(seed);
        this.rand = new Random(seed_random);
        Long num = Long.parseLong(seed);
        Createworld cr = new Createworld(num);
        this.tiles = cr.returnTile();
        int[] start = generate_avatar_location();
        this.position = start;
        for (int i = 0; i < movements.length(); i++) {
            char c = movements.charAt(i);
            move(c, "input");
        }

    }




    public static class Createworld {

        private static final int WIDTH = 80;
        private static final int HEIGHT = 40;
        private TETile[][] tiles;
        private TERenderer ter;
        private int numberofRooms;
        private int target;
        private Random r;
        private HashMap<Integer, Room> rooms;


        public Createworld(long seed) {

            long finalseed = seed;
            this.tiles = new TETile[WIDTH][HEIGHT];
            this.rooms = new HashMap<>();
            this.numberofRooms = 0;
            this.r = new Random(finalseed);
            this.target = r.nextInt(5) + 20;
            this.ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);


            // Initialize entire world to black
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    tiles[x][y] = Tileset.NOTHING;
                }
            }


            Room firstroom = createfirst();
            drawroom(firstroom);
            this.rooms.put(this.numberofRooms, firstroom);
            br(firstroom);
            //ter.renderFrame(tiles);


        }

        private Room createfirst() {

            int width = r.nextInt(4) + 4;
            int height = r.nextInt(4) + 3;
            int[] bottomleftstart = new int[2];
            bottomleftstart[0] = r.nextInt(35) + 20;
            bottomleftstart[1] = r.nextInt(20) + 10;
            ArrayList<String> orientation = new ArrayList<>();
            orientation.add("N"); // North side of box available
            orientation.add("S"); // South side of box available
            orientation.add("W"); // West side of box available
            orientation.add("E"); // East side of box available
            String roomnumber = "Room " + (this.numberofRooms + 1);
            Room firstroom = new Room(0, width, height, bottomleftstart,
                    false, orientation, roomnumber, null);
            return firstroom;

        }



        private void drawroom(Room ro) {

            numberofRooms += 1;
            //contain.put(r.roomnumber, r.terminated);
            int xposition = ro.bottomleftstart[0];
            int yposition = ro.bottomleftstart[1];
            int width = ro.width;
            int height = ro.height;

            for (int x = xposition; x < xposition + width; x++) {
                tiles[x][yposition] = Tileset.WALL;
            }
            for (int y = yposition; y < yposition + height; y++) {
                tiles[xposition][y] = Tileset.WALL;
            }
            for (int x = xposition; x < xposition + width; x++) {
                tiles[x][yposition + height] = Tileset.WALL;
            }
            for (int y = yposition; y < yposition + height + 1; y++) {
                tiles[xposition + width][y] = Tileset.WALL;
            }
            for (int y = yposition + 1; y < yposition + height; y++) {
                for (int x = xposition + 1; x < xposition + width; x++) {
                    tiles[x][y] = Tileset.FLOWER;
                }
            }

        }

        private void br(Room start) {
            if (this.numberofRooms < this.target) {
                if (start.orientation.size() == 0) {
                    start.terminated = true;
                    return;
                }
                int randomchoice = start.orientation.size();
                switch (randomchoice) {
                    case 0:
                        if (this.numberofRooms < this.target) {
                            br(start);
                        } else {
                            start.terminated = true;
                        }
                        break;
                    case 1:

                        createhallwayfromroom(start);

                        break;
                    case 2:
                        if (start.orientation.size() < 2) {
                            br(start);
                            break;
                        } else {
                            Room modifiedfirstroom = createhallwayfromroom(start);
                            if (modifiedfirstroom.orientation.size() == 0) {
                                modifiedfirstroom.terminated = true;
                                return;
                            }

                            createhallwayfromroom(modifiedfirstroom);
                            break;
                        }


                    case 3:
                        if (start.orientation.size() < 3) {
                            br(start);
                        } else {
                            Room modifiedfirstroom = createhallwayfromroom(start);
                            if (modifiedfirstroom.orientation.size() == 0) {
                                modifiedfirstroom.terminated = true;
                                return;
                            }
                            Room modifiedsecondroom = createhallwayfromroom(modifiedfirstroom);
                            if (modifiedsecondroom.orientation.size() == 0) {
                                modifiedsecondroom.terminated = true;
                                return;
                            }
                            createhallwayfromroom(modifiedsecondroom);
                        }
                        break;
                    case 4:
                        if (start.orientation.size() < 4) {
                            br(start);
                        } else {
                            Room modifiedfirstroom = createhallwayfromroom(start);
                            if (modifiedfirstroom.orientation.size() == 0) {
                                modifiedfirstroom.terminated = true;
                                return;
                            }
                            Room modifiedsecondroom = createhallwayfromroom(modifiedfirstroom);
                            if (modifiedsecondroom.orientation.size() == 0) {
                                modifiedsecondroom.terminated = true;
                                return;
                            }
                            Room modifiedthirdroom = createhallwayfromroom(modifiedsecondroom);
                            if (modifiedthirdroom.orientation.size() == 0) {
                                modifiedthirdroom.terminated = true;
                                return;
                            }
                            createhallwayfromroom(modifiedthirdroom);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        private void branchhallway(Hallway hall) {

            if (this.numberofRooms < this.target) {

                Room newroom = boxCreatorFromHallway(hall);
                if (newroom == null) {

                    Hallway try_again = hallWayCreatorFromHallway(hall);
                    if(try_again == null){
                       hallWayCreatorFromHallway(hall);
                    }
                }else {
                    br(newroom);
                }

            }
        }


        private Room createhallwayfromroom(Room start) {
            // if(start.orientation.size() > 0 && start != null) {
            int randomindextochoose = r.nextInt(start.orientation.size());
            String locationtoplace = start.orientation.remove(randomindextochoose);
            Room modifiedroom = start;
            Hallway hallway = buildhallway(modifiedroom, locationtoplace);
            if (hallway == null) {
                return modifiedroom;
            } else {
                branchhallway(hallway);
                return modifiedroom;
            }
        }

        // Creates a hallway from a hallway
        private Hallway hallWayCreatorFromHallway(Hallway hallway) {
            int width;
            int height;
            if (hallway.orientation.equals("up") || hallway.orientation.equals("down")) {
                width = 3;
                height = hallway.length;
            } else {
                width = hallway.length;
                height = 3;
            }

            int randside = r.nextInt(hallway.sides.size());
            String direction = hallway.sides.remove(randside);
            Hallway modifiedhallway = hallway;
            String before = hallway.previousobject;


            return hallWayCreator(modifiedhallway.bottomleft, width, height, direction, before, true);
        }


        private Hallway buildhallway(Room start, String pdirection) {
            int width = start.width;
            int height = start.height;
            int[] coordinates = start.bottomleftstart;
            int length = r.nextInt(4) + 3;
            if (pdirection.equals("N")) {
                int x = r.nextInt(width - 1) + coordinates[0];
                int[] pos = new int[2];
                pos[0] = x;
                pos[1] = coordinates[1] + height;
                ArrayList<String> available = new ArrayList<>();
                available.add("right");
                available.add("left");
                available.add("up");
                Hallway hall = new Hallway("up", length, "room", false, pos, available, "down");
                if (overlaphallway(hall)) {
                    start.orientation.remove(pdirection);
                    return null;
                } else {
                    boxCreator(pos, 2, length);
                    tiles[x + 1][pos[1]] = Tileset.FLOWER;
                    return hall;
                }
            } else if (pdirection.equals("S")) {
                int[] pos = new int[2];
                int x = r.nextInt(width - 1) + coordinates[0];
                pos[0] = x;
                pos[1] = coordinates[1] - length;
                ArrayList<String> available = new ArrayList<>();
                available.add("right");
                available.add("left");
                available.add("down");
                Hallway hall = new Hallway("down", length, "room", false, pos, available, "up");
                if (overlaphallway(hall)) {
                    start.orientation.remove(pdirection);
                    return null;
                } else {
                    boxCreator(pos, 2, length);
                    tiles[x + 1][coordinates[1]] = Tileset.FLOWER;
                    return hall;
                }
            } else if (pdirection.equals("E")) {
                int y = r.nextInt(height - 1) + coordinates[1];
                int[] pos = new int[2];
                pos[0] = coordinates[0] + width;
                pos[1] = y;
                ArrayList<String> available = new ArrayList<>();
                available.add("up");
                available.add("right");
                available.add("down");
                Hallway hall = new Hallway("right", length, "room", false, pos, available, "left");
                if (overlaphallway(hall)) {
                    start.orientation.remove(pdirection);
                    return null;
                } else {
                    boxCreator(pos, length, 2);
                    tiles[pos[0]][y + 1] = Tileset.FLOWER;
                    return hall;
                }
            } else {
                int y = r.nextInt(height - 1) + coordinates[1];
                int[] pos = new int[2];
                pos[0] = coordinates[0] - length;
                pos[1] = y;
                ArrayList<String> available = new ArrayList<>();
                available.add("up");
                available.add("left");
                available.add("down");
                Hallway hall = new Hallway("left", length, "room", false, pos, available, "right");
                if (overlaphallway(hall)) {
                    start.orientation.remove(pdirection);
                    return null;
                } else {
                    boxCreator(pos, length, 2);
                    tiles[coordinates[0]][y + 1] = Tileset.FLOWER;
                    return hall;
                }
            }
        }

        private void specialMethodOne(int[] pos, int[] previous, int length, int height) {
            boxCreator(pos, 2, length);
            tiles[pos[0] + 1][previous[1] + height] = Tileset.FLOWER;
            tiles[pos[0] + 1][previous[1] + height - 1] = Tileset.FLOWER;
        }

        private Hallway specialMethodTwo(int[] previous, int width,
                                         int height, int length, String pdirection, String before) {
            int[] pos = new int[2];
            pos[0] = r.nextInt(width - 2) + previous[0];
            pos[1] = previous[1] - length + 2;
            ArrayList<String> availablesides = arrayMaker(pdirection);
            if (before.equals("room")) {
                String previousobject = "1";
                Hallway hall = new Hallway("down", length, previousobject,
                        false, pos, availablesides, "up");
                if (overlaphallway(hall)) {
                    return null;
                } else {
                    boxCreator(pos, 2, length);
                    tiles[pos[0] + 1][previous[1] + height] = Tileset.FLOWER;
                    return hall;
                }
            } else {
                int num = Integer.parseInt(before);
                num = num + 1;
                String previousobject = Integer.toString(num);
                Hallway hall = new Hallway("down", length, previousobject,
                        false, pos, availablesides, "up");
                if (overlaphallway(hall)) {
                    return null;
                } else {
                    boxCreator(pos, 2, length);
                    tiles[pos[0] + 1][previous[1] + height] = Tileset.FLOWER;
                    return hall;
                }
            }

        }

        private Hallway specialMethodThree(int[] previous,
                                           int height, int length, String pdirection, String before) {
            int[] coordinates = new int[2];
            coordinates[0] = previous[0] - length;
            coordinates[1] = r.nextInt(height - 2) + previous[1];
            ArrayList<String> availablesides = arrayMaker(pdirection);
            if (before.equals("room")) {
                String previousobject = "1";
                Hallway hall = new Hallway("left", length, previousobject,
                        false, coordinates, availablesides, "right");
                if (overlaphallway(hall)) {
                    return null;
                } else {
                    boxCreator(coordinates, length, 2);
                    tiles[previous[0]][coordinates[1] + 1] = Tileset.FLOWER;
                    return hall;
                }
            } else {
                int num = Integer.parseInt(before);
                num = num + 1;
                String previousobject = Integer.toString(num);
                Hallway hall = new Hallway("left", length, previousobject,
                        false, coordinates, availablesides, "right");
                if (overlaphallway(hall)) {
                    return null;
                } else {
                    boxCreator(coordinates, length, 2);
                    tiles[previous[0]][coordinates[1] + 1] = Tileset.FLOWER;
                    return hall;
                }
            }
        }


        private Hallway hallWayCreator(int[] previous, int width,
                                       int height, String pdirection, String before, boolean hfh) {
            int length;
            if (hfh) {
                length = r.nextInt(3) + 2;
            } else {
                length = r.nextInt(4) + 3;
            }
            if (pdirection.equals("up")) {
                int[] pos = new int[2];
                pos[0] = r.nextInt(width - 2) + previous[0];
                pos[1] = previous[1] + height - 1;
                ArrayList<String> availablesides = arrayMaker(pdirection);
                if (before.equals("room")) {
                    String previousobject = "1";
                    Hallway hall = new Hallway("up", length, previousobject,
                            false, pos, availablesides, "down");
                    if (overlaphallway(hall)) {
                        return null;
                    } else {
                        specialMethodOne(pos, previous, length, height);
                        return hall;
                    }
                } else {
                    int num = Integer.parseInt(before);
                    num = num + 1;
                    String previousobject = Integer.toString(num);
                    Hallway hall = new Hallway("up", length, previousobject,
                            false, pos, availablesides, "down");
                    if (overlaphallway(hall)) {
                        return null;
                    } else {
                        specialMethodOne(pos, previous, length, height);
                        return hall;
                    }
                }
            } else if (pdirection.equals("down")) {
                return specialMethodTwo(previous, width, height, length, pdirection, before);
            } else if (pdirection.equals("right")) {
                int[] coordinates = new int[2];
                coordinates[0] = previous[0] + width - 1;
                coordinates[1] = r.nextInt(height - 2) + previous[1];
                ArrayList<String> availablesides = arrayMaker(pdirection);
                if (before.equals("room")) {
                    String previousobject = "1";
                    Hallway hall = new Hallway("right", length, previousobject,
                            false, coordinates, availablesides, "left");
                    if (overlaphallway(hall)) {
                        return null;
                    } else {
                        boxCreator(coordinates, length, 2);
                        tiles[previous[0] + width][coordinates[1] + 1] = Tileset.FLOWER;
                        tiles[previous[0] + width - 1][coordinates[1] + 1] = Tileset.FLOWER;
                        return hall;
                    }
                } else {
                    int num = Integer.parseInt(before);
                    num = num + 1;
                    String previousobject = Integer.toString(num);
                    Hallway hall = new Hallway("right", length, previousobject,
                            false, coordinates, availablesides, "left");
                    if (overlaphallway(hall)) {
                        return null;
                    } else {
                        boxCreator(coordinates, length, 2);
                        tiles[previous[0] + width][coordinates[1] + 1] = Tileset.FLOWER;
                        tiles[previous[0] + width - 1][coordinates[1] + 1] = Tileset.FLOWER;
                        return hall;
                    }
                }
            } else {
                return specialMethodThree(previous, height, length, pdirection, before);
            }
        }



        // Creates a box from a hallway
        private Room boxCreatorFromHallway(Hallway hallway) {
            int[] previous = hallway.bottomleft;
            int length = hallway.length;
            String pdirection = hallway.orientation;
            int width = r.nextInt(5) + 5;
            int height = r.nextInt(6) + 5;
            if (pdirection.equals("up")) {
                int[] coordinates = new int[2];
                coordinates[0] = r.nextInt(width - 2) + previous[0] + 2 - width + 1;
                coordinates[1] = previous[1] + length;
                ArrayList<String> sidesavailable = arrayMaker(pdirection);
                String numberofrooms = "Room " + (this.numberofRooms + 1);
                String received = "S";
                Room room = new Room(1, width, height, coordinates,
                        false, sidesavailable, numberofrooms, received);
                if (overlapRooms(room)) {
                    return null;
                } else {
                    this.numberofRooms += 1;
                    boxCreator(coordinates, width, height);
                    tiles[previous[0] + 1][previous[1] + length] = Tileset.FLOWER;
                    this.rooms.put(this.numberofRooms, room);
                    return room;
                }
            } else if (pdirection.equals("down")) {
                int[] coordinates = new int[2];
                coordinates[0] = r.nextInt(width - 2) + previous[0] + 2 - width + 1;
                coordinates[1] = previous[1] - height;
                ArrayList<String> sidesavailable = arrayMaker(pdirection);
                String numberofrooms = "Room " + (this.numberofRooms + 1);
                String received = "N";
                Room room = new Room(1, width, height, coordinates,
                        false, sidesavailable, numberofrooms, received);
                if (overlapRooms(room)) {
                    return null;
                } else {
                    this.numberofRooms += 1;
                    boxCreator(coordinates, width, height);
                    tiles[previous[0] + 1][previous[1]] = Tileset.FLOWER;
                    this.rooms.put(this.numberofRooms, room);
                    return room;
                }
            } else if (pdirection.equals("right")) {
                int[] coordinates = new int[2];
                coordinates[0] = previous[0] + length;
                coordinates[1] = r.nextInt(height - 2) + previous[1] + 2 - height + 1;
                ArrayList<String> sidesavailable = arrayMaker(pdirection);
                String numberofrooms = "Room" + (this.numberofRooms + 1);
                String received = "W";
                Room room = new Room(1, width, height, coordinates,
                        false, sidesavailable, numberofrooms, received);
                if (overlapRooms(room)) {
                    return null;
                } else {
                    this.numberofRooms += 1;
                    boxCreator(coordinates, width, height);
                    tiles[previous[0] + length][previous[1] + 1] = Tileset.FLOWER;
                    this.rooms.put(numberofRooms, room);
                    return room;
                }
            } else {
                int[] coordinates = new int[2];
                coordinates[0] = previous[0] - width;
                coordinates[1] = r.nextInt(height - 2) + previous[1] + 2 - height + 1;
                ArrayList<String> sidesavailable = arrayMaker(pdirection);
                String numberofrooms = "Room" + (this.numberofRooms + 1);
                String received = "E";
                Room room = new Room(1, width, height, coordinates,
                        false, sidesavailable, numberofrooms, received);
                if (overlapRooms(room)) {
                    return null;
                } else {
                    this.numberofRooms += 1;
                    boxCreator(coordinates, width, height);
                    tiles[previous[0]][previous[1] + 1] = Tileset.FLOWER;
                    this.rooms.put(this.numberofRooms, room);
                    return room;
                }
            }
        }

        private ArrayList<String> arrayMaker(String direction) {
            ArrayList<String> sidesavailable = new ArrayList<>();
            if (direction.equals("left")) {
                sidesavailable.add("N");
                sidesavailable.add("S");
                sidesavailable.add("W");
                return sidesavailable;
            } else if (direction.equals("right")) {
                sidesavailable.add("N");
                sidesavailable.add("S");
                sidesavailable.add("E");
                return sidesavailable;
            } else if (direction.equals("down")) {
                sidesavailable.add("W");
                sidesavailable.add("E");
                sidesavailable.add("S");
                return sidesavailable;
            } else {
                sidesavailable.add("N");
                sidesavailable.add("E");
                sidesavailable.add("W");
                return sidesavailable;
            }
        }
        // Calculates parameters
        // I have to go through this with you.

        // Draws t
        private void boxCreator(int[] pos, int width, int height) {
            for (int x = pos[0]; x < pos[0] + width; x++) {
                tiles[x][pos[1]] = Tileset.WALL;
            }
            for (int y = pos[1]; y < pos[1] + height; y++) {
                tiles[pos[0]][y] = Tileset.WALL;
            }
            for (int x = pos[0]; x < pos[0] + width; x++) {
                tiles[x][pos[1] + height] = Tileset.WALL;
            }
            for (int y = pos[1]; y < pos[1] + height + 1; y++) {
                tiles[pos[0] + width][y] = Tileset.WALL;
            }
            for (int y = pos[1] + 1; y < pos[1] + height; y++) {
                for (int x = pos[0] + 1; x < pos[0] + width; x++) {
                    tiles[x][y] = Tileset.FLOWER;
                }
            }
        }

        private boolean overlapRooms(Room room) {

            if (outOfBounds(room.bottomleftstart, room.width, room.height)) {
                return true;
            }
            String side = room.received;
            int[] d1 = dimensionsCalculator(room.bottomleftstart, room.width, room.height);
            if (side.equals("S")) {
                for (int x = d1[0] + 1; x < d1[2]; x++) {
                    for (int y = d1[1] + 1; y < d1[3] + 1; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            } else if (side.equals("N")) {
                for (int x = d1[0] + 1; x < d1[2]; x++) {
                    for (int y = d1[1] + 1; y < d1[3]; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            } else if (side.equals("E")) {
                for (int x = d1[0] + 1; x < d1[2]; x++) {
                    for (int y = d1[1] + 1; y < d1[3]; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            } else {
                for (int x = d1[0] + 1; x < d1[2]; x++) {
                    for (int y = d1[1] + 1; y < d1[3]; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean overlaphallway(Hallway hallway) {
            int width;
            int height;
            if (hallway.flipped.equals("up") || hallway.flipped.equals("down")) {
                width = 2;
                height = hallway.length;
            } else {
                width = hallway.length;
                height = 2;
            }
            if (outOfBounds(hallway.bottomleft, width, height)) {
                return true;
            }
            int[] d1 = dimensionsCalculator(hallway.bottomleft, width, height);
            if (hallway.orientation.equals("up")) {
                for (int x = d1[0] + 1; x < d1[2]; x++) {
                    for (int y = d1[1] + 1; y < d1[3] + 1; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            } else if (hallway.orientation.equals("down")) {
                for (int x = d1[0] + 1; x < d1[2]; x++) {
                    for (int y = d1[1]; y < d1[3]; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            } else if (hallway.orientation.equals("right")) {
                for (int x = d1[0] + 1; x < d1[2] + 1; x++) {
                    for (int y = d1[1] + 1; y < d1[3]; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            } else {
                for (int x = d1[0]; x < d1[2]; x++) {
                    for (int y = d1[1] + 1; y < d1[3]; y++) {
                        if (tiles[x][y] != Tileset.NOTHING) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }



        private boolean outOfBounds(int[] pos, int width, int height) {
            int[] dimensions = dimensionsCalculator(pos, width, height);
            return (dimensions[0] < 0 || dimensions[1] < 0
                    || dimensions[2] >= WIDTH || dimensions[3] >= HEIGHT - 5);
        }

        private int[] dimensionsCalculator(int[] p, int width, int height) {
            int[] dimensions = new int[4];
            dimensions[0] = p[0];
            dimensions[1] = p[1];
            dimensions[2] = p[0] + width;
            dimensions[3] = p[1] + height;
            return dimensions;
        }

        public TETile[][] returnTile() {
            return tiles;
        }

        private class Room {

            private int hallwaysconnected;
            private int width;
            private int height;
            private int[] bottomleftstart;
            private boolean terminated;
            private ArrayList<String> orientation;
            private String roomnumber;
            private String received;


            private Room(int hallwaysconnected, int width, int height, int[] bottomleftstart,
                         boolean terminated, ArrayList<String> orientation,
                         String roomnumber, String received) {

                this.hallwaysconnected = hallwaysconnected;
                this.width = width;
                this.height = height;
                this.bottomleftstart = bottomleftstart;
                this.terminated = terminated;
                this.orientation = orientation;
                this.roomnumber = roomnumber;
                this.received = received;

            }
        }

        private class Hallway {

            private String orientation; // up, down, left, right
            private int length; // the length of each wall
            private String previousobject;
            private boolean terminated;
            private int[] bottomleft;
            private ArrayList<String> sides;
            private String flipped;

            private Hallway(String orientation, int length, String previousobject,
                            boolean terminated, int[] bottomleft,
                            ArrayList<String> sides, String flipped) {

                this.orientation = orientation;
                this.length = length;
                this.previousobject = previousobject;
                this.terminated = terminated;
                this.bottomleft = bottomleft;
                this.sides = sides;
                this.flipped = flipped;

            }
        }


    }
}
