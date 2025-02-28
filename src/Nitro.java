import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

public class Nitro extends JPanel implements ActionListener, KeyListener{

    // Dimensiones de la ventana
    int boardWidth = 360;
    int boardHeight = 680;

    // Imágenes necesarias
    Image backgroundImage;
    Image principalCar;
    Image aleatorieCar;
    Image fuegoImg;
    Image failed;
    Image ganaste;

    // PRINCIPAL CAR CLASS
    int carX = (boardWidth / 6) * 3;
    int carY = 600;
    int carWidth = 50;
    int carHeight = 78;

    class Car {
        int x = carX;
        int y = carY;
        int width = carWidth;
        int height = carHeight;
        Image img;

        Car(Image img) {
            this.img = img;
        }
    }

    //Random CAR 
       int rCarX = boardWidth;
       int rCarY = 0;
       int rCarWidth = 65;
       int rCarHeight = 78;

       class CarroAleatorio {
        int x;
        int y;
        int width = rCarWidth;
        int height = rCarHeight;
        Image img;
        boolean passed = false;
        int velocityY = 5; // Velocidad de caída
    
        CarroAleatorio(Image img, int startX) {
            this.img = img;
            this.x = startX;
            this.y = -height; // Empieza fuera de la pantalla arriba
        }
    
        public void move() {
            y += velocityY; // Mueve el carro hacia abajo
        }
    }

    //fuego class
       int fuegoWidth = 70;
       int fuegoHeight = 88;

       class Fuego {
        int x;
        int y;
        int width = fuegoWidth;
        int height = fuegoHeight;
        Image img;
    
        Fuego(Image img) {
            this.img = img;
        }
    }

    //GAME OVER CLASS
        int gameoWidth = 179;
        int gameoHeight = 120;
        int xOver = (boardWidth - gameoWidth) / 2;
        int yOver = (boardWidth - gameoHeight) / 2; 

       class Over {
        int x = xOver;
        int y = yOver;
        int width = gameoWidth;
        int height = gameoHeight;
        Image img;
    
        Over(Image img) {
            this.img = img;
        }
    }


    //WINNER CLASS
    int winWidth = 179;
    int winHeight = 120;
    int xWin = (boardWidth - winWidth) / 2;
    int yWin = (boardWidth - winHeight) / 2; 

    class Winner {
        int x = xWin;
        int y = yWin;
        int width = winWidth;
        int height = winHeight;
        Image img;

        Winner(Image img) {
            this.img = img;
        }
    }
    
    // Game Logic
    Car car;
    Fuego fuego;
    Over over;
    Winner winner;
    int velocityY = -3;
    JButton again;

    ArrayList<CarroAleatorio> carroAleatorios;

    Timer gameLoop;
    Timer placeRandomTimer;
    boolean gameOver = false;
    boolean youWin = false;

    Nitro() {
        // Definir dimensiones de la ventana
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
    
        // Cargar imágenes
        backgroundImage = new ImageIcon(getClass().getResource("./background.png")).getImage();
        principalCar = new ImageIcon(getClass().getResource("./bugatti.png")).getImage();
        aleatorieCar = new ImageIcon(getClass().getResource("./greencar.png")).getImage();
        fuegoImg = new ImageIcon(getClass().getResource("./xplosion.png")).getImage();
        failed = new ImageIcon(getClass().getResource("./gameO.png")).getImage();
        ganaste = new ImageIcon(getClass().getResource("./win.png")).getImage();

        over = new Over(failed);
        winner = new Winner(ganaste);
        fuego = new Fuego(fuegoImg);

        // Crear el carro
        car = new Car(principalCar);
        carroAleatorios = new ArrayList<CarroAleatorio>();
    
        // Timer para generar carros aleatorios cada 2 segundos
        placeRandomTimer = new Timer(2000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placeRandomCar();
            }
        });
        placeRandomTimer.start(); 
    
        // Timer del loop principal del juego
        gameLoop = new Timer(1000/22, this);
        gameLoop.start();
        
  /*    again =new JButton("PLAY AGAIN");
        again.setBounds(300,250,100,30);
        add(again);
        setVisible(false);
        again.addActionListener(this); */
    }
    
    public void placeRandomCar() {
        int randomX = (int) (Math.random() * (boardWidth - rCarWidth)); 
        CarroAleatorio carro = new CarroAleatorio(aleatorieCar, randomX);
        carroAleatorios.add(carro);
    }
    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(car.img, car.x, car.y, car.width, car.height, null);
    
        // Dibujar los carros aleatorios
        for (CarroAleatorio carro : carroAleatorios) {
            g.drawImage(carro.img, carro.x, carro.y, carro.width, carro.height, null);
        }
    
        // Si el juego termina, dibuja la explosión en la posición del carro principal
        if (gameOver) {
            g.drawImage(fuego.img, car.x, car.y, fuego.width, fuego.height, null);
            g.drawImage(over.img, over.x, over.y, over.width, over.height, null);
        }

        if(youWin){
            g.drawImage(winner.img, winner.x, winner.y, winner.width, winner.height, null);
        }

        //Mensaje presiona tecla para jugar

        if (gameOver || youWin) {
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press space bar to play again", 50, 100);
            g.drawString("By Sebastián",(boardWidth / 6) * 2, 600);
        }
    }
    

    public void move() {
        // Movimiento del Bugatti (jugador)
        car.y += velocityY;
        car.x = Math.max(0, Math.min(car.x, boardWidth - car.width));

        // Movimiento de los carros aleatorios
        for (CarroAleatorio carro : carroAleatorios) {
            carro.move();
    
            if (Collision(car, carro)) {  
                gameOver = true;
                break;
            }
        }
    
        // Verificar victoria correctamente
        if (car.y + car.height < 0) { 
            youWin = true;
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    
        if (gameOver || youWin) {
            placeRandomTimer.stop();
            gameLoop.stop();

        }

    }
    
    boolean Collision(Car a, CarroAleatorio b){
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
        a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
        a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
        a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }


    @Override
    public void keyPressed(KeyEvent e) {
        // Obtiene la tecla presionada
        int key = e.getKeyCode(); 

        if (key == KeyEvent.VK_RIGHT) {
            car.x += 10; 
        } else if (key == KeyEvent.VK_LEFT) {
            car.x -= 10;
        } else if (key == KeyEvent.VK_DOWN) {
            car.y += 10; 
        };
        // Vuelve a dibujar el panel con la nueva posición
        repaint(); 

        if(gameOver || youWin){

            if(key == KeyEvent.VK_SPACE  ){
                car.x = (boardWidth / 6) * 3;
                car.y = 600;
                carroAleatorios.clear();
                gameOver = false;
                youWin = false;
            
                // Volver a iniciar los timers
                placeRandomTimer.start();
                gameLoop.start();
                }
        }
    }

    //sin uso
    @Override
    public void keyTyped(KeyEvent e) {}
 
    //sin uso
    @Override
    public void keyReleased(KeyEvent e) {}
    
}