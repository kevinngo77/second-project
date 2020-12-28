package learn.unexplained.ui;

import learn.unexplained.data.DataAccessException;
import learn.unexplained.domain.EncounterResult;
import learn.unexplained.domain.EncounterService;
import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;

import java.util.List;
import java.util.Scanner;

public class Controller {

    private final EncounterService service;
    private final View view;

    public Controller(EncounterService service, View view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        view.printHeader("Welcome To Unexplained Encounters.");

        try {
            runMenuLoop();
        } catch (DataAccessException ex) {
            view.printHeader("CRITICAL ERROR:" + ex.getMessage());
        }

        view.printHeader("Goodbye");
    }

    private void runMenuLoop() throws DataAccessException {
        MenuOption option;
        do {
            option = view.displayMenuGetOption();
            switch (option) {
                case DISPLAY_ALL:
                    displayAllEncounters();
                    break;
                case ADD:
                    addEncounter();
                    break;
                case DISPLAY_BY_TYPE:
                    displayByType(view.printAllOfType());
                    break;
                case UPDATE_ENCOUNTER:
                    updateEncounter();
                    break;
                case DELETE_BY_ID:
                    deleteById();
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    private void displayAllEncounters() throws DataAccessException {
        List<Encounter> encounters = service.findAll();
        view.printAllEncounters(encounters);
    }

    private void addEncounter() throws DataAccessException {
        Encounter encounter = view.makeEncounter();
        EncounterResult result = service.add(encounter);
        view.printResult(result);
    }

    private void displayByType(EncounterType type) throws DataAccessException {
        List<Encounter> encounters = service.findByType(type);
        view.printAllEncounters(encounters);
    }

    private void updateEncounter() throws DataAccessException {
        List<Encounter> encounters = service.findAll();
        view.printAllEncounters(encounters);
        Encounter encounter = view.update(encounters);
        if (encounter == null) {
            return;
        }

        EncounterResult result = service.update(encounter);
        view.displayResult(result);
    }

    private void deleteById() throws DataAccessException {
        List<Encounter> encounters = service.findAll();
        view.printAllEncounters(encounters);

        Encounter encounter = view.findEncounter(encounters);

        EncounterResult result = service.deleteById(encounter.getEncounterId());
        view.displayResult(result);
    }
}
