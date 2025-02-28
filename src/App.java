import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        //values de la ventana (se llaman mas adelante)
        int boardWidth = 360;
        int boardHeight = 1916;
    
        //title de la ventana (esquina superior izquierda)
        JFrame frame = new JFrame("Nitro Race");
        //se visualiza la ventana 
        //frame.setVisible(true);
        //setear valores de la ventana con las variables definidas mas arriba
		frame.setSize(boardWidth, boardHeight);
        //por defecto se posiciona en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        //no permitir que se edite el tamaño de la ventana en el momento de ejecución
        frame.setResizable(false);
        //terminar procesos automaticamente una vez el user cierre la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //instanciando la ventana del juego 
        Nitro nitro = new Nitro();

        frame.add(nitro);
        //no incluir la franja superior del titulo de la ventana dentro de las dimensiones dadas 
        frame.pack();
        nitro.requestFocus();
        frame.setVisible(true);
        

   
    }
}
