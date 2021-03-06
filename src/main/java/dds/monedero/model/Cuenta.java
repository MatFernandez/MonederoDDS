package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }


  public void depositar(double monto) {
    this.validarDeposito(monto);
    saldo = saldo + monto;
    this.agregarMovimiento(LocalDate.now(),monto,true);
  }

  public void extraer(double monto) {
    this.validarExtraccion(monto);
    saldo = saldo - monto;
    this.agregarMovimiento(LocalDate.now(),monto,false);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double cantidadDeMovimientos(){
    return movimientos.size();
  }


  public double getMontoExtraidoA(LocalDate fecha) {
    return movimientos.stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }


  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }


  public void validarDeposito(double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }

    if (this.cantidadDeDepositosEnLosMovimientos() >= 3) {
      throw new MaximaCantidadDepositosException();
    }
  }

  public void validarExtraccion(double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }
    if (saldo - monto < 0) {
      throw new SaldoMenorException(saldo);
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException(limite);
    }
  }

  public double cantidadDeExtraccionesEnLosMovimientos(){
    return movimientos.stream().filter(movimiento -> !movimiento.isDeposito()).count();
  }

  public double cantidadDeDepositosEnLosMovimientos(){
    return movimientos.stream().filter(movimiento -> movimiento.isDeposito()).count();
  }

}
