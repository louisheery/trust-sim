package com.trustsim.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.trustsim.simulator.trustmodel.EigenTrustModel;
import com.trustsim.synthesiser.AgentSystem;
import java.sql.SQLException;
import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class SQLiteDatabaseManagerTest {

    protected static SQLiteDatabaseManager sqLiteDatabaseManager;

    @BeforeAll
    public static void setUpAll() {
        sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
    }

    @AfterAll
    public static void tearDownAll() {
    }

    @Test
    public void checkAddAndRetrievalFromTable() throws SQLException {

        sqLiteDatabaseManager.addObjectToTable("trustModels", "TEST_OBJ_1", new EigenTrustModel());
        sqLiteDatabaseManager.addObjectToTable("agentSystems", "TEST_OBJ_2", new AgentSystem("TEST_SYSTEM"));
        sqLiteDatabaseManager.addObjectToTable("agentSystemsCalculator", "TEST_OBJ_3", new AgentSystem("TEST_SYSTEM"));
        sqLiteDatabaseManager.addObjectToTable("simulationResults", "TEST_OBJ_4", new AgentSystem("TEST_RESULT"));

        assertTrue(sqLiteDatabaseManager.findInTable("trustModels", "TEST_OBJ_1"));
        assertTrue(sqLiteDatabaseManager.findInTable("agentSystems", "TEST_OBJ_2"));
        assertTrue(sqLiteDatabaseManager.findInTable("agentSystemsCalculator", "TEST_OBJ_3"));
        assertTrue(sqLiteDatabaseManager.findInTable("simulationResults", "TEST_OBJ_4"));

        assertEquals("TEST_SYSTEM", ((AgentSystem) sqLiteDatabaseManager.retrieveObjectTableData("agentSystems", "TEST_OBJ_2")).getSystemName());
        assertEquals("TEST_SYSTEM", ((AgentSystem) sqLiteDatabaseManager.retrieveObjectTableData("agentSystemsCalculator", "TEST_OBJ_3")).getSystemName());

        assertEquals("TEST_RESULT", ((AgentSystem) sqLiteDatabaseManager.retrieveObjectTableData("simulationResults", "TEST_OBJ_4")).getSystemName());



    }

    @Test
    public void checkAddSameTableTwice() {

        String randomNum = String.valueOf(Math.random());

        assertNull(sqLiteDatabaseManager.retrieveObjectTableData("nonExistantTable", "test"));

        assertNotNull(sqLiteDatabaseManager.retrieveAllObjectsTableData("agentSystems"));
        assertNotNull(sqLiteDatabaseManager.retrieveAllObjectsTableData("trustModels"));
        assertNotNull(sqLiteDatabaseManager.retrieveAllObjectsTableData("simulationResults"));
        assertNotNull(sqLiteDatabaseManager.retrieveAllObjectsTableData("nonExistantTable"));


        sqLiteDatabaseManager.removeObjectFromTable("agentSystems", "test" + randomNum);
        sqLiteDatabaseManager.removeObjectFromTable("trustModels", "test" + randomNum);
        sqLiteDatabaseManager.removeObjectFromTable("simulationResults", "test" + randomNum);
    }

    @Test
    public void checkRemoveAllObjectsFromTable() {

        sqLiteDatabaseManager.removeAllObjectsFromTable("agentSystems");
        sqLiteDatabaseManager.removeAllObjectsFromTable("agentSystemsCalculator");
        sqLiteDatabaseManager.removeAllObjectsFromTable("simulationResults");
        sqLiteDatabaseManager.removeAllObjectsFromTable("trustModels");

        assertEquals(0, sqLiteDatabaseManager.retrieveAllObjectsTableData("agentSystems").size());
        assertEquals(0, sqLiteDatabaseManager.retrieveAllObjectsTableData("agentSystemsCalculator").size());
        assertEquals(0, sqLiteDatabaseManager.retrieveAllObjectsTableData("simulationResults").size());
        assertEquals(0, sqLiteDatabaseManager.retrieveAllObjectsTableData("trustModels").size());

    }

}
