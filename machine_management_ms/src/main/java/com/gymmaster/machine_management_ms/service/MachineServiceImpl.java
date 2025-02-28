package com.gymmaster.machine_management_ms.service;

import java.util.List;
import java.util.stream.Collectors;

import com.gymmaster.machine_management_ms.dto.response.ResponseDTO;
import com.gymmaster.machine_management_ms.dto.response.TypesMachines;
import com.gymmaster.machine_management_ms.enums.StateMachine;
import com.gymmaster.machine_management_ms.exception.NotAvailabilityException;
import com.gymmaster.machine_management_ms.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.gymmaster.machine_management_ms.dto.request.MachineDTO;
import com.gymmaster.machine_management_ms.dto.request.MachineServicesDTO;
import com.gymmaster.machine_management_ms.entity.Machine;
import com.gymmaster.machine_management_ms.entity.MachineServices;
import com.gymmaster.machine_management_ms.repository.MachineRepository;
import com.gymmaster.machine_management_ms.repository.MachineServicesRepository;
import com.gymmaster.machine_management_ms.utils.MachineMapper;

@Service
public class MachineServiceImpl implements IMachineService{
    private final MachineRepository machineRepository;
    private final MachineServicesRepository machineServicesRepository;

    public MachineServiceImpl(MachineRepository machineRepository, MachineServicesRepository machineServicesRepository) {
        this.machineRepository = machineRepository;
        this.machineServicesRepository = machineServicesRepository;
    }

    @Override
    public MachineDTO createMachine(MachineDTO machineDTO) {
        Machine machine = new Machine();
        machine.setName(machineDTO.getName());
        machine.setDescription(machineDTO.getDescription());
        machine.setState(machineDTO.getState());
        machine.setType(machineDTO.getType());
        machine.setLastService(machineDTO.getLastService());
        machine.setServiceInterval(machineDTO.getServiceInterval());
        machine = machineRepository.save(machine);
        return MachineMapper.toDTO(machine);
    }

    @Override
    public MachineDTO getMachineById(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(() -> new NotFoundException("Machine not found with ID: " + id));
        return MachineMapper.toDTO(machine);
    }

    @Override
    public List<MachineDTO> getAllMachines() {
        return machineRepository.findAll().stream().map(MachineMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public MachineDTO updateMachine(Long id, MachineDTO machineDTO) {
        Machine machine = machineRepository.findById(id).orElseThrow(() -> new NotFoundException("Machine not found with ID: " + id));
        machine.setName(machineDTO.getName());
        machine.setDescription(machineDTO.getDescription());
        machine.setState(machineDTO.getState());
        machine.setState(machineDTO.getType());
        machine.setLastService(machineDTO.getLastService());
        machine.setServiceInterval(machineDTO.getServiceInterval());
        machine = machineRepository.save(machine);
        return MachineMapper.toDTO(machine);
    }

    @Override
    @Transactional
    public ResponseDTO deleteMachine(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new NotFoundException("Machine not found with ID: " + id);
        }
        machineServicesRepository.deleteByMachineId(id);
        machineRepository.deleteById(id);
        return new ResponseDTO("Machine with ID: "+ id + " deleted successfully!", HttpStatusCode.valueOf(204));
    }

    @Override
    public MachineServicesDTO addMachineService(Long machineId, MachineServicesDTO machineServicesDTO) {
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new NotFoundException("Machine not found with ID: " + machineId));

        MachineServices machineService = new MachineServices();
        machineService.setDate(machineServicesDTO.getDate());
        machineService.setDescription(machineServicesDTO.getDescription());
        machineService.setMachineId(machine.getId());

        machineService = machineServicesRepository.save(machineService);

        machine.setLastService(machineService.getDate());
        machineRepository.save(machine);

        return MachineMapper.toServiceDTO(machineService);
    }

    @Override
    public List<MachineServicesDTO> getServicesByMachineId(Long machineId) {
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new NotFoundException("Machine not found with ID: " + machineId));

        return machine.getMachineServices().stream()
                .map(MachineMapper::toServiceDTO)
                .toList();
    }

    @Override
    public List<MachineDTO> getAvalableMachines(String state) {
        List<MachineDTO> machines = machineRepository.findAll().stream().map(MachineMapper::toDTO).collect(Collectors.toList());
        return machines.stream()
                .filter(m -> m.getState().equals( state))
                .toList();
    }

    @Override
    public TypesMachines getAllTypes() {
        TypesMachines typesMachines = new TypesMachines(machineRepository.findAllMachineTypes());
        return typesMachines;
    }

    @Override
    public List<MachineDTO> getMachinesByType(String type) {
        List<MachineDTO> machines = machineRepository.findMachinesByType(type).stream().map(MachineMapper::toDTO).collect(Collectors.toList());
        return machines;
    }

    @Override
    public ResponseDTO updateMachineInUse(Long id) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Machine not found with ID: " + id));

        if(machine.getState().equals(StateMachine.OCUPADA.getState())){
            machine.setState(StateMachine.DISPONIBLE.getState());
        }else if(machine.getState().equals(StateMachine.DISPONIBLE.getState())){
            machine.setState(StateMachine.OCUPADA.getState());
        }else if(machine.getState().equals(StateMachine.EN_MANTENIMIENTO.getState())){
            throw new NotAvailabilityException("Machine with ID: "+ id + " is under maintenance");
        }
        machineRepository.save(machine);
        return new ResponseDTO("Machine change state: " + machine.getState() + ", successfully",HttpStatusCode.valueOf(200));
    }


}
