package com.gymmaster.machine_management_ms.service;

import com.gymmaster.machine_management_ms.dto.request.MachineDTO;
import com.gymmaster.machine_management_ms.dto.request.MachineServicesDTO;
import com.gymmaster.machine_management_ms.dto.response.ResponseDTO;
import com.gymmaster.machine_management_ms.dto.response.TypesMachines;

import java.util.List;

public interface IMachineService {
    MachineDTO createMachine(MachineDTO machineDTO);
    MachineDTO getMachineById(Long id);
    List<MachineDTO> getAllMachines();
    MachineDTO updateMachine(Long id, MachineDTO machineDTO);
    ResponseDTO deleteMachine(Long id);
    MachineServicesDTO addMachineService(Long machineId, MachineServicesDTO machineServicesDTO);
    List<MachineServicesDTO> getServicesByMachineId(Long machineId);
    List<MachineDTO> getAvalableMachines(String state);
    TypesMachines getAllTypes();
    List<MachineDTO> getMachinesByType(String type);

    ResponseDTO updateMachineInUse(Long id);
}
