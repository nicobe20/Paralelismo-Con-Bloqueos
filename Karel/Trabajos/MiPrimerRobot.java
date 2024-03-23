import kareltherobot.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerRobot implements Directions {

    public static Minar mina = new Minar();
    public static Extraer extraer = new Extraer();
    public static Transportar transporte = new Transportar();

    public static class Workers extends Thread {
        private Object mainObject; // Objeto principal (Minero, Tren, Extractor)
        private Racer worker;

        public Workers(Object mainObject, int street, int avenue, Direction direction, int beepers, Color color) {
            this.mainObject = mainObject;
            this.worker = new Racer(street, avenue, direction, beepers, color, mainObject); // Creamos el Racer con el
                                                                                            // objeto principal
        }

        public void run() {
            worker.race(); // Ejecutamos el movimiento del Racer
        }
    }

    public static void createRobots(String[] args) {
        // Definición de valores por defecto
        int mineros = 2;
        int trenes = 2;
        int extractores = 2;

        // Procesamiento de argumentos de línea de comandos
        if (args.length > 0) {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equals("-m")) {
                    mineros = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-t")) {
                    trenes = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-e")) {
                    extractores = Integer.parseInt(args[i + 1]);
                }
            }
        }

        // Crear mineros
        for (int z = 0; z < mineros; z++) {
            Minero nuevoMinero = new Minero(7, 1, false);
            mina.getMineros().add(nuevoMinero);

            Workers mineroBot = new Workers(nuevoMinero, 7, 1, South, 0, Color.DARK_GRAY);
            mineroBot.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // // Crear trenes
        if(trenes>16){
            trenes = 16;
        }
        for (int y = 0; y < trenes; y++) {
            Tren nuevoTren = new Tren(7, 1);
            transporte.getTrenes().add(nuevoTren);
            Workers trenesBot = new Workers(nuevoTren, 7, 1, South, 0, Color.CYAN);
            trenesBot.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // // Crear extractores
        if(extractores>4){
            extractores = 4;
        }
        for (int x = 0; x < extractores; x++) {
            
            Extractor nuevoTren = new Extractor(7, 1, x == 0 ? true : false);
            extraer.getExtractores().add(nuevoTren);
            Workers extractoresBot = new Workers(nuevoTren, 7, 1, South, 0, Color.RED);
            extractoresBot.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        
        }
    
    }

    public static void main(String[] args) {
        World.readWorld("Mundo.kwld");
        World.setVisible(true);
        createRobots(args);
    }
}

class Racer extends Robot implements Runnable {
    private Minero minero;
    private Tren tren;
    private Extractor extractor;
    private Color robotColor;

    public Racer(int street, int avenue, Direction direction, int beepers, Color color, Object mainObject) {
        super(street, avenue, direction, beepers, color);
        robotColor = color;

        if (mainObject instanceof Minero) {
            this.minero = (Minero) mainObject; // Asignamos el Minero asociado al Racer
        } else if (mainObject instanceof Tren) {
            this.tren = (Tren) mainObject; // Asignamos el Minero asociado al Racer
        } else if (mainObject instanceof Extractor) {
            this.extractor = (Extractor) mainObject; // Asignamos el Minero asociado al Racer
        }

        World.setupThread(this);
        World.setDelay(30);
    }

    public void race() {
        while (true) {
            if (robotColor.equals(Color.DARK_GRAY)) {
                if (minero.getStreet() == 11 && minero.getAvenue() == 8 && facingSouth()) {
                    turnLeft();
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 14) {
                    if (facingEast()) {
                        mineroPickBeeper();
                    } else if (facingNorth()) {
                        while (!facingEast()) {
                            turnLeft();
                        }
                        mineroPickBeeper();

                    } else if (facingWest()) {
                        while (MiPrimerRobot.mina.getPuertoMina()) {
                            ponerEnEspera();
                        }
                    }
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 13) {
                    if (facingWest()) {
                        MiPrimerRobot.mina.truePuertoMina(); // Decimos que el puerto mina esta "ocupado"

                        MiPrimerRobot.mina.updateMina(); // Decimos que la mina esta "desocupada"
                        if (anyBeepersInBeeperBag()) {
                            for (int i = 0; i < MiPrimerRobot.mina.getCapacidadMinero(); i++) {
                                putBeeper();
                                MiPrimerRobot.mina.incrementBeepersPuertoMina();
                            }
                        }
                        ubicarMineroPuntoEspera(minero);
                    }
                }

                if (minero.getStreet() == 11 && minero.getAvenue() == 13 &&
                        MiPrimerRobot.mina.getMina()) {
                    ubicarMineroPuntoEspera(minero);
                }

                if (frontIsClear()) {
                    move();
                    updateMineroPosition(minero);
                } else {
                    turnLeft();
                }
            } else if (robotColor.equals(Color.CYAN)) {

                if (tren.getStreet() == 11 && tren.getAvenue() == 8 && facingSouth()) {
                    turnLeft();
                }

                if (tren.getStreet() == 11 && tren.getAvenue() == 12 && facingEast()) {
                    while (MiPrimerRobot.mina.getPuertoMina() == true
                            || MiPrimerRobot.mina.getBeepersPuertoMina() < MiPrimerRobot.transporte
                                    .getCapacidadTren()) {
                        ponerEnEspera();
                    }
                    move();
                    updateTrenPosition(tren);
                }

                if (tren.getStreet() == 11 && tren.getAvenue() == 13) {
                    MiPrimerRobot.mina.truePuertoMina();
                    if (nextToABeeper()) {
                        for (int i = 0; i < MiPrimerRobot.transporte.getCapacidadTren(); i++) {
                            pickBeeper();
                            MiPrimerRobot.mina.decrementBeepersPuertoMina();
                        }
                        girarIzquierda(3);
                        MiPrimerRobot.mina.falsePuertoMina();
                    }
                }

                if (tren.getStreet() == 6 && tren.getAvenue() == 13) {
                    turnLeft();
                }

                if (tren.getStreet() == 2 && tren.getAvenue() == 3) {

                    while (MiPrimerRobot.extraer.getPuertoExtractor()) {
                        ponerEnEspera();
                    }
                    ponerEnEspera();
                    while (MiPrimerRobot.extraer.getPuertoExtractor()) {
                        ponerEnEspera();
                    }

                }

                if (tren.getStreet() == 1 && tren.getAvenue() == 3 && facingEast()) {
                    if (anyBeepersInBeeperBag()) {
                        MiPrimerRobot.extraer.ocuparPuertoExtractor(); // decimos que puerto extractor esta ocupado
                        for (int i = 0; i < MiPrimerRobot.transporte.getCapacidadTren(); i++) {
                            putBeeper();
                            MiPrimerRobot.extraer.incrementBeepersPuertoExtractor();
                        }
                    }
                    MiPrimerRobot.extraer.desocuparPuertoExtractor();
                }

                verificarYEsperarSiFrenteOcupado(11);
                verificarYEsperarSiFrenteOcupado(1);

                if (tren.getAvenue() == 8 && facingNorth()) {
                    while (true) {
                        boolean frenteOcupado = false;
                        int streetCurrentTrain = tren.getStreet(); // calle = 4

                        for (Tren trenActual : MiPrimerRobot.transporte.getTrenes()) {
                            int street = trenActual.getStreet();
                            if (street > streetCurrentTrain && (street - 1) == streetCurrentTrain && facingNorth()
                                    && trenActual.getAvenue() == 8) {
                                frenteOcupado = true;
                                ponerEnEspera();
                                break;
                            }
                            if (streetCurrentTrain == 5 && streetCurrentTrain + 2 == trenActual.getStreet()
                                    && facingNorth() && trenActual.getAvenue() == 8) {
                                frenteOcupado = true;
                                ponerEnEspera();
                                break;
                            }
                        }
                        if (!frenteOcupado) {
                            break;
                        }
                    }
                }

                if (frontIsClear()) {
                    move();
                    updateTrenPosition(tren);
                } else {
                    turnLeft();
                }
            } else if (robotColor.equals(Color.RED)) {
                int posicion = MiPrimerRobot.extraer.getExtractores().indexOf(extractor);
                int cantidadExtractores = MiPrimerRobot.extraer.getExtractores().size();
                Extractor ultimoExtractor = MiPrimerRobot.extraer.getExtractores().get(cantidadExtractores - 1);

                if (posicion == 0) {
                    if (extractor.getStreet() == 1 && extractor.getAvenue() == 2 && facingEast()) {
                        while (MiPrimerRobot.extraer.getBeepersPuertoExtractor() < MiPrimerRobot.extraer
                                .getCapacidadExtractor()
                                || MiPrimerRobot.extraer.getPuertoExtractor() || !extractor.getAutorizacion()) {
                            ponerEnEspera();
                        }
                    }

                    if (extractor.getStreet() == 1 && extractor.getAvenue() == 3 && nextToABeeper()) {
                        MiPrimerRobot.extraer.ocuparPuertoExtractor();
                        for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                            if (nextToABeeper()) {
                                pickBeeper();
                                MiPrimerRobot.extraer.decrementBeepersPuertoExtractor();
                            }
                        }
                        girarIzquierda(2);
                        MiPrimerRobot.extraer.desocuparPuertoExtractor();
                    }

                    if (extractor != ultimoExtractor) {
                        if (extractor.getStreet() == 1 && extractor.getAvenue() == 1 && facingWest()) {
                            while (!facingEast()) {
                                turnLeft();
                            }

                            for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                                if (anyBeepersInBeeperBag()) {
                                    putBeeper();
                                }
                            }

                            pasarAutorizacion(posicion);

                        }
                    }

                }
                if (posicion > 0 && posicion <= cantidadExtractores && !anyBeepersInBeeperBag()) {
                    if (posicion == 1) {
                        if (extractor.getStreet() == posicion + 1) {
                            while (true) {
                                if (!extractor.getAutorizacion()) {
                                    ponerEnEspera();
                                } else {
                                    recogerYLlevarBeepers(ultimoExtractor, posicion);

                                    break;

                                }
                            }
                        }
                    } else if (posicion * 2 == extractor.getStreet()) {
                        while (true) {
                            if (!extractor.getAutorizacion()) {
                                ponerEnEspera();
                            } else if (extractor != ultimoExtractor) {
                                recogerYLlevarBeepers(ultimoExtractor, posicion);

                                break;

                            } else {
                                move();
                                updateExtractorPosition(extractor);

                                for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                                    if (nextToABeeper()) {
                                        pickBeeper();
                                    }
                                }
                                extractor.updateAutorizacion();

                                girarIzquierda(2);

                                break;
                            }
                        }
                    }
                }

                if (extractor == ultimoExtractor) {
                    if (extractor.getAvenue() == 1 && extractor.getStreet() == 7 &&
                            anyBeepersInBeeperBag()) {
                        girarIzquierda(3);
                    }

                    if (extractor.getStreet() == 7 && extractor.getAvenue() == 2 &&
                            facingSouth()) {
                        if (posicion != 0) {
                            Extractor primerExtractor = MiPrimerRobot.extraer.getExtractores().get(0);
                            primerExtractor.updateAutorizacion();
                            MiPrimerRobot.extraer.getExtractores().set(0, primerExtractor);
                        }
                        girarIzquierda(3);
                    }
                    
                    // implementar cambio de bodegas.
                    if (extractor.getStreet() == 10 && extractor.getAvenue() == 2  && MiPrimerRobot.extraer.getMinas() < 3000) {
                        for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                            putBeeper();
                            MiPrimerRobot.extraer.incrementMinas();
                        }
                        girarIzquierda(2);
                    }
                     
                    if (extractor.getStreet() == 11 && extractor.getAvenue() == 2  && MiPrimerRobot.extraer.getMinas() <=6000) {
                        for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                            putBeeper();
                            MiPrimerRobot.extraer.incrementMinas();
                        }
                        girarIzquierda(2);
                    }
                    
                    if (extractor.getStreet() == 12 && extractor.getAvenue() == 2  && MiPrimerRobot.extraer.getMinas() <= 9000) {
                        for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                            putBeeper();
                            MiPrimerRobot.extraer.incrementMinas();
                        }
                        girarIzquierda(2);
                    }
                    if (extractor.getStreet() == 13 && extractor.getAvenue() == 2  && MiPrimerRobot.extraer.getMinas() <=12000) {
                        for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                            putBeeper();
                            MiPrimerRobot.extraer.incrementMinas();
                        }
                        girarIzquierda(2);
                    }
                     


                }

                if (extractor.getAvenue() == 1 && extractor.getStreet() == 1 && facingWest()) {
                    girarIzquierda(3);
                }

                if (frontIsClear()) {
                    move();
                    updateExtractorPosition(extractor);
                } else {
                    turnLeft();
                }
            }
        }
    }

    private void recogerYLlevarBeepers(Extractor ultimoExtractor, int posicion) {
        move();
        updateExtractorPosition(extractor);

        for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
            if (nextToABeeper()) {
                pickBeeper();
            }
        }

        girarIzquierda(2);

        if (extractor != ultimoExtractor) {
            move();
            updateExtractorPosition(extractor);
            move();
            updateExtractorPosition(extractor);

            for (int i = 0; i < MiPrimerRobot.extraer.getCapacidadExtractor(); i++) {
                if (anyBeepersInBeeperBag()) {
                    putBeeper();
                }
            }

            girarIzquierda(2);

        }

        pasarAutorizacion(posicion);
    }

    private void girarIzquierda(int cantidadGiros) {
        for (int i = 0; i < cantidadGiros; i++) {
            turnLeft();
        }
    }

    private void pasarAutorizacion(int posicion) {
        extractor.updateAutorizacion();
        if (posicion + 1 < MiPrimerRobot.extraer.getExtractores().size()) {
            Extractor siguienteExtractor = MiPrimerRobot.extraer.getExtractores().get(posicion + 1);
            siguienteExtractor.updateAutorizacion();
            MiPrimerRobot.extraer.getExtractores().set(posicion + 1, siguienteExtractor);
        }
    }

    // Método para actualizar la posición de un minero
    private void updateMineroPosition(Minero minero) {
        if (facingNorth()) {
            minero.incrementStreet();
        } else if (facingSouth()) {
            minero.decreaseStreet();
        } else if (facingEast()) {
            minero.incrementAvenue();
        } else if (facingWest()) {
            minero.decreaseAvenue();
        }
    }

    private void updateTrenPosition(Tren tren) {
        if (facingNorth()) {
            tren.incrementStreet();
        } else if (facingSouth()) {
            tren.decreaseStreet();
        } else if (facingEast()) {
            tren.incrementAvenue();
        } else if (facingWest()) {
            tren.decreaseAvenue();
        }
    }

    private void updateExtractorPosition(Extractor extractor) {
        if (facingNorth()) {
            extractor.incrementStreet();
        } else if (facingSouth()) {
            extractor.decreaseStreet();
        } else if (facingEast()) {
            extractor.incrementAvenue();
        } else if (facingWest()) {
            extractor.decreaseAvenue();
        }
    }

    private void mineroPickBeeper() {
        MiPrimerRobot.mina.updateMina(); // Decimos que la mina esta "Ocupada"
        if (nextToABeeper()) {
            for (int i = 0; i < MiPrimerRobot.mina.getCapacidadMinero(); i++) {
                pickBeeper();
            }
        }
    }

    private void ponerEnEspera() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ubicarMineroPuntoEspera(Minero minero) {
        while (!facingSouth()) {
            turnLeft();
        }
        MiPrimerRobot.mina.falsePuertoMina(); // Decimos que el puerto mina esta "desocupada"
        move();
        updateMineroPosition(minero);
        turnLeft();
        int posicion = MiPrimerRobot.mina.getMineros().indexOf(minero);
        if (posicion != -1 && posicion > 1) {
            if (minero.getStreet() == 10 && minero.getAvenue() == 13 && facingEast()) {
                turnOff();
            }
        }
        move();
        updateMineroPosition(minero);
        turnLeft();

        while (MiPrimerRobot.mina.getMina()) {
            ponerEnEspera();
        }
    }

   


    public void verificarYEsperarSiFrenteOcupado(int streetDeseado) {
        if (tren.getStreet() == streetDeseado && facingEast()) {
            while (true) {
                boolean frenteOcupado = false;
                int avenueCurrentTrain = tren.getAvenue();
                for (Tren trenActual : MiPrimerRobot.transporte.getTrenes()) {
                    int avenue = trenActual.getAvenue();
                    if (avenue > avenueCurrentTrain && avenue == (avenueCurrentTrain + 1) && facingEast()
                            && trenActual.getStreet() == streetDeseado) {
                        frenteOcupado = true;
                        ponerEnEspera();
                        break;
                    }
                }
                if (!frenteOcupado) {
                    break;
                }
            }
        }
    }

    public void run() {
        race();
    }

}
