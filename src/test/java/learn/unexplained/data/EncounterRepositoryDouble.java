package learn.unexplained.data;

import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;

import java.util.ArrayList;
import java.util.List;

public class EncounterRepositoryDouble implements EncounterRepository {

    private ArrayList<Encounter> encounters = new ArrayList<>();

    public EncounterRepositoryDouble(){
        encounters.add(new Encounter(1, EncounterType.UFO, "Tuesday", "It was spooky", 4));
        encounters.add(new Encounter(2, EncounterType.UFO, "Wednesday", "It was weird", 1));
        encounters.add(new Encounter(3, EncounterType.VISION, "Thursday", "It was odd", 2));
    }


    @Override
    public List<Encounter> findAll() throws DataAccessException {
        return List.of(new Encounter(2, EncounterType.CREATURE, "1/1/2015", "test description", 1));
    }

    @Override
    public List<Encounter> findByType(EncounterType encounterType) throws DataAccessException {
        ArrayList<Encounter> result = new ArrayList<>();
        for (Encounter e : encounters) {
            if (e.getType() == encounterType){
                result.add(e);
            }
        }
        return result;
    }
    

    @Override
    public boolean update(Encounter encounter) throws DataAccessException {
        return true;
    }


    @Override
    public Encounter add(Encounter encounter) throws DataAccessException {
        return encounter;
    }

    @Override
    public boolean deleteById(int encounterId) throws DataAccessException {
        return encounterId == 2;
    }
}
