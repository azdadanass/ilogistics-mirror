package ma.azdad.service;

import java.util.*;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import ma.azdad.model.*;
import ma.azdad.repos.*;

@Service
public class RouteAppService {

    private final ProximityRouteOptimizerService optimizer;
    private final PathRepos pathRepos;
    private final StopRepos stopRepos;
    private final DriverLocationRepo driverLocationRepo;
    private final TransportationJobRepos jobRepos;

    public RouteAppService(ProximityRouteOptimizerService optimizer,
                           PathRepos pathRepos,
                           StopRepos stopRepos,
                           DriverLocationRepo driverLocationRepo,
                           TransportationJobRepos jobRepos) {
        this.optimizer = optimizer;
        this.pathRepos = pathRepos;
        this.stopRepos = stopRepos;
        this.driverLocationRepo = driverLocationRepo;
        this.jobRepos = jobRepos;
    }

    @Transactional
    public List<Path> buildAndPersistPathsForJob(Integer jobId,
                                                 String driverUsername,
                                                 double clusterRadiusKm,
                                                 boolean lockFirst,
                                                 boolean lockLast) {

        TransportationJob job = jobRepos.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job introuvable: " + jobId));

        List<Stop> stops = stopRepos.findByTransportationJobIdOrderByDateAsc(jobId);
        if (stops == null || stops.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        DriverLocation driverNow = null;
        if (driverUsername != null && !driverUsername.isEmpty()) {
            driverNow = driverLocationRepo.findTopByDriverUsernameOrderByDateDesc(driverUsername);
        }

        List<Path> paths = optimizer.optimizeByProximity(
                jobId, stops, driverNow, clusterRadiusKm, lockFirst, lockLast
        );

      
        for (Path p : paths) {
            p.setTransportationJob(job);
        }
        return paths;
    }

}