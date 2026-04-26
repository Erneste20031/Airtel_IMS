package com.airtel.inventory.repository;

import com.airtel.inventory.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    @Query("SELECT d FROM Device d WHERE " +
           "LOWER(d.assetTag) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.serialNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.brand) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.model) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Device> searchDevices(@Param("search") String search);
    
    List<Device> findByStatusId(Long statusId);
    List<Device> findByDeviceTypeId(Long deviceTypeId);
    List<Device> findByDeviceCondition(String deviceCondition);
}