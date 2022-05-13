package dds.monedero.exceptions;

public class MaximoExtraccionDiarioException extends RuntimeException {
  public MaximoExtraccionDiarioException(double limite) {
    super("No puede extraer mas de $ " + 1000
        + " diarios, límite: " + limite);
  }
}