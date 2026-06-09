import java.util.ArrayList;
import java.util.List;

public class MatrixApp {
    private Matrix matrix;

    public MatrixApp() {

        List<Position> obstacles = new ArrayList<>();

        // Bloque central superior
        obstacles.add(new Position(8, 17));
        obstacles.add(new Position(8, 18));
        obstacles.add(new Position(8, 19));
        obstacles.add(new Position(8, 20));
        obstacles.add(new Position(8, 21));

        obstacles.add(new Position(9, 17));
        obstacles.add(new Position(10, 17));
        obstacles.add(new Position(11, 17));

        obstacles.add(new Position(11, 18));
        obstacles.add(new Position(11, 19));
        obstacles.add(new Position(11, 20));
        obstacles.add(new Position(11, 21));

        // Bloque central inferior
        obstacles.add(new Position(14, 22));
        obstacles.add(new Position(14, 23));
        obstacles.add(new Position(14, 24));
        obstacles.add(new Position(14, 25));
        obstacles.add(new Position(14, 26));

        obstacles.add(new Position(15, 22));
        obstacles.add(new Position(16, 22));
        obstacles.add(new Position(17, 22));

        // Pared vertical izquierda
        obstacles.add(new Position(3, 5));
        obstacles.add(new Position(4, 5));
        obstacles.add(new Position(5, 5));
        obstacles.add(new Position(6, 5));
        obstacles.add(new Position(7, 5));
        obstacles.add(new Position(8, 5));
        obstacles.add(new Position(9, 5));

        // Pared vertical derecha
        obstacles.add(new Position(10, 31));
        obstacles.add(new Position(11, 31));
        obstacles.add(new Position(12, 31));
        obstacles.add(new Position(13, 31));
        obstacles.add(new Position(14, 31));
        obstacles.add(new Position(15, 31));
        obstacles.add(new Position(16, 31));

        // Pared horizontal superior
        obstacles.add(new Position(4, 12));
        obstacles.add(new Position(4, 13));
        obstacles.add(new Position(4, 14));
        obstacles.add(new Position(4, 15));
        obstacles.add(new Position(4, 16));

        // Pared horizontal inferior
        obstacles.add(new Position(16, 7));
        obstacles.add(new Position(16, 8));
        obstacles.add(new Position(16, 9));
        obstacles.add(new Position(16, 10));
        obstacles.add(new Position(16, 11));
        obstacles.add(new Position(16, 12));

        // Obstáculos sueltos zona izquierda
        obstacles.add(new Position(2, 10));
        obstacles.add(new Position(3, 10));
        obstacles.add(new Position(5, 12));

        obstacles.add(new Position(12, 3));
        obstacles.add(new Position(13, 3));
        obstacles.add(new Position(14, 3));

        // Obstáculos sueltos zona derecha
        obstacles.add(new Position(3, 29));
        obstacles.add(new Position(4, 29));
        obstacles.add(new Position(5, 29));

        obstacles.add(new Position(6, 34));
        obstacles.add(new Position(7, 34));
        obstacles.add(new Position(8, 34));

        obstacles.add(new Position(17, 34));
        obstacles.add(new Position(17, 35));
        obstacles.add(new Position(17, 36));

        // Obstáculos cerca del centro, pero sin cerrar completamente
        obstacles.add(new Position(6, 22));
        obstacles.add(new Position(6, 23));
        obstacles.add(new Position(6, 24));

        obstacles.add(new Position(13, 15));
        obstacles.add(new Position(14, 15));
        obstacles.add(new Position(15, 15));

        List<Position> telephones = new ArrayList<>();
        telephones.add(new Position(0, 0));
        telephones.add(new Position(19, 39));
        telephones.add(new Position(0, 39));

        List<Position> neos = new ArrayList<>();
        neos.add(new Position(9, 15));

        List<Position> agents = new ArrayList<>();
        agents.add(new Position(0, 2));
        agents.add(new Position(0, 18));
        agents.add(new Position(10, 10));
        agents.add(new Position(18, 18));
        agents.add(new Position(15, 35));

        List<Double> agentSpeeds = new ArrayList<>();
        agentSpeeds.add(2.0);

        this.matrix = new Matrix(
                20,
                40,
                obstacles,
                telephones,
                neos,
                agents,
                2.0,
                agentSpeeds
        );
    }

        public void start(){
        int renderDelay = 200;
        clearConsole();
        System.out.println(matrix);

        matrix.start();

        while (!matrix.isFinished()) {
            try {
                Thread.sleep(renderDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            clearConsole();
            System.out.println(matrix);
        }

        clearConsole();
        System.out.println(matrix);
        System.out.println("Simulation finished.");
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
