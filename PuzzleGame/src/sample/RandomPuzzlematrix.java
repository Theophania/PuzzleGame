package sample;

class Tile{
    int x;
    int y;
    Tile(int _x,int _y){
        x = _x;
        y = _y;
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime*result + x + y;
        return result;
    }
    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if(x != other.x && y != other.y)
            return false;
        return true;
    }
}

public class RandomPuzzlematrix {
    int tileCount;
    Tile[][] puzzleMatrix;

    RandomPuzzlematrix(int newTileCount){
        tileCount = newTileCount;
        puzzleMatrix = new Tile[tileCount][tileCount];
        for(int i=0;i<tileCount;i++){
            for(int j=0;j<tileCount;j++){
                puzzleMatrix[i][j] = new Tile(i,j);
            }
        }
    }

    public void swapTiles(int i, int j, int k, int l){
        Tile temp;
        temp = puzzleMatrix[i][j];
        puzzleMatrix[i][j] = puzzleMatrix[k][l];
        puzzleMatrix[k][l] = temp;
    }

    public void initTiles(){
        int i = tileCount*tileCount-1;
        while(i>0){
            int j = (int)Math.floor(Math.random()*i);
            int xi = i % tileCount;
            int yi = (int)Math.floor(i/tileCount);
            int xj = j % tileCount;
            int yj = (int)Math.floor(j/tileCount);
            swapTiles(xi, yi, xj, yj);
            --i;
        }
    }

    public int countInversions(int i, int j){
        int inversions = 0;
        //int tileNum = j*tileCount+i;
        int tileNum = i*tileCount+j;
        int lastTile = tileCount*tileCount;
        int tileValue = puzzleMatrix[i][j].y * tileCount + puzzleMatrix[i][j].x;
        for(int q = tileNum+1;q<lastTile;++q){
            int k = q%tileCount;
            int l = (int)Math.floor(q/tileCount);

            //int compValue = puzzleMatrix[k][l].y*tileCount + puzzleMatrix[k][l].x;
            int compValue = puzzleMatrix[k][l].x*tileCount + puzzleMatrix[k][l].y;
            if(tileValue>compValue && tileValue != (lastTile-1)){
                ++inversions;
            }
        }

        return inversions;
    }

    public int sumInversions(){
        int inversions = 0;
        for(int i=0;i<tileCount;i++){
            for(int j=0;j<tileCount;j++){
                inversions += countInversions(i, j);
            }
        }
        return inversions;
    }

    public void hasEmptyPieceAtBeginning(){
        if((puzzleMatrix[0][0].x == tileCount-1 && puzzleMatrix[0][0].y == tileCount-1) ||
                (puzzleMatrix[0][1].x == tileCount-1 && puzzleMatrix[0][1].y == tileCount-1))
            swapTiles(tileCount-2, tileCount-1, tileCount-1, tileCount-1);
        else{
            swapTiles(0,0,1,0);
        }
    }

    public boolean isSolved(){
        for(int i=0;i<tileCount;i++){
            for(int j=0;j<tileCount;j++){
                if(puzzleMatrix[i][j].x != i || puzzleMatrix[i][j].y != j)
                    return false;
            }
        }
        return true;
    }

    public int distanceFromEmptyToLastRow(){
        for(int i=0;i<tileCount;i++){
            for(int j=0;j<tileCount;j++){
                if(puzzleMatrix[i][j].x == tileCount-1 && puzzleMatrix[i][j].y == tileCount-1)
                    return i+1;
            }
        }
        return 0;
    }
}
