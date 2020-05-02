package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class Main extends Application {
    private int nrButtonClicks=0;
    private int lastClicked=-1;
    private int preLastClicked=-1;
    Button[] buttonArray;
    boolean isSwapped =false;
    boolean solved = false;


    @Override
    public void start(Stage stage) throws FileNotFoundException{


        Image image0 = new Image(new FileInputStream("E:\\Java\\yoda\\Resources\\bebeyoda_400x400.jpg"));
        ImageView imageView0 = new ImageView(image0);

        //for 3x3 set puzzle
//        Image image1 = new Image(new FileInputStream("Resources/blank.png"));
//        Image image2 = new Image(new FileInputStream("Resources/2.jpg"));
//        Image image3 = new Image(new FileInputStream("Resources/3.jpg"));
//        Image image4 = new Image(new FileInputStream("Resources/4.jpg"));
//        Image image5 = new Image(new FileInputStream("Resources/5.jpg"));
//        Image image6 = new Image(new FileInputStream("Resources/6.jpg"));
//        Image image7 = new Image(new FileInputStream("Resources/7.jpg"));
//        Image image8 = new Image(new FileInputStream("Resources/8.jpg"));
//        Image image9 = new Image(new FileInputStream("Resources/9.jpg"));

        //for n x n puzzle
        Image primaryImageToSplit = new Image(new FileInputStream("E:\\Java\\yoda\\Resources\\bebeyoda_400x400.jpg"));
        Image blankImageToSplit = new Image(new FileInputStream("E:\\Java\\yoda\\Resources\\black_400x400.jpg"));
        /**
         * image dimension is set for 400x400 for our baby yoda image
         * in case of another image, resize accordingly
         */
        int imageDimension = 400;
        Scanner scan = new Scanner(System.in);
        //Input from user
        int tileCount = scan.nextInt();
        scan.close();
        //starting to crop image into tileCount x tileCount images
        PixelReader reader = primaryImageToSplit.getPixelReader();
        WritableImage[] imageArray = new WritableImage[tileCount*tileCount];
        int tileDimension = imageDimension/tileCount;
        int ct = 0;
        for(int i=0;i<tileCount;i++){
            for(int j=0;j<tileCount;j++){
                WritableImage newImage = new WritableImage(reader, i*tileDimension, j*tileDimension, tileDimension, tileDimension);
                imageArray[ct] = newImage;
                ct++;
            }
        }
        //replace last image with black tile of desired dimension
        PixelReader readerBlank = blankImageToSplit.getPixelReader();
        WritableImage blankImage = new WritableImage(readerBlank, 0, 0, tileDimension, tileDimension);
        imageArray[tileCount*tileCount - 1] = blankImage;

        //button init for n x n puzzle
        buttonArray = new Button[tileCount*tileCount];

        for(int i=0;i<tileCount*tileCount;i++){
            buttonArray[i] = new Button();
        }

        //button init for 3x3 puzzle
//        Button buton9 = new Button();
//        Button buton8 = new Button();
//        Button buton7 = new Button();
//        Button buton6 = new Button();
//        Button buton5 = new Button();
//        Button buton4 = new Button();
//        Button buton3 = new Button();
//        Button buton2 = new Button();
//        Button buton1 = new Button();

        //base image for the 3x3 puzzle
        imageView0.setX(20);
        imageView0.setY(50);
        imageView0.setFitHeight(200);
        imageView0.setFitWidth(200);
        imageView0.setPreserveRatio(true);

        Group root = new Group();
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("A mirobolant puzzle game");
        stage.setScene(scene);

        GridPane gridPane = new GridPane();
        /**
         * variable tileCount represents the fragmentation of the puzzle
         * argument for RandomPuzzlematrix constructor is the dimension of the puzzle
         * puzzle will be randomised into a SOLVABLE puzzle
         */
        //int tileCount = 3; //TODO tilecount should be input from user
        RandomPuzzlematrix puzzle = new RandomPuzzlematrix(tileCount);
        puzzle.initTiles();
        //odd number of inversions for odd-width puzzles ==> SOLVABLE
        //if not odd ==> unsolvable ==> need to swap tiles to make it solvable
        if(tileCount % 2 == 1){
            if(puzzle.sumInversions() % 2 != 0){
                puzzle.hasEmptyPieceAtBeginning();
            }
        }
        //even-width puzzle condition to be solvable
        else{
            if((puzzle.sumInversions() + tileCount - puzzle.distanceFromEmptyToLastRow()) % 2 != 0){
                puzzle.hasEmptyPieceAtBeginning();
            }
        }

        //HashMap for knowing which are the right coordinates of a certain button
        HashMap<Tile, Integer> correspondingPlace = new HashMap<Tile, Integer>();

        //hashmap for 3x3 puzzle
//        correspondingPlace.put(new Tile(0,0), 9);
//        correspondingPlace.put(new Tile(0,1), 8);
//        correspondingPlace.put(new Tile(0,2), 7);
//        correspondingPlace.put(new Tile(1,0), 6);
//        correspondingPlace.put(new Tile(1,1), 5);
//        correspondingPlace.put(new Tile(1,2), 4);
//        correspondingPlace.put(new Tile(2,0), 3);
//        correspondingPlace.put(new Tile(2,1), 2);
//        correspondingPlace.put(new Tile(2,2), 1);

        //hashmap for n x n puzzle
        for(int i=0;i<tileCount;i++){
            for(int j =0;j<tileCount;j++){
                correspondingPlace.put(new Tile(j,i), i*tileCount + j);
            }
        }


        for(int i=0;i<tileCount;i++){
            for(int j=0;j<tileCount;j++){
                Tile temp = new Tile(0,0);
                temp = puzzle.puzzleMatrix[i][j];
                Integer indexOfButton = correspondingPlace.get(temp);
                //gridPane.add(correspondingPlace.get(temp), i, j, 1, 1);
                //add switch statement for 3 x 3 puzzle
                ImageView imv = new ImageView(imageArray[indexOfButton]);
                buttonArray[indexOfButton].setGraphic(imv);
                buttonArray[indexOfButton].setPadding(Insets.EMPTY); //imaginea acopera tot butonul
                gridPane.add(buttonArray[indexOfButton], j, i, 1, 1);

            }
        }

//        switch (indexOfButton){
//            case 9:
//                ImageView imv9 = new ImageView(image9);
//                buton9.setGraphic(imv9);
//                gridPane.add(buton9, j, i, 1, 1);
//                break;
//            case 8:
//                ImageView imv8 = new ImageView(image8);
//                buton8.setGraphic(imv8);
//                gridPane.add(buton8, j, i, 1, 1);
//                break;
//            case 7:
//                ImageView imv7 = new ImageView(image7);
//                buton7.setGraphic(imv7);
//                gridPane.add(buton7, j, i, 1, 1);
//                break;
//            case 6:
//                ImageView imv6 = new ImageView(image6);
//                buton6.setGraphic(imv6);
//                gridPane.add(buton6, j, i, 1, 1);
//                break;
//            case 5:
//                ImageView imv5 = new ImageView(image5);
//                buton5.setGraphic(imv5);
//                gridPane.add(buton5, j, i, 1, 1);
//                break;
//            case 4:
//                ImageView imv4 = new ImageView(image4);
//                buton4.setGraphic(imv4);
//                gridPane.add(buton4, j, i, 1, 1);
//                break;
//            case 3:
//                ImageView imv3 = new ImageView(image3);
//                buton3.setGraphic(imv3);
//                gridPane.add(buton3, j, i, 1, 1);
//                break;
//            case 2:
//                ImageView imv2 = new ImageView(image2);
//                buton2.setGraphic(imv2);
//                gridPane.add(buton2, j, i, 1, 1);
//                break;
//            case 1:
//                ImageView imv1 = new ImageView(image1);
//                buton1.setGraphic(imv1);
//                gridPane.add(buton1, j, i, 1, 1);
//                break;
//        }

        //gridPane.setHgap(5);
        //gridPane.setVgap(5);

        root.getChildren().add(gridPane);
        //use for 3 x 3 puzzle
//        vector_butoane[0]=buton1; vector_butoane[1]=buton2; vector_butoane[2]=buton3; vector_butoane[3]=buton4;vector_butoane[4]=buton5;
//        vector_butoane[5]=buton6;vector_butoane[6]=buton7;vector_butoane[7]=buton8;vector_butoane[8]=buton9;

        for (int i = 0; i < tileCount*tileCount; i++) { //creeare EventListener
            final int butonIndex = i;
            buttonArray[i].setOnAction(e -> {
                nrButtonClicks++;
                preLastClicked = lastClicked;
                lastClicked = butonIndex;
                System.out.println(nrButtonClicks + " " + preLastClicked + " " + lastClicked);
            });
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        swapTest(puzzle);
                        if(!solved && puzzle.isSolved()){
                            solved = true;
                            solvedPuzzleScreen(stage);
                        }
                    }
                };
                while(true){
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){}
                    Platform.runLater(updater);
                }
            }
        });
        thread.start();

        stage.show();
    }



    public void swap(Node n1, Node n2, RandomPuzzlematrix puzzle) {
        puzzle.swapTiles(GridPane.getRowIndex(n1), GridPane.getColumnIndex(n1), GridPane.getRowIndex(n2), GridPane.getColumnIndex(n2));

        Integer temp = GridPane.getRowIndex(n1);
        GridPane.setRowIndex(n1, GridPane.getRowIndex(n2));
        GridPane.setRowIndex(n2, temp);

        temp = GridPane.getColumnIndex(n1);
        GridPane.setColumnIndex(n1, GridPane.getColumnIndex(n2));
        GridPane.setColumnIndex(n2, temp);
    }
    public boolean testIfAdjacent(Node n1, Node n2){
        Integer testRow = GridPane.getRowIndex(n1);
        Integer testColumn = GridPane.getColumnIndex(n1);
        /**
         * comment return pt test
         * cu return true schimbam orice tile cu ala negru
         */
        return ((testRow == GridPane.getRowIndex(n2) - 1) && (testColumn == GridPane.getColumnIndex(n2))) ||
                ((testRow == GridPane.getRowIndex(n2) + 1) && (testColumn == GridPane.getColumnIndex(n2))) ||
                ((testRow == GridPane.getRowIndex(n2)) && (testColumn == GridPane.getColumnIndex(n2) - 1)) ||
                ((testRow == GridPane.getRowIndex(n2)) && (testColumn == GridPane.getColumnIndex(n2) + 1));

        //return true;
    }

    public void swapTest(RandomPuzzlematrix puzzle){
        if (nrButtonClicks % 2 == 0 && nrButtonClicks > 0 && !isSwapped) {
            if((preLastClicked == puzzle.tileCount*puzzle.tileCount - 1 || lastClicked == puzzle.tileCount*puzzle.tileCount - 1) &&
                    testIfAdjacent(buttonArray[preLastClicked], buttonArray[lastClicked])) {
                swap(buttonArray[preLastClicked], buttonArray[lastClicked], puzzle);
            }
            isSwapped =true;
        }
        if(nrButtonClicks%2!=0){
            isSwapped =false;
        }
    }
    public void solvedPuzzleScreen(Stage stage){
        System.out.println("SOLVED!");
        final Stage end = new Stage();
        end.initModality(Modality.APPLICATION_MODAL);
        end.initOwner(stage);
        VBox endVbox = new VBox(20);
        endVbox.getChildren().add(new Text("You've solved the puzzle!"));
        Scene endScene = new Scene(endVbox, 300, 200);
        end.setScene(endScene);
        end.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
