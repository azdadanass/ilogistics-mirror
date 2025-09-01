package ma.azdad.service;

import java.util.*;
import org.springframework.stereotype.Service;
import ma.azdad.model.*;

@Service
public class ProximityRouteOptimizerService {

    public List<Path> optimizeByProximity(Integer jobId,
                                          List<Stop> stops,
                                          DriverLocation driverStart,
                                          double clusterRadiusKm,
                                          boolean lockFirstStop,
                                          boolean lockLastStop) {

        if (stops == null || stops.isEmpty()) return Collections.emptyList();

        Stop fixedStart = lockFirstStop ? stops.get(0) : null;
        Stop fixedEnd   = lockLastStop  ? stops.get(stops.size() - 1) : null;

        List<Stop> work = new ArrayList<>(stops);
        if (fixedStart != null) work.remove(0);
        if (fixedEnd   != null && work.contains(fixedEnd)) work.remove(fixedEnd);

        List<Cluster> clusters = buildClusters(work, clusterRadiusKm);

        LatLng startLatLng = (fixedStart != null)
                ? coordsOf(fixedStart)
                : (driverStart != null ? new LatLng(driverStart.getLatitude(), driverStart.getLongitude())
                                       : coordsOf(stops.get(0)));

        clusters.sort(Comparator.comparingDouble(c -> haversineKm(startLatLng, c.centroid())));

        List<Stop> ordered = new ArrayList<>();
        if (fixedStart != null) ordered.add(fixedStart);

        LatLng current = (fixedStart != null) ? coordsOf(fixedStart) : startLatLng;

        for (Cluster cluster : clusters) {
            List<Stop> inner = new ArrayList<>(cluster.members);
            List<Stop> innerOrdered = greedyNearestNeighbor(current, inner);
            ordered.addAll(innerOrdered);
            if (!innerOrdered.isEmpty()) {
                current = coordsOf(innerOrdered.get(innerOrdered.size() - 1));
            }
        }

        if (fixedEnd != null) ordered.add(fixedEnd);

        List<Path> paths = new ArrayList<>();
        for (int i = 0; i < ordered.size() - 1; i++) {
            Stop a = ordered.get(i);
            Stop b = ordered.get(i + 1);

            double distKm = safeDistanceKm(a, b);
            double etaMinutes = (distKm / 35.0) * 60.0; // 35 km/h par d�faut

            Path p = new Path();
            p.setFrom(a);
            p.setTo(b);
            p.setTransportationJob(a.getTransportationJob());
            p.setEstimatedDistance(distKm * 1000.0);
            p.setEstimatedDistanceText(String.format(Locale.US, "%.1f km", distKm));
            p.setEstimatedDuration(etaMinutes);
            p.setEstimatedDurationText(String.format(Locale.US, "%.0f min", etaMinutes));

            paths.add(p);
        }

        return paths;
    }

    private static class Cluster {
        final List<Stop> members = new ArrayList<>();
        LatLng centroid = null;
        void add(Stop s) { members.add(s); recomputeCentroid(); }
        LatLng centroid() { return centroid; }
        private void recomputeCentroid() {
            double sumLat = 0, sumLng = 0; int n = 0;
            for (Stop s : members) {
                LatLng c = coordsOf(s);
                if (c != null) { sumLat += c.lat; sumLng += c.lng; n++; }
            }
            centroid = (n == 0) ? null : new LatLng(sumLat / n, sumLng / n);
        }
    }

    private static List<Cluster> buildClusters(List<Stop> stops, double radiusKm) {
        List<Cluster> clusters = new ArrayList<>();
        for (Stop s : stops) {
            LatLng c = coordsOf(s);
            if (c == null) continue;

            Cluster best = null; double bestD = Double.MAX_VALUE;
            for (Cluster cl : clusters) {
                if (cl.centroid() == null) continue;
                double d = haversineKm(c, cl.centroid());
                if (d < bestD) { bestD = d; best = cl; }
            }
            if (best != null && bestD <= radiusKm) best.add(s);
            else { Cluster cl = new Cluster(); cl.add(s); clusters.add(cl); }
        }
        clusters.forEach(cl -> cl.members.sort(Comparator.comparing(Stop::getDate)));
        return clusters;
    }

    private static List<Stop> greedyNearestNeighbor(LatLng start, List<Stop> pool) {
        List<Stop> ordered = new ArrayList<>();
        LatLng current = start;
        List<Stop> remaining = new ArrayList<>(pool);
        while (!remaining.isEmpty()) {
            Stop best = null; double bestD = Double.MAX_VALUE;
            for (Stop s : remaining) {
                LatLng c = coordsOf(s); if (c == null) continue;
                double d = haversineKm(current, c);
                if (d < bestD) { bestD = d; best = s; }
            }
            if (best == null) break;
            ordered.add(best);
            current = coordsOf(best);
            remaining.remove(best);
        }
        ordered.addAll(remaining); 
        return ordered;
    }

    private static double safeDistanceKm(Stop a, Stop b) {
        LatLng ca = coordsOf(a), cb = coordsOf(b);
        if (ca == null || cb == null) return 0.0;
        return haversineKm(ca, cb);
    }

    private static LatLng coordsOf(Stop s) {
        Object place = (s.getSite() != null) ? s.getSite() : s.getWarehouse();
        if (place instanceof Localizable) {
            Localizable loc = (Localizable) place;
            Double lat = loc.getLatitude(), lng = loc.getLongitude();
            if (lat != null && lng != null) return new LatLng(lat, lng);
        }
        return null;
    }

    private static double haversineKm(LatLng a, LatLng b) {
        return haversineKm(a.lat, a.lng, b.lat, b.lng);
    }
    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1), dLon = Math.toRadians(lon2 - lon1);
        double s1 = Math.sin(dLat/2), s2 = Math.sin(dLon/2);
        double aa = s1*s1 + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*s2*s2;
        double c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1-aa));
        return R * c;
    }

    private static class LatLng { final double lat,lng; LatLng(double a,double b){lat=a;lng=b;} }
}