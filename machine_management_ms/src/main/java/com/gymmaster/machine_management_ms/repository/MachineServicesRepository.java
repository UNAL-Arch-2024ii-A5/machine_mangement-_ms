package com.gymmaster.machine_management_ms.repository;

import com.gymmaster.machine_management_ms.entity.MachineServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineServicesRepository extends JpaRepository<MachineServices, Long> {
     List<MachineServices> findByMachineId(Long machineId);
     void deleteByMachineId(Long machineId);
}
