package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.etims.entity.Etims;
import com.emtech.ushurusmart.etims.service.EtimsOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Component
public class SampleDataInitializer {

    @Autowired
    private EtimsOwnerService ownerService;


    public void initSampleData() {
        // Add sample data for Etims class
        List<Etims> etimsSampleDataList = getEtimsSampleDataList();
        for (Etims etims : etimsSampleDataList) {
            ownerService.save(etims);
        }

    }

    public List<Etims> getEtimsSampleDataList() {
        if(ownerService.count() ==0 ){
            List<Etims> etimsSampleDataList = new ArrayList<>();
            etimsSampleDataList.add(new Etims("P012345678Z", "A012345678B", "Green Energy Solutions Ltd"));
            etimsSampleDataList.add(new Etims("P098765432Z", "A098765432B", "SolarTech Innovations Ltd"));
            etimsSampleDataList.add(new Etims("P045678912Z", "A045678912B", "WindPower Enterprises Ltd"));
            etimsSampleDataList.add(new Etims("P056789123Z", "A056789123B", "SunWorks Renewable Energy Ltd"));
            etimsSampleDataList.add(new Etims("P034567891Z", "A034567891B", "EcoEnergy Solutions Ltd"));
            etimsSampleDataList.add(new Etims("P078912345Z", "A078912345B", "Renewable Power Inc."));
            etimsSampleDataList.add(new Etims("P023456789Z", "A023456789B", "CleanTech Solutions Ltd"));
            etimsSampleDataList.add(new Etims("P089123456Z", "A089123456B", "WindFarm Industries Ltd"));
            etimsSampleDataList.add(new Etims("P067891234Z", "A067891234B", "SolarWave Technologies Ltd"));
            etimsSampleDataList.add(new Etims("P032109876Z", "A032109876B", "BioEnergy Corporation"));
            etimsSampleDataList.add(new Etims("P045678912Z", "A045678912B", "Hydrogen Energy Solutions Inc."));
            etimsSampleDataList.add(new Etims("P056789123Z", "A056789123B", "SunPower Innovations Ltd"));
            etimsSampleDataList.add(new Etims("P065432189Z", "A065432189B", "Geothermal Resources Group"));
            etimsSampleDataList.add(new Etims("P078912345Z", "A078912345B", "Oceanic Energy Systems Ltd"));
            etimsSampleDataList.add(new Etims("P087654321Z", "A087654321B", "GreenGrid Technologies Inc."));
            return etimsSampleDataList;
        }
        else return new ArrayList<>();
    }
}
