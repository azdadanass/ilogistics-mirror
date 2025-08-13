/**
 * updatePointsDistances.js
 * Calculates driving distances between job points and updates backend without rendering a map.
 *
 * Usage: call updateJobPointsDistances(jobId)
 */

// IMPORTANT: Requires Google Maps JS API to be loaded before running.
// Example in HTML: 
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY"></script>
// <script src="updatePointsDistances.js"></script>

function updateJobPointsDistances(jobId) {
    if (!jobId) {
        console.error("No jobId provided.");
        return;
    }

    const directionsService = new google.maps.DirectionsService();

    fetch(`/api/job/itenerary/${jobId}`)
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch itinerary");
            return response.json();
        })
        .then(points => {
            if (!points || points.length < 2) {
                console.warn("Not enough points to calculate distances.");
                return;
            }

            let cumulativeKm = 0;

            points.forEach((p1, i) => {
                if (i === points.length - 1) return; // skip last

                const p2 = points[i + 1];
                const segmentRequest = {
                    origin: new google.maps.LatLng(p1.latitude, p1.longitude),
                    destination: new google.maps.LatLng(p2.latitude, p2.longitude),
                    travelMode: google.maps.TravelMode.DRIVING
                };

                directionsService.route(segmentRequest, (segResult, segStatus) => {
                    if (segStatus === google.maps.DirectionsStatus.OK) {
                        const distKm = segResult.routes[0].legs[0].distance.value / 1000.0;
                        cumulativeKm += distKm;

                        if (p2.id) {
                            fetch(`/api/job/updatePoint/${p2.id}`, {
                                method: "POST",
                                headers: { "Content-Type": "application/json" },
                                body: JSON.stringify({
                                    distanceFromPrevious: distKm,
                                    cumulativeDistance: cumulativeKm
                                })
                            }).catch(err => console.error("Error saving point distance:", err));
                        } else {
                            console.warn("Skipping update for point with no id:", p2);
                        }
                    } else {
                        console.error(`Failed to get segment ${i} → ${i+1} :`, segStatus);
                    }
                });
            });
        })
        .catch(err => {
            console.error("Error during distance update:", err);
        });
}
