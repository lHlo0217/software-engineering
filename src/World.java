public class World {
    private int lx;
    private int ly;
    private int nowGeneration;
    private Cell[][] cell = new Cell[180][180];
    public World(int lx,int ly){
        this.lx = lx;
        this.ly = ly;
        nowGeneration = 0;
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                cell[i][j] = new Cell(i,j);
                cell[i][j].setIsLive(false);
            }
        }
    }

    public int getLx() {
        return lx;
    }

    public void setLx(int lx) {
        this.lx = lx;
    }

    public int getLy() {
        return ly;
    }

    public void setLy(int ly) {
        this.ly = ly;
    }
    public boolean getCellXY(int x,int y){
        return cell[x][y].getIsLive();
    }
    public void setCellXY(int x,int y,boolean is){cell[x][y].setIsLive(is);}
    public int getNowGeneration(){
        return nowGeneration;
    }
    public void randomInitCell(){
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                cell[i][j].setIsLive(Math.random()>0.5?true:false);
            }
        }
    }
    public void deleteAllCell(){
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                cell[i][j].setIsLive(false);
            }
        }
        nowGeneration = 0;
    }
    public void updateOfCell(){
        Cell[][] ctmp = new Cell[lx][ly];
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                ctmp[i][j] = new Cell(i,j);
            }
        }
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                int cnt = 0;
                for(int t = i-1;t<=i+1;t++){
                    for(int w = j-1;w<=j+1;w++){
                        if(t>=0&&t<lx&&w>=0&&w<ly&&cell[t][w].getIsLive())cnt++;
                    }
                }
                if(cell[i][j].getIsLive())cnt--;
                if(cnt==3)ctmp[i][j].setIsLive(true);
                else if(cnt==2)ctmp[i][j].setIsLive(cell[i][j].getIsLive());
                else ctmp[i][j].setIsLive(false);
            }
        }
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                cell[i][j]=ctmp[i][j];
            }
        }
        nowGeneration++;
    }
}
