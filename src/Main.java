import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Parqueadero parqueadero = new Parqueadero();

		int x = 0;
		int opcion = 0;
		String pPlaca;

		try {
			while (x == 0) {
				System.out.println("\nElija una opción");
				System.out.println("1. Ingresar un carro al parqueadero");
				System.out.println("2. Dar salida a un carro del parquedero");
				System.out.println("3. Informar los Ingresos del parqueadero");
				System.out.println("4. Consultar la cantidad de puestos disponibles");
				System.out.println("5. Avanzar hora");
				System.out.println("6. Cambiar la tarifa del parqueadero");
				System.out.println("7. Tiempo promedio de los carros en el parqueadero");
				System.out.println("8. Primer carro con mas horas");
				System.out.println("9. Mostrar si hay carros con más de 8 horas");
				System.out.println("10. Carros con más de tres horas de parqueo");
				System.out.println("11. Mostrar si hay carros con la misma placa");
				System.out.println("12. Mostrar Carros con PB y carros con más de 24H de Parqueo");
				System.out.println("13. Desocupar Parqueadero");
				System.out.println("14. Mostrar cantidad de carros sacados");
				System.out.println("15. Salir");

				opcion = Integer.parseInt(br.readLine());

				switch (opcion) {
				case 1: {
					System.out.println("\nIngrese la Placa del Carro");

					pPlaca = br.readLine();

					int entrarCarro = parqueadero.entrarCarro(pPlaca);

					if (entrarCarro == parqueadero.PARQUEADERO_CERRADO) {
						System.out.println("El parqueadero esta cerrado");
					} else if (entrarCarro == parqueadero.CARRO_YA_EXISTE) {
						System.out.println("Ya existe un Carro con esa placa");
					} else if (entrarCarro == parqueadero.NO_HAY_PUESTO) {
						System.out.println("No Hay puestos en el parqueadero");
					} else {
						System.out.println("Carro fue ingresado en el puesto: " + (entrarCarro + 1));
					}

				}
					break;
				case 2: {
					System.out.println("\nIngrese la Placa del Carro");

					pPlaca = br.readLine();

					int sacarCarro = parqueadero.sacarCarro(pPlaca);

					if (sacarCarro == parqueadero.PARQUEADERO_CERRADO) {
						System.out.println("\nEl parqueadero esta cerrado");
					} else if (sacarCarro == parqueadero.CARRO_NO_EXISTE) {
						System.out.println("\nEl carro no existe");
					} else {
						System.out.println("\nCarro retirador. Monto a pagar: " + sacarCarro);
					}
					break;
				}
				case 3: {
					System.out.println("\nLos ingresos del parqueadero son: " + parqueadero.darMontoCaja());
					break;
				}
				case 4: {
					System.out.println("\nPuestos disponibles: " + parqueadero.calcularPuestosLibres());
					break;
				}
				case 5: {
					parqueadero.avanzarHora();
					System.out.println("\nHora avanzada. Hora actual: " + parqueadero.darHoraActual());
					break;
				}
				case 6: {
					System.out.println("\nIngrese la nueva tarifa");
					parqueadero.cambiarTarifa(Integer.parseInt(br.readLine()));
					System.out.println("\nSe ha establecido la nueva tarifa");
					break;
				}
				case 7: {
					double tiempoPromedio = parqueadero.darTiempoPromedio();
					System.out.println("\nEl tiempo promedio que llevan los carros parqueados es: " + tiempoPromedio);
					break;
				}
				case 8: {
					String carroMasHoras = parqueadero.CarroconMayorHoras();
					System.out.println("\nEl Carro que esta más horas en el parqueadero es: " + carroMasHoras);
					break;
				}
				case 9: {
					boolean carroMas8Horas = parqueadero.hayCarroMasDeOchoHoras();
					System.out.println("\nHay Carro con mas de 8 Horas: " + carroMas8Horas);
					break;
				}
				case 10: {
					System.out.println("\nCarros más de 3 horas: " + parqueadero.darCarrosMasDeTresHorasParqueados());
					break;
				}
				case 11: {
					boolean carroPlacaIgual = parqueadero.hayCarrosPlacaIgual();
					System.out.println("\nHay Carros con placas Iguales: " + carroPlacaIgual);
					break;
				}
				case 12: {
					String respuesta1 = parqueadero.metodo1();
					System.out.println(respuesta1);
					break;
				}
				case 13: {
					parqueadero.desocuparParqueadero();
					System.out.println("\nSe ha desocuopado todo el parqueadero");
					break;
				}
				case 14: {
					String respuesta2 = parqueadero.metodo2();
					System.out.println(respuesta2);
					break;
				}
				case 15: {
					x = 1;
					break;
				}
				default:
					System.out.println("\nOpción no válida");
				}

			}
		} catch (IOException e) {
			System.err.println("Error de entrada/salida: " + e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Error al cerrar el BufferedReader: " + e.getMessage());
			}

		}
	}
}