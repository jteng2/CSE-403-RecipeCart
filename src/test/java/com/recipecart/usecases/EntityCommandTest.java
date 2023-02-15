/* (C)2023 */
package com.recipecart.usecases;

import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.storage.EntityStorage;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EntityCommandTest {
    private EntityStorage storage;

    private static EntityCommand goodEntityCommand_Successful() {
        return new EntityCommand() {
            @Override
            public void execute() {
                beSuccessful();
                setExecutionMessage(Command.OK_GENERAL);
                finishExecuting();
            }
        };
    }

    private static EntityCommand goodEntityCommand_Unsuccessful() {
        return new EntityCommand() {
            @Override
            public void execute() {
                finishExecuting();
            }
        };
    }

    private static EntityCommand badEntityCommand_BadOrder1() {
        return new EntityCommand() {
            @Override
            public void execute() {
                beSuccessful();
                finishExecuting();
                setExecutionMessage(Command.OK_GENERAL);
            }
        };
    }

    private static EntityCommand badEntityCommand_BadOrder2() {
        return new EntityCommand() {
            @Override
            public void execute() {
                setExecutionMessage(Command.OK_GENERAL);
                finishExecuting();
                beSuccessful();
            }
        };
    }

    private static EntityCommand badEntityCommand_DoubleFinish() {
        return new EntityCommand() {
            @Override
            public void execute() {
                finishExecuting();
                finishExecuting();
            }
        };
    }

    private static EntityCommand badEntityCommand_DoubleSuccess() {
        return new EntityCommand() {
            @Override
            public void execute() {
                beSuccessful();
                beSuccessful();
                finishExecuting();
            }
        };
    }

    private static EntityCommand badEntityCommand_DoubleMessage() {
        return new EntityCommand() {
            @Override
            public void execute() {
                setExecutionMessage(Command.OK_GENERAL);
                setExecutionMessage(Command.NOT_OK_GENERAL);
                finishExecuting();
            }
        };
    }

    private static EntityCommand nullCheckEntityCommand_NullMessage() {
        return new EntityCommand() {
            @Override
            public void execute() {
                setExecutionMessage(null);
                finishExecuting();
            }
        };
    }

    private static Stream<Arguments> goodEntityCommands() {
        return Stream.of(
                Arguments.of(goodEntityCommand_Successful()),
                Arguments.of(goodEntityCommand_Unsuccessful()));
    }

    private static Stream<Arguments> badEntityCommands() {
        return Stream.of(
                Arguments.of(badEntityCommand_BadOrder1()),
                Arguments.of(badEntityCommand_BadOrder2()),
                Arguments.of(badEntityCommand_DoubleFinish()),
                Arguments.of(badEntityCommand_DoubleSuccess()),
                Arguments.of(badEntityCommand_DoubleMessage()));
    }

    private static Stream<Arguments> nullCheckEntityCommands() {
        return Stream.of(Arguments.of(nullCheckEntityCommand_NullMessage()));
    }

    // does not include null-check commands
    private static Stream<Arguments> goodAndBadEntityCommands() {
        return Stream.concat(goodEntityCommands(), badEntityCommands());
    }

    @BeforeEach
    void initEntityStorage() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        storage = new EntityStorage(saveAndLoader, saveAndLoader);
    }

    @ParameterizedTest
    @MethodSource("goodAndBadEntityCommands")
    void testDefaultState(EntityCommand command) {
        assertFalse(command.isFinishedExecuting());

        assertThrows(IllegalStateException.class, command::isSuccessful);
        assertThrows(IllegalStateException.class, command::getExecutionMessage);
    }

    @ParameterizedTest
    @MethodSource("nullCheckEntityCommands")
    void testMessageNullCheck(EntityCommand command) {
        assertThrows(NullPointerException.class, command::execute);
    }

    @ParameterizedTest
    @MethodSource("goodAndBadEntityCommands")
    void testEntityStorage(EntityCommand command) {
        command.setStorageSource(storage);

        assertEquals(storage, command.getStorageSource());
    }

    @ParameterizedTest
    @MethodSource("badEntityCommands")
    void testBadEntityCommandExceptions(EntityCommand command) {
        assertThrows(IllegalStateException.class, command::execute);
    }

    @ParameterizedTest
    @MethodSource("goodEntityCommands")
    void testGoodEntityCommandExecutions(EntityCommand command) {
        command.execute();

        assertTrue(command.isFinishedExecuting());
    }

    @Test
    void testGoodEntityCommand_Successful() {
        EntityCommand command = goodEntityCommand_Successful();
        command.execute();

        assertTrue(command.isSuccessful());
        assertEquals(Command.OK_GENERAL, command.getExecutionMessage());
    }

    @Test
    void testGoodEntityCommand_Unsuccessful() {
        EntityCommand command = goodEntityCommand_Unsuccessful();
        command.execute();

        assertFalse(command.isSuccessful());
        assertEquals(Command.NOT_OK_GENERAL, command.getExecutionMessage());
    }
}
