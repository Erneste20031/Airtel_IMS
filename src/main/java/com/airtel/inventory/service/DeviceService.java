package com.airtel.inventory.service;

import com.airtel.inventory.model.Device;
import com.airtel.inventory.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    public Device saveDevice(Device device) {
        if (device.getId() == null) {
            device.setCreatedAt(LocalDate.now());
        }
        device.setUpdatedAt(LocalDate.now());
        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public List<Device> searchDevices(String search) {
        if (search == null || search.isEmpty()) {
            return getAllDevices();
        }
        return deviceRepository.searchDevices(search);
    }

    public long getDeviceCount() {
        return deviceRepository.count();
    }

    public long getAvailableDevicesCount() {
        return deviceRepository.findByStatusId(1L).size();
    }

    public long getAssignedDevicesCount() {
        return deviceRepository.findByStatusId(2L).size();
    }

    public long getUnderRepairDevicesCount() {
        return deviceRepository.findByStatusId(3L).size();
    }
}