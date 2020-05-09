package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore t1;
  private TorpedoStore t2;

  @BeforeEach
  public void init(){
    t1 = mock(TorpedoStore.class);
    t2 = mock(TorpedoStore.class);

    this.ship = new GT4500(t1, t2);

  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(t1.fire(1)).thenReturn(true);
    when(t2.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(t1, times(1)).fire(1);
    
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(t1.fire(1)).thenReturn(true);
    when(t2.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(t1, times(1)).fire(1);
    verify(t2, times(1)).fire(1);
  }

  // 1. CASE SINGLE, SECONDARY FIRE
  //      first fire primary, then fire secondary <- secondary fire should be true
  @Test
  public void fireTorpedo_SingleSecondary_Success(){
    when(t1.fire(1)).thenReturn(true);
    when(t2.fire(1)).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(t2, times(1)).fire(1);
  }

  // 2. CASE SINGLE, SECONDARY FIRE EMPTY
  //      first fire primary, then fire secondary <- secondary fire should be false, primary 2 fire

  @Test
  public void fireTorpedo_SingleSecondaryEmpty_Success(){
    when(t1.fire(1)).thenReturn(true);
    when(t2.isEmpty()).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(t1, times(2)).fire(1);
    verify(t2, times(1)).isEmpty();
  }

  // 3. CASE SINGLE, PRIMARY FIRE EMPTY
  //      first fire primary <- empty, so secondary should be fired.
  @Test
  public void fireTorpedo_SinglePrimaryEmpty_Success(){
    when(t1.isEmpty()).thenReturn(true);
    when(t2.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(t2, times(1)).fire(1);
  }

  // 4. CASE SINGLE, BOTH EMPTY
  //      fire primary <- empty, secondary empty, return
  @Test
  public void fireTorpedo_SingleBothEmpty_Fail(){
    when(t1.isEmpty()).thenReturn(true);
    when(t2.isEmpty()).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);

    verify(t1, times(1)).isEmpty();
    verify(t2, times(1)).isEmpty();
  }
  
  // 5. CASE ALL, PRIMARY EMPTY
  //      just secondary fired
  @Test
  public void fireTorpedo_AllPrimaryEmpty_Success(){
    when(t1.isEmpty()).thenReturn(true);
    when(t2.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(t1, times(1)).isEmpty();
    verify(t2, times(1)).fire(1);
  }

  // 6. CASE ALL, SECONDARY EMPTY
  //      just primary fired
  @Test
  public void fireTorpedo_AllSecondaryEmpty_Success(){
    when(t2.isEmpty()).thenReturn(true);
    when(t1.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(t2, times(1)).isEmpty();
    verify(t1, times(1)).fire(1);
  }

  // 7. CASE ALL, BOTH EMPTY
  //      neither of them fired
  @Test
  public void fireTorpedo_AllBothEmpty_Fail(){
    when(t2.isEmpty()).thenReturn(true);
    when(t1.isEmpty()).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);

    verify(t2, times(1)).isEmpty();
    verify(t1, times(1)).isEmpty();
  }

  @Test
  public void fireTorpedo_SinglePrimaryFireThenEmpty_Success(){
    when(t1.fire(1)).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);

    when(t1.isEmpty()).thenReturn(true);
    when(t2.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);

    verify(t1, times(1)).fire(1);
    verify(t2, times(1)).isEmpty();
    verify(t1, times(2)).isEmpty();
  }

}
