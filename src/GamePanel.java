import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import java.security.PublicKey;

public class GamePanel extends JPanel implements ActionListener {

    static final  int SCREEN_WIDTH = 600; // Alt çizgi genişliği
    static final  int SCREEN_HEIGHT = 600; // Yan çizgi Genişliği
    static final int UNIT_SIZE = 25;  // oyundaki nesnelerin boyutunun büyüklüğü
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // ekranan kaç nesne sığar hesaplamak için
    static final int DELAY = 75; // zamanlayıcı gecıkme sayısı ne kadar yuksekse oyun o kadar yavaş
    final int x[] = new int[GAME_UNITS]; // yılanın vucut kordinatlarnı tutucak
    final int y[] = new int[GAME_UNITS]; // yılanın vucut kordinatlarını tutucak
    int bodyParts = 6; // yılanın kaç vucut parçasıyla başlıyacağını yazarız
    int applesEaten;
    int appleX; // elmanın kordınatları
    int appleY; // elmanın kordinatları
    char direction = 'R'; // yılanın sağ yöne giderek başlamasını sağlar
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT)); //tercih edilen boyutu ayarla
        this.setBackground(Color.black); // arka plan rengi
        this.setFocusable(true);  // anlamadım
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running){
            /*  // basta Burası bize yardımcı olsun diye ızgaraları çizdi
            for (int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT); // yukarıdan aşağıya  çizgi çizmemizi sağladı
                g.drawLine(0,i*UNIT_SIZE,SCREEN_HEIGHT,i*UNIT_SIZE); // sağdan sola çizgi çizmemizi sağladı
            }*/
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            for (int i = 0;i<bodyParts;i++){
                if (i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }else{
                    g.setColor(new Color(45,180,0));
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ applesEaten,(SCREEN_WIDTH -metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());

        }else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;



    }
    public void move(){
        for (int i = bodyParts;i>0;i--){
            x[i] = x[i-1];  // yılanı oluştururuz
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple(){
        if (((x[0]) == appleX)&&(y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions(){
        // kafanın gövde ile çarpışıp çarpişmadıgını kontrol eder
        for (int i = bodyParts;i>0;i--){
            if ((x[0] == x[i])&&(y[0] == y[i])){
                running = false;
            }
        }
        // kafanın sol sınıra değip değmediğini kontrol eder
        if (x[0] <0){
            running = false;
        }
        // kafanın sağ sınıra değip değmediğini kontrol eder
        if (x[0] >SCREEN_WIDTH){
            running = false;
        }
        // kafanın üst sınıra değip değmediğini kontrol eder
        if (y[0] < 0){
            running = false;
        }
        // kafanın alt sınıra değip değmediğini kontrol eder
        if (y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if (!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten,(SCREEN_WIDTH -metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());


        // Game Over yazarız

        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH -metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);








    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
}
