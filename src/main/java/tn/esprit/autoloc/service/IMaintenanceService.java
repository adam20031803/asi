package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    Maintenance createMaintenance(Maintenance maintenance);

    Maintenance updateMaintenance(Long id, Maintenance maintenance);

    Maintenance getMaintenanceById(Long id);

    List<Maintenance> getAllMaintenances();

    void deleteMaintenance(Long id);

    List<Maintenance> getMaintenancesByVehicule(Long idVehicule);
}
