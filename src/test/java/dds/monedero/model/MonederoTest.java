package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;
  private Cuenta cuentaConSaldo;
  private Movimiento movimiento1;
  private Movimiento movimiento2;
  private Movimiento movimiento3;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    cuentaConSaldo = new Cuenta(4500);
    movimiento1 = new Movimiento(LocalDate.now(),300,true);
    movimiento2 = new Movimiento(LocalDate.now(),200,true);
    movimiento3 = new Movimiento(LocalDate.now(), 500, false);
  }


  public Cuenta DepositarMontoEnCuenta(double monto) {
    cuenta.depositar(monto);
    return cuenta;
  }

  public Cuenta CuentaConTresMovimientosRegistrados(){
    cuenta.agregarMovimiento(LocalDate.now(),300,true);
    cuenta.agregarMovimiento(LocalDate.now(),200,true);
    cuenta.agregarMovimiento(LocalDate.now(), 500, false);
    return cuenta;
  }


  @Test
  void DepositoEfectivo(){
    assertEquals(DepositarMontoEnCuenta(1500).getSaldo(),1500);
  }

  @Test
  void DepositarMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.depositar(-1500));
  }

  @Test
  public Cuenta SeHacenTresDepositosEnUnaCuenta() {
    cuenta.depositar(1500);
    cuenta.depositar(456);
    cuenta.depositar(1900);
    return cuenta;
  }

  public Cuenta SeHacenDosExtracciones() {
    cuentaConSaldo.extraer(350);
    cuentaConSaldo.extraer(200);
    return cuentaConSaldo;
  }

  @Test
  void ElSaldoDisminuyeTrasLasExtracciones(){
    assertEquals(SeHacenDosExtracciones().getSaldo(), 3950);
  }


  @Test
  void CuentaConTresDepositos(){
    assertEquals(SeHacenTresDepositosEnUnaCuenta().cantidadDeMovimientos(), 3);
  }

  @Test
  void SeIncrementaElSaldoTrasLosDepositos(){
    assertEquals(SeHacenTresDepositosEnUnaCuenta().getSaldo(), 3856);
  }


  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.depositar(1500);
          cuenta.depositar(456);
          cuenta.depositar(1900);
          cuenta.depositar(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.extraer(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.extraer(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.extraer(-500));
  }

  @Test
  public void DeTresMovimientosEnUnaCuentaSoloUnoEsExtraccion(){
    assertEquals(CuentaConTresMovimientosRegistrados().cantidadDeExtraccionesEnLosMovimientos(),1);
  }

  @Test
  public void DeTresMovimientosEnUnaCuentaDosSonDepositos(){
    assertEquals(CuentaConTresMovimientosRegistrados().cantidadDeDepositosEnLosMovimientos(),2);
  }


}