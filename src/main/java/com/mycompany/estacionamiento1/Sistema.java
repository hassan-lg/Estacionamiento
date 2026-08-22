package com.mycompany.estacionamiento1;
/**
 *
 * @author ACER
 */
import java.util.Scanner;
import java.util.Random;

public class Sistema {
    // Declaraciones
    static final int FILAS = 10;
    static final int COLUMNAS = 10;
    static final char BORDE = '=';
    static final char LIBRE = 'L';
    static final char OCUPADO = 'O';
    static final double TARIFA = 10.00;
    static final int TOTAL_PERIMETRO = 2 * FILAS + 2 * COLUMNAS - 4;

    // 2. VARIABLES Y MATRICES GLOBALES
    static final char[][] tablero = new char[FILAS][COLUMNAS];
    static final String[][] placas = new String[FILAS][COLUMNAS];  
    static double totalIngresos = 0.0; 
    
    static int filaEntrada, columnaEntrada;
    static int filaSalida, columnaSalida;
    
    
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Random rnd = new Random();
        
        llenarTablero();
        
        int[] posEntrada = generarPosicionEnBorde(rnd);
        filaEntrada = posEntrada[0];
        columnaEntrada = posEntrada[1];
        
        int[] posSalida;
        do {
            posSalida = generarPosicionEnBorde(rnd);
            filaSalida = posSalida[0];
            columnaSalida = posSalida[1];
        } while (filaEntrada == filaSalida && columnaEntrada == columnaSalida); 
        
        tablero[filaEntrada][columnaEntrada] = 'E';
        tablero[filaSalida][columnaSalida] = 'S';

        boolean continuarEjecucion = true;
        
        do {
            mostrarMenu();
            
            if (!teclado.hasNextInt()) {
                System.out.println("Se debe ingresar un número entre 1 y 7");
                teclado.next(); 
                continue;
            }
            
            int opcion = teclado.nextInt();
            
            switch (opcion) {
                case 1:
                    System.out.println("--- INGRESO DE VEHÍCULO ---");
                
                
                String placaInput = "";
                boolean placaValida = false; 
                
                
                while (!placaValida) {
                    System.out.print("Ingrese la placa de su vehiculo (Formato P123ABC): ");
                    placaInput = teclado.next().trim();
                    
                    if (!esFormatoPlacaValido(placaInput)) {
                        System.out.println("Error: El dato ingresado no es un formato válido de placa.");
                        continue; 
                    }
                    
                    if (esPlacaDuplicada(placaInput)) {
                        System.out.println("Error: Ese vehículo ya está registrado en el sistema.");
                        continue;
                    }
                    
                    placaValida = true;
                }
                
                if (placaValida) {
                    
                    if (!hayEspacioLibre(tablero)) {
                        System.out.println("Lo sentimos, el estacionamiento está completamente lleno.");
                        break;
                    }

                    // Posición aleatoria del carro en el parqueo
                    int filaAleatoria;
                    int columnaAleatoria;
                    boolean puestoEncontrado = false;

                    do {
                        filaAleatoria = rnd.nextInt(FILAS);      
                        columnaAleatoria = rnd.nextInt(COLUMNAS);  

                        
                        if (tablero[filaAleatoria][columnaAleatoria] == LIBRE) {
                            puestoEncontrado = true;
                        }
                    } while (!puestoEncontrado); 

                    
                    double monto = solicitarPago(teclado);
                    double cambio = monto - TARIFA;
                    System.out.println("Pago recibido: Q" + monto + ". Su cambio es: Q" + cambio);

                    
                    tablero[filaAleatoria][columnaAleatoria] = OCUPADO;
                    placas[filaAleatoria][columnaAleatoria] = placaInput;
                    totalIngresos += TARIFA; 

                    System.out.println("¡Éxito! Vehículo [" + placaInput + "] asignado aleatoriamente a la Fila " + filaAleatoria + ", Columna " + columnaAleatoria + ".");
                }
                break;
                    
                case 3:
                    System.out.println("--- MOSTRAR EL ESTACIONAMIENTO ---");
                    imprimirTablero();
                    break;
                    
                case 4:
                    System.out.println("--- BUSCAR POR PLACA ---");
                    System.out.print("Ingrese la placa a buscar: ");
                    String placaBuscar = teclado.next().trim();
                    
                    int[] ubicacion = buscarPosicionPlaca(placaBuscar);
                    if (ubicacion[0] == -1) {
                        System.out.println(placaBuscar + " -> no encontrado");
                    } else {
                        System.out.println(placaBuscar + " -> encontrado en posicion Fila: " + ubicacion[0] + ", Columna: " + ubicacion[1]);
                    }
                    break;
                    
                case 5:
                    System.out.println("--- CÁLCULO DE RUTA MÁS CORTA ---");
                    int[] filaPerimetro = new int[TOTAL_PERIMETRO];
                    int[] columnaPerimetro = new int[TOTAL_PERIMETRO];
                    construirPerimetro(filaPerimetro, columnaPerimetro);
                    calcularYMostrarRuta(filaPerimetro, columnaPerimetro, filaEntrada, columnaEntrada, filaSalida, columnaSalida);
                    break;
                    
                case 6:
                    System.out.println("===INGRESOS===");
                    System.out.println("TARIFA DE Q 10.00");
                    System.out.println("TOTAL RECAUDADO: Q" + totalIngresos);
                    break;
                    
                case 7:
                    System.out.println("Salir del sistema");
                    continuarEjecucion = false;
                    break;
                    
                default:
                    System.out.println("Debe escoger un número del 1 al 7");
            }
        } while (continuarEjecucion);
        teclado.close();
    }

    
    static void mostrarMenu() {
        System.out.println();
        System.out.println("===== SISTEMA DE ESTACIONAMIENTO =====");
        System.out.println("1. Ingresar vehiculo");
        System.out.println("2. Retirar vehiculo");
        System.out.println("3. Mostrar estacionamiento");
        System.out.println("4. Buscar vehiculo por placa");
        System.out.println("5. Mostrar ruta mas corta entre entrada y salida");
        System.out.println("6. Mostrar ingresos");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    static void llenarTablero(){
        for (int fila = 0; fila < FILAS; fila++){
            for(int columna = 0; columna < COLUMNAS; columna++){
                tablero[fila][columna] = esBorde(fila, columna) ? BORDE : LIBRE;
            }
        }
    }    

    static boolean esBorde(int fila, int columna) {
        return fila == 0 || fila == FILAS - 1 || columna == 0 || columna == COLUMNAS - 1;
    }    
    
    static void imprimirTablero() {
        System.out.print("  ");
        for (int c = 0; c < COLUMNAS; c++) System.out.print(c + " ");
        System.out.println();
        for (int i = 0; i < FILAS; i++){
            System.out.print(i + " ");
            for(int j = 0; j < COLUMNAS; j++){
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }

    static boolean esFormatoPlacaValido(String placa) {
        if (placa.length() != 7) return false;
        if (placa.charAt(0) != 'P') return false;
        for (int i = 1; i <= 3; i++) {
            if (!Character.isDigit(placa.charAt(i))) return false;
        }
        for (int i = 4; i <= 6; i++) {
            char c = placa.charAt(i);
            if (!Character.isLetter(c) || !Character.isUpperCase(c)) return false;
        }
        return true;
    }

    static boolean esPlacaDuplicada(String placa) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (placas[i][j] != null && placas[i][j].equalsIgnoreCase(placa)) return true;
            }
        }
        return false;
    }

    static boolean hayEspacioLibre(char[][] espacios) {
        for (int fila = 0; fila < espacios.length; fila++) {
            for (int columna = 0; columna < espacios[fila].length; columna++) {
                if (espacios[fila][columna] == LIBRE) {
                    return true;
                }
            }
        }
        return false;
    }
    static boolean esPosicionLibre(char[][] espacios, int fila, int columna) {
        return espacios[fila][columna] == LIBRE;
    }


    static double solicitarPago(Scanner teclado) {
        double montoEntregado;
        do {
            System.out.print("Tarifa: Q" + TARIFA + ". Ingrese el monto entregado: Q");
            montoEntregado = teclado.nextDouble();
            
            if (montoEntregado < 0) {
                System.out.println("El monto no puede ser negativo.");
            } else if (montoEntregado < TARIFA) {
                System.out.println("Pago insuficiente. Ingrese nuevamente.");
            }
        } while (montoEntregado < 0 || montoEntregado < TARIFA);
        
        return montoEntregado;
    }


    static int[] buscarPosicionPlaca(String buscado) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (placas[i][j] != null && placas[i][j].equalsIgnoreCase(buscado)) {
                    return new int[]{i, j}; 
                }
            }
        }
        return new int[]{-1, -1}; 
    }

    static int[] generarPosicionEnBorde(Random rnd) {
        int lado = rnd.nextInt(4);
        int fila, columna;
        
        switch (lado) {
            case 0: 
                fila = 0;
                columna = 1 + rnd.nextInt(COLUMNAS - 2);
                break;
            case 1: 
                fila = FILAS - 1;
                columna = 1 + rnd.nextInt(COLUMNAS - 2);
                break;
            case 2: 
                columna = 0;
                fila = 1 + rnd.nextInt(FILAS - 2);
                break;
            default: 
                columna = COLUMNAS - 1;
                fila = 1 + rnd.nextInt(FILAS - 2);
                break;
        }
        return new int[]{fila, columna};
    }


    static void construirPerimetro(int[] filaPerimetro, int[] columnaPerimetro) {
        int indice = 0;

        
        for (int columna = 0; columna < COLUMNAS; columna++) {
            filaPerimetro[indice] = 0;
            columnaPerimetro[indice] = columna;
            indice++;
        }
        
        for (int fila = 1; fila < FILAS; fila++) {
            filaPerimetro[indice] = fila;
            columnaPerimetro[indice] = COLUMNAS - 1;
            indice++;
        }
        
        for (int columna = COLUMNAS - 2; columna >= 0; columna--) {
            filaPerimetro[indice] = FILAS - 1;
            columnaPerimetro[indice] = columna;
            indice++;
        }
        
        for (int fila = FILAS - 2; fila >= 1; fila--) {
            filaPerimetro[indice] = fila;
            columnaPerimetro[indice] = 0;
            indice++;
        }
    }

    static int buscarIndiceEnPerimetro(int[] filaPerimetro, int[] columnaPerimetro, int filaBuscada, int columnaBuscada) {
        for (int i = 0; i < filaPerimetro.length; i++) {
            if (filaPerimetro[i] == filaBuscada && columnaPerimetro[i] == columnaBuscada) {
                return i;
            }
        }
        return -1;
    }

    static void calcularYMostrarRuta(int[] filaPerimetro, int[] columnaPerimetro, int filaE, int columnaE, int filaS, int columnaS) {
        int indiceEntrada = buscarIndiceEnPerimetro(filaPerimetro, columnaPerimetro, filaE, columnaE);
        int indiceSalida = buscarIndiceEnPerimetro(filaPerimetro, columnaPerimetro, filaS, columnaS);
        int total = filaPerimetro.length;

        
        int distanciaHoraria = (indiceSalida - indiceEntrada + total) % total;
        int distanciaAntihoraria = (indiceEntrada - indiceSalida + total) % total;

        System.out.println("Entrada [E]: fila " + filaE + ", columna " + columnaE);
        System.out.println("Salida [S]: fila " + filaS + ", columna " + columnaS);
        System.out.println("Distancia horaria por el borde: " + distanciaHoraria + " posiciones");
        System.out.println("Distancia antihoraria por el borde: " + distanciaAntihoraria + " posiciones");

        if (distanciaHoraria < distanciaAntihoraria) {
            System.out.println("Ruta recomendada: sentido horario (" + distanciaHoraria + " posiciones)");
        } else if (distanciaAntihoraria < distanciaHoraria) {
            System.out.println("Ruta recomendada: sentido antihorario (" + distanciaAntihoraria + " posiciones)");
        } else {
            System.out.println("Ambas rutas tienen la misma distancia por el borde.");
        }
    }
} 

