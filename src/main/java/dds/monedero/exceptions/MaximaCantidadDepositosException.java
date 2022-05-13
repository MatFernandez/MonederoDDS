package dds.monedero.exceptions;

public class MaximaCantidadDepositosException extends RuntimeException {

  public MaximaCantidadDepositosException() {
    super("Ya excedio los " + 3 + " depositos diarios");
  }

}