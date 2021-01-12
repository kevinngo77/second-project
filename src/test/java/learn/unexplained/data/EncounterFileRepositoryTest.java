package learn.unexplained.data;

import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EncounterFileRepositoryTest {

    static final String TEST_PATH = "./data/encounters-test.csv";
    static final String SEED_PATH = "./data/encounters-seed.csv";
    final Encounter[] testEncounters = new Encounter[]{
            new Encounter(1, EncounterType.UFO, "2020-01-01", "short test #1", 1),
            new Encounter(2, EncounterType.CREATURE, "2020-02-01", "short test #2", 1),
            new Encounter(3, EncounterType.SOUND, "2020-03-01", "short test #3", 1)
    };

    EncounterRepository repository = new EncounterFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws DataAccessException, IOException {
//        for (Encounter e : repository.findAll()) {
//            repository.deleteById(e.getEncounterId());
//        }
//
//        for (Encounter e : testEncounters) {
//            repository.add(e);
//        }
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() throws DataAccessException {
        List<Encounter> encounters = repository.findAll();
        Encounter[] actual = encounters.toArray(new Encounter[encounters.size()]);
        assertArrayEquals(testEncounters, actual);
    }

    @Test
    void shouldAdd() throws DataAccessException {
        Encounter encounter = new Encounter();
        encounter.setType(EncounterType.UFO);
        encounter.setWhen("Jan 15, 2005");
        encounter.setDescription("moving pinpoint of light." +
                "seemed to move with me along the highway. " +
                "then suddenly reversed direction without slowing down. it just reversed.");
        encounter.setOccurrences(1);

            Encounter actual = repository.add(encounter);

        assertNotNull(actual);
        assertEquals(4, actual.getEncounterId());
    }

    @Test
    void shouldListOneType() throws DataAccessException {
        int actual = repository.findByType(EncounterType.UFO).size();
        assertEquals(1, actual);
    }

    @Test
    void shouldReturnZeroTypes() throws DataAccessException {
        int actual = repository.findByType(EncounterType.VISION).size();
        assertEquals(0, actual);
    }

    @Test
    void shouldNotUpdateWithIncorrectId() throws DataAccessException {
        Encounter encounter = new Encounter();
        encounter.setEncounterId(10000);
        boolean actual = repository.update(encounter);
        assertFalse(actual);
    }

    @Test
    void shouldUpdateWithCorrectId() throws DataAccessException {
        Encounter encounter = new Encounter();
        encounter.setEncounterId(3);
        encounter.setType(EncounterType.UFO);
        encounter.setWhen("Jan 15, 2005");
        encounter.setDescription("moving pinpoint of light." +
                "seemed to move with me along the highway. " +
                "then suddenly reversed direction without slowing down. it just reversed.");
        encounter.setOccurrences(1);
        boolean actual = repository.update(encounter);
        assertTrue(actual);
    }

    @Test
    void shouldDeleteWithValidId() throws DataAccessException {
        boolean actual = repository.deleteById(3);
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteWithOutOfRangeId() throws DataAccessException {
        boolean actual =repository.deleteById(1238123);
        assertFalse(actual);
    }


}