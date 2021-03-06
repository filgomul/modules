package org.motechproject.hub.mds.service;

import org.motechproject.hub.mds.HubDistributionStatus;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Data Service interface for {@link org.motechproject.hub.mds.HubDistributionStatus}.
 * The implementation is generated by the Motech Data Services module at runtime.
 */
public interface HubDistributionStatusMDSService extends
        MotechDataService<HubDistributionStatus> {

    /**
     * Finds all <code>HubDistributionStatus</code>es with given id.
     *
     * @param distributionStatusId the id of the distribution status
     * @return list of the matching distribution statuses
     */
    @Lookup(name = "By distributionStatusId")
    List<HubDistributionStatus> findByDistStatus(
            @LookupField(name = "distributionStatusId") Integer distributionStatusId);
}
