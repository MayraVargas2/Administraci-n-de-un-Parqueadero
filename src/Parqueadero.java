import java.util.ArrayList;

/**
 * Esta clase representa un parqueadero con TAMANO puestos.
 */
public class Parqueadero {
// -----------------------------------------------------------------
	// Constantes
	// -----------------------------------------------------------------

	/**
	 * Indica el n mero de puestos en el parqueadero.
	 */
	public static final int TAMANO = 40;

	/**
	 * Es el c digo de error para cuando el parqueadero est lleno.
	 */
	public static final int NO_HAY_PUESTO = -1;

	/**
	 * Es el c digo de error para cuando el parqueadero est cerrado.
	 */
	public static final int PARQUEADERO_CERRADO = -2;

	/**
	 * Es el c digo de error para cuando el carro que se busca no est dentro del
	 * parqueadero.
	 */
	public static final int CARRO_NO_EXISTE = -3;

	/**
	 * Es el c digo de error para cuando ya hay un carro en el parqueadero con la
	 * placa de un nuevo carro que va a entrar.
	 */
	public static final int CARRO_YA_EXISTE = -4;

	/**
	 * Es la hora a la que se abre el parqueadero.
	 */
	public static final int HORA_INICIAL = 6;

	/**
	 * Es la hora a la que se cierra el parqueadero.
	 */
	public static final int HORA_CIERRE = 21;

	/**
	 * Es la tarifa inicial del parqueadero.
	 */
	public static final int TARIFA_INICIAL = 1200;

	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	/**
	 * Contenedora de tama o fijo de puestos.
	 */
	private Puesto puestos[];

	/**
	 * Tarifa por hora en el parqueadero.
	 */
	private int tarifa;

	/**
	 * Valor recibido en la caja del parqueadero.
	 */
	private int caja;

	/**
	 * Hora actual en el parqueadero.
	 */
	private int horaActual;

	private int hora24H;

	private int carrosSacados;

	/**
	 * Indica si el parqueadero esta abierto.
	 */
	private boolean abierto;

	// -----------------------------------------------------------------
	// Constructores
	// -----------------------------------------------------------------

	/**
	 * Crea un parqueadero con su informaci n b sica. <br>
	 * <b>post: </b> Se cre un parqueadero abierto con la tarifa establecida y el
	 * arreglo de puestos est creado.
	 */
	public Parqueadero() {
		horaActual = HORA_INICIAL;
		hora24H = HORA_INICIAL;
		abierto = true;
		tarifa = TARIFA_INICIAL;
		caja = 0;
		// Crea el arreglo de puestos e inicializa cada uno de ellos
		puestos = new Puesto[TAMANO];
		for (int i = 0; i < TAMANO; i++)
			puestos[i] = new Puesto(i);
	}

	// -----------------------------------------------------------------
	// M todos
	// -----------------------------------------------------------------

	/**
	 * Retorna un mensaje con la placa del carro que se encuentra en la posici n
	 * indicada.
	 * 
	 * @param pPosicion Posicion del carro.
	 * @return Mensaje con la placa. Si no hay un carro en dicha posicion retorna un
	 *         mensaje indicando que no hay un carro en esa posicion.
	 */
	public String darPlacaCarro(int pPosicion) {
		String respuesta = "";
		if (estaOcupado(pPosicion)) {
			respuesta = "Placa: " + puestos[pPosicion].darCarro().darPlaca();
		} else {
			respuesta = "No hay un carro en esta posicion";
		}

		return respuesta;
	}

	/**
	 * Ingresa un un carro al parqueadero. <br>
	 * <b>pre: </b> El arreglo de puestos no est vac o. <br>
	 * <b>post: </b>El carro qued parqueado en el puesto indicado.
	 * 
	 * @param pPlaca Placa del carro que ingresa. pPlaca != null.
	 * @return Puesto en el que debe parquear. <br>
	 *         Si el parqueadero est lleno retorna el valor NO_HAY_PUESTO. <br>
	 *         Si el parqueadero est cerrado retorna el valor PARQUEADERO_CERRADO.
	 */
	public int entrarCarro(String pPlaca) {
		int resultado = 0;
		if (!abierto) {
			resultado = PARQUEADERO_CERRADO;
		} else {
			// Buscar en el parqueadero un carro con la placa indicada
			int numPuestoCarro = buscarPuestoCarro(pPlaca.toUpperCase());
			if (numPuestoCarro != CARRO_NO_EXISTE) {
				resultado = CARRO_YA_EXISTE;
			} else {

				// Buscar un puesto libre para el carro y agregarlo
				resultado = buscarPuestoLibre();
				if (resultado != NO_HAY_PUESTO) {
					Carro carroEntrando = new Carro(pPlaca, horaActual);
					puestos[resultado].parquearCarro(carroEntrando);
				}
			}
		}
		return resultado;
	}

	/**
	 * Sirve para sacar un carro del parqueadero y saber la cantidad de dinero que
	 * debe pagar. <br>
	 * <b>pre: </b> El arreglo de puestos no est vac o. <br>
	 * <b>post: </b> El carro sali del parqueadero y el puesto que ocupaba, ahora
	 * est libre.
	 * 
	 * @param pPlaca Placa del carro que va a salir. pPlaca != null.
	 * @return Retorna el valor a pagar. Si el carro no se encontraba dentro del
	 *         parqueadero entonces retorna CARRO_NO_EXISTE. <br>
	 *         Si el parqueadero ya estaba cerrado retorna PARQUEADERO_CERRADO.
	 */
	public int sacarCarro(String pPlaca) {
		int resultado = 0;
		if (!abierto) {
			resultado = PARQUEADERO_CERRADO;
		} else {
			int numPuesto = buscarPuestoCarro(pPlaca.toUpperCase());
			if (numPuesto == CARRO_NO_EXISTE) {
				resultado = CARRO_NO_EXISTE;
			} else {
				Carro carro = puestos[numPuesto].darCarro();
				int nHoras = carro.darTiempoEnParqueadero(hora24H);
				int porPagar = nHoras * tarifa;
				caja = caja + porPagar;
				puestos[numPuesto].sacarCarro();
				resultado = porPagar;
				carrosSacados = carrosSacados + 1;
			}
		}

		return resultado;
	}

	/**
	 * Indica la cantidad de dinero que hay en la caja.
	 * 
	 * @return Los ingresos totales en la caja.
	 */
	public int darMontoCaja() {
		return caja;
	}

	/**
	 * Indica la cantidad de puestos libres que hay.
	 * 
	 * @return El n mero de espacios vac os en el parqueadero.
	 */
	public int calcularPuestosLibres() {
		int puestosLibres = 0;
		for (Puesto puesto : puestos) {
			if (!puesto.estaOcupado()) {
				puestosLibres = puestosLibres + 1;
			}
		}
		return puestosLibres;
	}

	/**
	 * Cambia la tarifa actual del parqueadero.
	 * 
	 * @param pTarifa Tarifa nueva del parqueadero.
	 */
	public void cambiarTarifa(int pTarifa) {
		tarifa = pTarifa;
	}

	/**
	 * Busca un puesto libre en el parqueadero y lo retorna. Si no encuentra retorna
	 * el valor NO_HAY_PUESTO.
	 * 
	 * @return N mero del puesto libre encontrado.
	 */
	private int buscarPuestoLibre() {
		int puesto = NO_HAY_PUESTO;
		for (int i = 0; i < TAMANO && puesto == NO_HAY_PUESTO; i++) {
			if (!puestos[i].estaOcupado()) {
				puesto = i;
			}
		}
		return puesto;
	}

	/**
	 * Indica el n mero de puesto en el que se encuentra el carro con una placa
	 * dada. Si no lo encuentra retorna el valor CARRO_NO_EXISTE.
	 * 
	 * @param pPlaca Placa del carro que se busca. pPlaca != null.
	 * @return N mero del puesto en el que se encuentra el carro.
	 */
	private int buscarPuestoCarro(String pPlaca) {
		int puesto = CARRO_NO_EXISTE;
		for (int i = 0; i < TAMANO && puesto == CARRO_NO_EXISTE; i++) {
			if (puestos[i].tieneCarroConPlaca(pPlaca)) {
				puesto = i;
			}
		}
		return puesto;
	}

	/**
	 * Avanza una hora en el parqueadero. Si la hora actual es igual a la hora de
	 * cierre, el parqueadero se cierra.
	 */
	public void avanzarHora() {
		if (horaActual < HORA_CIERRE) {
			horaActual = (horaActual + 1);
		} else if (horaActual == HORA_CIERRE) {
			horaActual = HORA_INICIAL;
			abierto = true;
			hora24H = hora24H + 8;
		}
		if (horaActual == HORA_CIERRE) {
			abierto = false;
		}
		hora24H = hora24H + 1;
	}

	/**
	 * Retorna la hora actual.
	 * 
	 * @return La hora actual en el parqueadero.
	 */
	public int darHoraActual() {
		return horaActual;
	}

	/**
	 * Indica si el parqueadero est abierto.
	 * 
	 * @return Retorna true si el parqueadero est abierto. False en caso contrario.
	 */
	public boolean estaAbierto() {
		return abierto;
	}

	/**
	 * Retorna la tarifa por hora del parqueadero.
	 * 
	 * @return La tarifa que se est aplicando en el parqueadero.
	 */
	public int darTarifa() {
		return tarifa;
	}

	/**
	 * Indica si un puesto est ocupado.
	 * 
	 * @param pPuesto El puesto que se quiere saber si est ocupado. pPuesto >= 0 &&
	 *                pPuesto < puestos.length.
	 * @return Retorna true si el puesto est ocupado. False en caso contrario.
	 */
	public boolean estaOcupado(int pPuesto) {
		boolean ocupado = puestos[pPuesto].estaOcupado();
		return ocupado;
	}

	// -----------------------------------------------------------------
	// Puntos de Extensi n
	// -----------------------------------------------------------------

	// 2.1 Tiempo Promedio
	public double darTiempoPromedio() {
		double tiempoPromedio;
		double tHoras = 0;
		double contador = 0;
		for (int i = 0; i < TAMANO; i++) {
			if (estaOcupado(i)) {
				Carro carro = puestos[i].darCarro();
				double nHoras = carro.darTiempoEnParqueadero(hora24H);
				tHoras = nHoras + tHoras;
				contador++;
			}
		}
		tiempoPromedio = tHoras / contador;
		return tiempoPromedio;
	}

	// 2.2 Primer Carro con más Horas
	public String CarroconMayorHoras() {
		String placa = null;
		int nHoras = 0;
		int nHoras1 = 0;
		int maxHoras = 0;
		for (int i = 0; i < TAMANO; i++) {
			if (estaOcupado(i)) {
				Carro carro = puestos[i].darCarro();
				nHoras = carro.darTiempoEnParqueadero(hora24H);
				if (nHoras > maxHoras) {
					maxHoras = nHoras;
					placa = carro.darPlaca();
					if (estaOcupado(i + 1) && i != 40) {
						Carro carro1 = puestos[i + 1].darCarro();
						nHoras1 = carro1.darTiempoEnParqueadero(hora24H);
						if (nHoras == nHoras1) {
							placa = carro.darPlaca();
							break;
						}
					}
				} else if (nHoras == maxHoras) {
					break;
				}
			}
		}
		return placa;
	}

	// 2.3 Si existe un carro con más de 8 Horas
	public boolean hayCarroMasDeOchoHoras() {
		boolean carroMas8Horas = false;
		int nHoras = 0;
		for (int i = 0; i < TAMANO; i++) {
			if (estaOcupado(i)) {
				Carro carro = puestos[i].darCarro();
				nHoras = carro.darTiempoEnParqueadero(hora24H);
				if (nHoras > 8) {
					carroMas8Horas = true;
				}
			}
		}
		return carroMas8Horas;
	}

	// 2.4 Todos los carros que llevan más de 3 horas parqueados

	public ArrayList<String> darCarrosMasDeTresHorasParqueados() {
		ArrayList<String> carros = new ArrayList<>();
		int nHoras = 0;
		for (int i = 0; i < TAMANO; i++) {
			if (estaOcupado(i)) {
				Carro carro = puestos[i].darCarro();
				nHoras = carro.darTiempoEnParqueadero(hora24H);
				if (nHoras > 3) {
					carros.add(carro.darPlaca());
				}
			}
		}
		return carros;
	}
	
	// 2.5 Dos Carros con la misma placa
	public boolean hayCarrosPlacaIgual(){
		boolean placasIguales = false;
		String placa = "";
		for (int i = 0; i < TAMANO; i++) {
			if (estaOcupado(i)) {
				Carro carro = puestos[i].darCarro();
				String pPlaca = carro.darPlaca();
				if (placa.equals(pPlaca)) {
					placa = pPlaca;
					placasIguales = true;
				}
			}
		}
		return placasIguales;
	}
	

	// Carros que comienzen con PB
	public int contarCarrosQueComienzanConPlacaPB() {
		int contador = 0;
		for (int i = 0; i < TAMANO; i++) {
			String cadena = darPlacaCarro(i);
			String primerosDos = cadena.substring(7, 9);
			if (primerosDos.equals("PB")) {
				contador++;
			}
		}
		return contador;
	}

	// Carros con 24 Horas o más
	public boolean hayCarroCon24Horas() {
		boolean carro24H = false;
		if (buscarPuestoLibre() == 40) {
			carro24H = false;
		} else {
			for (int i = 0; i < TAMANO; i++) {
				if (estaOcupado(i) && hora24H - horaActual >= 24) {
					carro24H = true;
				}
			}
		}
		return carro24H;
	}

	// Desocupar parqueadero
	public void desocuparParqueadero() {
		for (int i = 0; i < TAMANO; i++) {
			if (estaOcupado(i)) {
				puestos[i].sacarCarro();
				carrosSacados = carrosSacados + 1;
			}
		}
	}

	/**
	 * M todo de extensi n 1.
	 * 
	 * @return Respuesta 1.
	 */
	public String metodo1() {
		String respuesta;
		if (hayCarroCon24Horas() == true) {
			respuesta = "Cantidad de carros con placa PB: " + String.valueOf(contarCarrosQueComienzanConPlacaPB())
					+ " - Hay carro parqueado por 24 o más horas: Si";
		} else {
			respuesta = "Cantidad de carros con placa PB: " + String.valueOf(contarCarrosQueComienzanConPlacaPB())
					+ " - Hay carro parqueado por 24 o más horas: No";
		}

		return respuesta;
	}

	/**
	 * M todo de extensi n 2.
	 * 
	 * @return Respuesta 2.
	 */
	public String metodo2() {
		return ("Cantidad de carros sacados: " + carrosSacados);
	}

}