package runners;

import framework.custom.Custom;
import framework.data.dynamicValues.DynamicValuesCustom;
import framework.data.entities.Procedure;
import org.junit.Test;
import org.testng.AssertJUnit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertNull;

public class UnitTest {

    @Test
    public void validationCustom() throws Exception {
        Custom custon =new Custom();
        Procedure procedure = new Procedure();

        try {
            procedure.MethodId = 411;
        }catch (NullPointerException e) {
            e.printStackTrace();
            AssertJUnit.assertNull(e);
        }

        //custon.executeAction("M", procedure);

    }

    @Test
    public void validationDynamicValues() throws SQLException, IOException {
        System.out.println("Inicio de Prueba");
        DynamicValuesCustom dynamicValuesCustom = new DynamicValuesCustom();
        dynamicValuesCustom.SET_ID_PRUEBA("1000");
        System.out.println("Numero de cuenta: "+ dynamicValuesCustom.GET_NUMERO_CUENTA());
        System.out.println("Fin de Prueba");

        dynamicValuesCustom.SET_PARAMETROS_ROBOT("1");
        System.out.println("Cajero= "+ dynamicValuesCustom.GET_IP_ROBOT());

    }

    @Test
    public void validarPause(){

        int sec;
        String value ="60";
        if (value.contains("=")){
            sec = Integer.parseInt(value.substring(value.indexOf("=")+1));
        }else {
            sec = Integer.parseInt(value);
        }
        System.out.println("Espera "+ sec +" segundos...");
    }
}
