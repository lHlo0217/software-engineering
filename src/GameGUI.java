import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GameGUI extends JFrame implements ActionListener{
    private int rowNum,colNum;
    private World world;
    private JButton[][] TWorld;
    private JButton randomInit,BeginAndOver,StopAndContinue,Next;
    private boolean isRunning;
    private Thread thread;
    public GameGUI(String name,World world){
        super(name);
        this.rowNum = world.getrowNum();
        this.colNum = world.getcolNum();
        this.world = world;
    }
    public void InitGameGUI(){
        JPanel backPanel,topPanel,centerPanel;
        backPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel();
        centerPanel = new JPanel(new GridLayout(rowNum,colNum));
        this.setContentPane(backPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        backPanel.add(centerPanel,"Center");
        backPanel.add(topPanel,"North");
        TWorld = new JButton[rowNum][colNum];
        randomInit = new JButton("随机生成细胞");
        BeginAndOver = new JButton("开始游戏");
        StopAndContinue = new JButton("暂停游戏");
        Next = new JButton("下一代");
        for(int i=0;i<rowNum;i++){//生成主细胞面板
            for(int j=0;j<colNum;j++){
                TWorld[i][j] = new JButton("");
                TWorld[i][j].setBackground(Color.white);
                centerPanel.add(TWorld[i][j]);
            }
        }
        topPanel.add(randomInit);
        topPanel.add(BeginAndOver);
        topPanel.add(StopAndContinue);
        topPanel.add(Next);
        backPanel.setBackground(Color.pink);
        topPanel.setBackground(Color.pink);
        centerPanel.setBackground(Color.pink);
        int szx,szy;//界面设置大小
        szx = Math.min(1000,rowNum*2);
        szx = Math.max(1000,rowNum*2);
        szy = Math.min(colNum*2,1000);
        szy = Math.max(colNum*2,1000);
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
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
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
                            Thread.sleep(100);
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
                            Thread.sleep(100);
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
        else{//按钮监听
            if(isRunning==false) {
                for (int i = 0; i < rowNum; i++) {
                    for (int j = 0; j < colNum; j++) {
                        if (e.getSource() == TWorld[i][j]) {
                            boolean cnt = world.getCelrowNumY(i, j);
                            if (cnt == true) cnt = false;
                            else cnt = true;
                            world.setCelrowNumY(i, j, cnt);
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
    }
    public void showWorld(){//显示当前状态
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
                if(world.getCelrowNumY(i,j)){
                    TWorld[i][j].setBackground(Color.GREEN);
                }
                else{
                    TWorld[i][j].setBackground(Color.RED);
                }
            }
        }
    }
}
