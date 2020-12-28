package learn.unexplained.domain;

import learn.unexplained.data.DataAccessException;
import learn.unexplained.data.EncounterRepository;
import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EncounterService {

    private final EncounterRepository repository;

    public EncounterService(EncounterRepository repository) {
        this.repository = repository;
    }

    public List<Encounter> findAll() throws DataAccessException {
        return repository.findAll();
    }

    public EncounterResult add(Encounter encounter) throws DataAccessException {
        EncounterResult result = validate(encounter);
        if (!result.isSuccess()) {
            return result;
        }

        // check for duplicate
        List<Encounter> encounters = repository.findAll();
        for (Encounter e : encounters) {
            if (Objects.equals(encounter.getWhen(), e.getWhen())
                    && Objects.equals(encounter.getType(), e.getType())
                    && Objects.equals(encounter.getDescription(), e.getDescription())) {
                result.addErrorMessage("duplicate encounter is not allowed");
                return result;
            }
        }

        encounter = repository.add(encounter);
        result.setPayload(encounter);
        return result;
    }

    public List<Encounter> findByType(EncounterType type) throws DataAccessException {
        return repository.findByType(type);
    }

    public EncounterResult update(Encounter encounter) throws DataAccessException {
        EncounterResult result = validate(encounter);
        if (!result.isSuccess()) {
            return result;
        }

        List<Encounter> existing = repository.findByType(encounter.getType());
        for (Encounter e : existing) {
            if (encounter.getType() == e.getType()
                    && encounter.getDescription().equals(e.getDescription())
                    && encounter.getWhen().equals(e.getWhen())
                    && encounter.getEncounterId() != e.getEncounterId()) {
                result.addErrorMessage("Cannot update a duplicate");
                return result;
            }
        }

        Encounter exists = findById(encounter);
        if (exists == null) {
            result.addErrorMessage("Encounter Id was not found");
            return result;
        }

        boolean success = repository.update(encounter);
        if (!success) {
            result.addErrorMessage("Couldnt find encounter ID");
        }
        return result;
    }

    public Encounter findById(Encounter encounter) throws DataAccessException{
        List<Encounter> encounters = repository.findAll();
        for (Encounter e : encounters) {
            if (encounter.getEncounterId() == e.getEncounterId()) {
                return e;
            }
        }
        return null;
    }

    public EncounterResult deleteById(int encounterId) throws DataAccessException{
        EncounterResult result = new EncounterResult();

        List<Encounter> encounters = repository.findAll();
        Encounter encounter = null;
        for (Encounter e : encounters) {
            if (e.getEncounterId() == encounterId) {
                encounter = e;
            }
        }
        if (encounter == null) {
            result.addErrorMessage("Could not find Encounter Id " + encounterId);
            return result;
        }

        boolean success = repository.deleteById(encounterId);
        if (!success) {
            result.addErrorMessage("Could not find Encounter Id " + encounter.getEncounterId());
            return result;
        }

        return result;
    }

    private EncounterResult validate(Encounter encounter) {

        EncounterResult result = new EncounterResult();
        if (encounter == null) {
            result.addErrorMessage("encounter cannot be null");
            return result;
        }

        if (encounter.getWhen() == null || encounter.getWhen().trim().length() == 0) {
            result.addErrorMessage("when is required");
        }

        if (encounter.getDescription() == null || encounter.getDescription().trim().length() == 0) {
            result.addErrorMessage("description is required");
        }

        if (encounter.getOccurrences() <= 0) {
            result.addErrorMessage("occurrences must be greater than 0");
        }

        return result;
    }
}
