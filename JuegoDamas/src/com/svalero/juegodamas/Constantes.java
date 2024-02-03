package com.svalero.juegodamas;


    public class Constantes {

        static Colors colors = new Colors();
        public static final String FICHA_JUGADOR1 = colors.colored_groundText(" O ",234,181);
        public static final String FICHA_JUGADOR2 =  colors.colored_groundText(" X ",234,181);

        public static final String CASILLA_VACIA  =  colors.colored_groundText(" L ",234,181);
        public static final String CASILLA_BLANCA =  colors.colored_ground("   ",230);

        public static final String BLANCAS = "J1";
        public static final String NEGRAS = "J2";
        public static final String MENSAJE_MATE= "\u001B[47m BUEN MATE\u001B[0m";

        public static final String MENSAJE_FINAL_GANADOR = "\u001B[47m FELICIDADES, ¡HAS GANADO LA PARTIDA¡\u001B[0m";
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLANCO_FONDO = "\u001B[41m";
        public static final String ANSI_LETRA_VERDE = "\u001B[32m";


    }


