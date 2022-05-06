import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GameGUI extends JFrame implements ActionListener{
    private int lx,ly;
    private World world;
    private JButton[][] TWorld;
    private JLabel NowGeneration;
    private JButton randomInit,BeginAndOver,StopAndContinue,Next,Exit;
    private boolean isRunning;
    private Thread thread;
    public GameGUI(String name,World world){
        super(name);
        this.lx = world.getLx();
        this.ly = world.getLy();
        this.world = world;
        InitGameGUI();
    }
    public void InitGameGUI(){
        JPanel backPanel,bottomPanel,centerPanel;
        backPanel = new JPanel(new BorderLayout());
        bottomPanel = new JPanel();
        centerPanel = new JPanel(new GridLayout(lx,ly));
        this.setContentPane(backPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        backPanel.add(centerPanel,"Center");
        backPanel.add(bottomPanel,"South");
        TWorld = new JButton[lx][ly];
        NowGeneration = new JLabel("当前代数： 0");
        randomInit = new JButton("随机生成细胞");
        BeginAndOver = new JButton("开始游戏");
        StopAndContinue = new JButton("暂停游戏");
        Next = new JButton("下一代");
        Exit = new JButton("退出");
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                TWorld[i][j] = new JButton("");
                TWorld[i][j].setBackground(Color.white);
                centerPanel.add(TWorld[i][j]);
            }
        }
        bottomPanel.add(randomInit);
        bottomPanel.add(BeginAndOver);
        bottomPanel.add(StopAndContinue);
        bottomPanel.add(Next);
        bottomPanel.add(NowGeneration);
        bottomPanel.add(Exit);

        int szx,szy;
        szx = Math.min(800,(lx+1)*20);
        szy = Math.min(ly*20,1500);
        szy = Math.max(ly*20,500);
        this.setSize(szy,szx);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        randomInit.addActionListener(this);
        BeginAndOver.addActionListener(this);
        StopAndContinue.addActionListener(this);
        Next.addActionListener(this);
        Exit.addActionListener(this);
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                TWorld[i][j].addActionListener(this);
            }
        }
        showWorld();        //初始化
    }


    public void actionPerformed(ActionEvent e){
        if(e.getSource()==randomInit&&BeginAndOver.getText()=="开始游戏"){
            world.randomInitCell();
            showWorld();
            isRunning = false;
            thread = null;
            randomInit.setText("重新随机");
        }
        else if(e.getSource()==BeginAndOver&&
        BeginAndOver.getText()=="开始游戏"){
            isRunning = true;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(isRunning){
                        Change();
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e1){
                            e1.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
            BeginAndOver.setText("结束游戏");
        }
        else if(e.getSource()==BeginAndOver&&BeginAndOver.getText()=="结束游戏"){
            isRunning = false;
            thread = null;
            world.deleteAllCell();
            showWorld();
            BeginAndOver.setText("开始游戏");
            StopAndContinue.setText("暂停游戏");
            randomInit.setText("随机生成细胞");
            NowGeneration.setText("当前代数： 0");
        }
        else if(e.getSource()==StopAndContinue&&StopAndContinue.getText()=="暂停游戏"){
            isRunning = false;
            thread = null;
            StopAndContinue.setText("继续游戏");
        }
        else if(e.getSource()==StopAndContinue&&StopAndContinue.getText()=="继续游戏"){
            isRunning = true;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning){
                        Change();
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e1){
                            e1.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
            StopAndContinue.setText("暂停游戏");
        }
        else if(e.getSource()==Next&&StopAndContinue.getText()=="继续游戏"){
            Change();
            isRunning = false;
            thread = null;
        }
        else if(e.getSource()==Exit){
            isRunning = false;
            thread = null;
            this.dispose();
            System.exit(0);
        }
        else{//按钮监听
            if(isRunning==false) {
                for (int i = 0; i < lx; i++) {
                    for (int j = 0; j < ly; j++) {
                        if (e.getSource() == TWorld[i][j]) {
                            boolean cnt = world.getCellXY(i, j);
                            if (cnt == true) cnt = false;
                            else cnt = true;
                            world.setCellXY(i, j, cnt);
                            break;
                        }
                    }
                }
                showWorld();
            }
        }
    }
    public void Change(){
        world.updateOfCell();
        showWorld();
        NowGeneration.setText("当前代数: "+world.getNowGeneration());
    }
    public void showWorld(){
        for(int i=0;i<lx;i++){
            for(int j=0;j<ly;j++){
                if(world.getCellXY(i,j)){
                    TWorld[i][j].setBackground(Color.GREEN);
                }
                else{
                    TWorld[i][j].setBackground(Color.RED);
                }
            }
        }
    }
}
