package com.svalero.juegodamas;

import java.io.Serializable;

public class GuardarPartida implements Serializable {
        private final String[][] tableroGuardado;
        private final int contadorBlancas;
        private final int contadorNegras;

        public GuardarPartida(String [][] tableroGuardado,int contadorBlancas, int contadorNegras){
            this.tableroGuardado=tableroGuardado;
            this.contadorBlancas=contadorBlancas;
            this.contadorNegras=contadorNegras;
        }


    public String[][] getTableroGuardado(){return tableroGuardado;}

    public int getContadorBlancas() {
        return contadorBlancas;
    }

    public int getContadorNegras() {
        return contadorNegras;
    }
}
