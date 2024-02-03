package com.svalero.juegodamas;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import static com.svalero.juegodamas.Constantes.*;
import static javax.sound.sampled.AudioSystem.*;


public class JuegoDamas {
    private String[][] tableroDamas;
    private String nombreJugador1;
    private String nombreJugador2;
    public JuegoDamas(){}
    boolean juegoacabado;
    boolean busquedaFicha;
    boolean busquedaCorrectaBlancas;
    boolean busquedaCorrectaNegras;
    boolean movimientoCorrectoBlancas;
    boolean movimientoCorrectoNegras;
    int coorNumericaI=0;
    int coorNumericaJ=0;
    int coorNumericaMovJ=0;
    int coorNumericaMovI=0;
    int contadorBlancasComidas;
    int contadorNegrasComidas;
    String respuestaFin;
    private String ruta;
    private GuardarPartida guardado1;

    Scanner teclado = new Scanner(System.in);
    //expresi'on regular para validar que el (x,y) introducido est'a dentro del rango y son numeros separados por ","
    String regex =  "[0-7],[0-7]";

    //FUNCIONALIDADES AÑADIDAS A LOS REQUISITOS OBLIGATORIOS
    //1.AÑADE CONTADOR DE FICHAS PARA CADA UNO DE LOS JUAGADORES
    //2.CUANDO UN JUGADOR COME TODAS LAS FICHAS SE GANA LA PARTIDA
    //3.AÑADE ALGO DE DISEÑO COLOR, TABLERO, FICHAS Y MENSAJES
    //4.AÑADE SONIDOS AL JUEGO
    //5.AÑADE FUNCIONALIDAD DE CARGAR/GUARDAR PARTIDA

    public void jugar(){
        juegoacabado = false;
        //Sonido de inicio
        ruta = "mario.wav";
        ejecutaSonido(ruta);
        //
        pedirNombreJugador();
        /////inicializarTablero();  AQUI ATENCION IMPLEMENTACION DE INICIALIZACION DE TABLERO


        if (new File("partida.dat").exists()) {
            System.out.println();
            System.out.print("Hola "+ANSI_LETRA_VERDE+nombreJugador1+" y "+nombreJugador2+ANSI_RESET);
            System.out.print(" ¿Quereis cargar la partida guardada (S/N)? ");
            String respuestaCargar = teclado.nextLine();
            if (respuestaCargar.equalsIgnoreCase("S")) {
                //Carga la partida grabada, tablero y variables de juego
                cargarJuego();
                //partidaGrabada = false;
                representarTablero();
            } else {
                inicializarTablero();
            }
        } else {
            inicializarTablero();
        }

        do {
             movimientoCorrectoBlancas = false;
             movimientoCorrectoNegras = false;
             movimiento(BLANCAS);
             if (movimientoCorrectoBlancas) {
                 System.out.print(nombreJugador1+ "¿Quiere Finalizar el juego(F), Grabar partida(G) o Continuar Jugando(Pulsar cualquier otra tecla)?: ");
                 respuestaFin = teclado.nextLine();
                 switch (respuestaFin.toUpperCase()) {
                     case "F":
                         System.out.println("El juego ha acabado");
                         juegoacabado = true;
                         //Sonido de bomba, No quiere seguir fumando(Fumar mata)
                         ruta = "bomb.wav";
                         ejecutaSonido(ruta);
                     case "G":
                         juegoacabado = true;
                         guardarJuego();
                         //Sonido de Grabar Partida
                         ruta = "rizz.wav";
                         ejecutaSonido(ruta);
                         break;

                     default:
                         System.out.println("!Continuamos Jugando!");
                 }
             }
             //
            if (contadorNegrasComidas == 15){
                     System.out.println(nombreJugador1 + " " + MENSAJE_FINAL_GANADOR);
                     juegoacabado = true;
                     //Sonido de ganador
                     ruta = "wow.wav";
                     ejecutaSonido(ruta);
                     //
            }
            if(!juegoacabado){
                     do {
                         movimiento(NEGRAS);
                         if (movimientoCorrectoNegras) {
                             System.out.print(nombreJugador1+ "¿Quiere Finalizar el juego(F), Grabar partida(G) o Continuar Jugando(Pulsar cualquier otra tecla)?: ");
                             respuestaFin = teclado.nextLine();
                             switch (respuestaFin.toUpperCase()) {
                                 case "F":
                                     System.out.println("El juego ha acabado");
                                     juegoacabado = true;
                                     //Sonido de bomba, No quiere seguir fumando(Fumar mata)
                                     ruta = "bomb.wav";
                                     ejecutaSonido(ruta);
                                 case "G":
                                     juegoacabado = true;
                                     guardarJuego();
                                     //Sonido de Grabar Partida
                                     ruta = "rizz.wav";
                                     ejecutaSonido(ruta);
                                     break;

                                 default:
                                     System.out.println("!Continuamos Jugando!");
                             }
                             if (contadorBlancasComidas == 15){
                                 System.out.println(nombreJugador2 + " " + MENSAJE_FINAL_GANADOR);
                                 juegoacabado = true;
                                 //Sonido de ganador
                                 ruta = "wow.wav";
                                 ejecutaSonido(ruta);
                                 //
                             }
                         }
                     }while (!movimientoCorrectoNegras);
            }
        } while (!juegoacabado);
    }

    private void inicializarTablero(){

        tableroDamas = new String[8][8];
        //Inicializamos tablero
        for (int i = 0; i < tableroDamas.length; i++) {
            for (int j = 0; j < tableroDamas[i].length; j++) {
                // Colocar las piezas en las posiciones iniciales
                if ((i + j) % 2 == 1) {
                    if (i < 3) {
                        tableroDamas[i][j] = FICHA_JUGADOR2;  // Representa las piezas blancas
                    } else if (i > 4) {
                        tableroDamas[i][j] = FICHA_JUGADOR1 ;  // Representa las piezas negras
                    } else {
                        tableroDamas[i][j] = CASILLA_VACIA;  // Casillas vacías
                    }
                } else {
                    tableroDamas[i][j] = CASILLA_BLANCA; // Casillas blancas
                }
            }
        }

        contadorBlancasComidas=0;
        contadorNegrasComidas=0;

        representarTablero();
    }

    private void representarTablero(){

        System.out.println("Tablero de " + nombreJugador1 + " y " + nombreJugador2);
        System.out.println();
        int i = 0;
        for (String[] fila : tableroDamas) {
            for (String casilla : fila) {
                System.out.print(casilla);
            }
            System.out.print(" " + i);
            i++;
            System.out.println();
        }
        for(i=0;i<tableroDamas.length;i++){
            System.out.print(" " + i + " ");
        }
        System.out.println();
        System.out.println("Numero de Fichas comidas de " + nombreJugador1 + " : " +ANSI_BLANCO_FONDO+contadorNegrasComidas+ANSI_RESET);
        System.out.println("Numero de Fichas comidas de " + nombreJugador2 + " : " +ANSI_BLANCO_FONDO+contadorBlancasComidas+ANSI_RESET);
    }

    private void pedirNombreJugador(){
        System.out.print("Nombre Jugador 1: ");
        nombreJugador1 = teclado.nextLine();
        System.out.print("Nombre Jugador 2: ");
        nombreJugador2 = teclado.nextLine();

    }
    private void movimiento(String jugador){
    //Jugador1 es blancas
        int coorNumericaI=0;
        int coorNumericaJ=0;

        String coorFicha;
        String coorMovimiento;
        if (jugador.equals(BLANCAS)){
            System.out.println(" ");
            System.out.print(nombreJugador1 + " " + FICHA_JUGADOR1+ " ¿Que ficha quiere mover en formato(x,y)?");
            coorFicha=teclado.nextLine();
            busquedaFicha=buscarFichaTableroBlancas(coorFicha);
            if (busquedaFicha){
                System.out.print("Movimiento en formato (x,y)?");
                coorMovimiento=teclado.nextLine();
                if(!coorFicha.equals(coorMovimiento)){
                    procesarmovimientoBlancas(coorMovimiento);
                }

            } else{
                System.out.println("La posicion introducida no es correcta, vuelva a intertarlo");
                //Sonido de fallo
                ruta = "fail.wav";
                ejecutaSonido(ruta);
                //
            }
        } else {
            System.out.println(" ");
            System.out.print(nombreJugador2 + " " + FICHA_JUGADOR2 +  "¿Que ficha quiere mover en formato(x,y)?");
            coorFicha=teclado.nextLine();
            busquedaFicha=buscarFichaTableroNegras(coorFicha);
            if (busquedaFicha){
                System.out.print("Movimiento en formato (x,y)?");
                coorMovimiento=teclado.nextLine();
                if(!coorFicha.equals(coorMovimiento)){
                    procesarmovimientoNegras(coorMovimiento);
                }

            } else{
                System.out.println("La posicion introducida no es correcta, vuelva a intertarlo");
                //Sonido de fallo
                ruta = "fail.wav";
                ejecutaSonido(ruta);
                //
            }
        }
    }

    private boolean buscarFichaTableroBlancas(String ficha){
        busquedaCorrectaBlancas = false;
        if(ficha.matches(regex))
        {
            String[] partes = ficha.split(",");
            coorNumericaI = Integer.parseInt(partes[1]);
            coorNumericaJ = Integer.parseInt(partes[0]);
            //
            if (tableroDamas[coorNumericaI][coorNumericaJ].equals(FICHA_JUGADOR1)) {
                busquedaCorrectaBlancas = true;
            }
        }
        return busquedaCorrectaBlancas;
    }
    private boolean buscarFichaTableroNegras(String ficha){
        busquedaCorrectaNegras = false;

        if(ficha.matches(regex))
        {
            String [] partes = ficha.split(",");
            coorNumericaI=Integer.parseInt(partes[1]);
            coorNumericaJ=Integer.parseInt(partes[0]);

            if (tableroDamas[coorNumericaI][coorNumericaJ].equals(FICHA_JUGADOR2)) {
                busquedaCorrectaNegras = true;
            }


        }

        return busquedaCorrectaNegras;
    }
    private void procesarmovimientoBlancas(String coordenaMovimiento){
        movimientoCorrectoBlancas = false;
        if(coordenaMovimiento.matches(regex)){
            String [] partes = coordenaMovimiento.split(",");
            coorNumericaMovI=Integer.parseInt(partes[1]);
            coorNumericaMovJ=Integer.parseInt(partes[0]);
            if (tableroDamas[coorNumericaMovI][coorNumericaMovJ].equals(CASILLA_VACIA)) {
                int difMovI = coorNumericaMovI-coorNumericaI;
                if(Math.abs(difMovI) == 1){
                    tableroDamas[coorNumericaMovI][coorNumericaMovJ] = FICHA_JUGADOR1;
                    tableroDamas[coorNumericaI][coorNumericaJ] = CASILLA_VACIA;
                    movimientoCorrectoBlancas =true;
                }else if(Math.abs(difMovI)==2){
                    if(coorNumericaMovJ >  coorNumericaJ){
                        tableroDamas[coorNumericaMovI + 1][coorNumericaMovJ - 1] = CASILLA_VACIA;
                    }else {
                        tableroDamas[coorNumericaMovI + 1][coorNumericaMovJ + 1] = CASILLA_VACIA;
                    }

                    tableroDamas[coorNumericaI][coorNumericaJ] = CASILLA_VACIA;
                    tableroDamas[coorNumericaMovI][coorNumericaMovJ] = FICHA_JUGADOR1;
                    System.out.println(MENSAJE_MATE);
                    contadorNegrasComidas++;
                    movimientoCorrectoBlancas =true;
                    //Sonido de Mate
                    ruta = "sonic.wav";
                    ejecutaSonido(ruta);
                    //
                } else{
                      System.out.println("Ese movimiento no se puede hacer");
                    //Sonido de fallo
                    ruta = "fail.wav";
                    ejecutaSonido(ruta);
                    //
                }
            } else{

                System.out.println("Ese movimiento no se puede hacer");
                //Sonido de fallo
                ruta = "fail.wav";
                ejecutaSonido(ruta);
                //

            }
        }else{
            System.out.println("Formato incorrecto, intentelo otra vez");
            //Sonido de fallo
            ruta = "fail.wav";
            ejecutaSonido(ruta);
            //
        }

        representarTablero();
    }

    private void procesarmovimientoNegras(String coordenaMovimiento){
        movimientoCorrectoNegras = false;
        if(coordenaMovimiento.matches(regex)) {
            String[] partes = coordenaMovimiento.split(",");
            coorNumericaMovI = Integer.parseInt(partes[1]);
            coorNumericaMovJ = Integer.parseInt(partes[0]);
            if (tableroDamas[coorNumericaMovI][coorNumericaMovJ].equals(CASILLA_VACIA)) {
                int difMovI = coorNumericaMovI-coorNumericaI;
                if(Math.abs(difMovI) == 1){

                    tableroDamas[coorNumericaMovI][coorNumericaMovJ] = FICHA_JUGADOR2;
                    tableroDamas[coorNumericaI][coorNumericaJ] = CASILLA_VACIA;

                    movimientoCorrectoNegras =true;

                }else if(Math.abs(difMovI)==2){

                    if(coorNumericaMovJ >  coorNumericaJ){
                        tableroDamas[coorNumericaMovI - 1 ][coorNumericaMovJ - 1] = CASILLA_VACIA;
                    }else {
                        tableroDamas[coorNumericaMovI - 1][coorNumericaMovJ + 1] = CASILLA_VACIA;
                    }

                    tableroDamas[coorNumericaI][coorNumericaJ] = CASILLA_VACIA;
                    tableroDamas[coorNumericaMovI][coorNumericaMovJ] = FICHA_JUGADOR2;

                    System.out.println(MENSAJE_MATE);
                    contadorNegrasComidas++;

                    movimientoCorrectoNegras =true;
                    //Sonido de Mate
                    ruta = "sonic.wav";
                    ejecutaSonido(ruta);
                    //

                } else{
                    System.out.println("Ese movimiento no se puede hacer");
                    //Sonido de fallo
                    ruta = "fail.wav";
                    ejecutaSonido(ruta);
                    //
                }
            } else{

                System.out.println("Ese movimiento no se puede hacer");
                //Sonido de fallo
                ruta = "fail.wav";
                ejecutaSonido(ruta);
                //
            }
        }else{
            System.out.println("Formato incorrecto, intentelo otra vez");
            //Sonido de fallo
            ruta = "fail.wav";
            ejecutaSonido(ruta);
            //
        }
        representarTablero();
    }
    private void ejecutaSonido (String ruta) {
        try {
            File rutaMusica = new File (ruta);
            if (rutaMusica.exists()) {
                AudioInputStream audioImput = getAudioInputStream(rutaMusica);
                Clip sonido= getClip();
                sonido.open(audioImput);
                sonido.start();
            }else {
                System.out.println("El archivo no se pudo encontrar");
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    private void guardarJuego (){
        guardado1 = new GuardarPartida(tableroDamas,contadorBlancasComidas,contadorNegrasComidas);

        ObjectOutputStream serializador = null;
        try {
            serializador = new ObjectOutputStream(new FileOutputStream("partida.dat"));
            serializador.writeObject(guardado1);
        } catch (FileNotFoundException fnfe) {
            System.out.println("No se ha podido crear el fichero");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("No se ha podido escribir en el fichero");
        } finally {
            try {
                serializador.close();
            } catch (IOException ioe) {
                System.out.println("Se ha producido un error al guardar");
            }
        }
    }
    private void cargarJuego (){
        ObjectInputStream deserializador = null;
        try {
            deserializador = new ObjectInputStream(new FileInputStream("partida.dat"));
            guardado1 = (GuardarPartida) deserializador.readObject();
            deserializador.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("El fichero partida.dat no existe");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("El fichero no tiene el formato correcto");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("No se ha podido leer el fichero");
        }
        tableroDamas=guardado1.getTableroGuardado();
        contadorBlancasComidas= guardado1.getContadorBlancas();
        contadorNegrasComidas= guardado1.getContadorNegras();


    }
}
