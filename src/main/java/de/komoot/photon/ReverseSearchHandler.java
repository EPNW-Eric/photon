package de.komoot.photon;

import de.komoot.photon.openapi.PhotonFeatureCollection;
import de.komoot.photon.query.ReverseRequest;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import org.jspecify.annotations.NullMarked;

/**
 * Handler for the /reverse (reverse geocoding) endpoint, documented for OpenAPI.
 */
@NullMarked
public class ReverseSearchHandler implements Handler {
    private final GenericSearchHandler<ReverseRequest> delegate;

    public ReverseSearchHandler(GenericSearchHandler<ReverseRequest> delegate) {
        this.delegate = delegate;
    }

    @OpenApi(
        path = "/reverse",
        methods = {HttpMethod.GET},
        operationId = "reverse",
        summary = "\"Reverse\" geocoding: coordinates → address/entity",
        description = "Get the address or place description for a given coordinate. The mandatory `lat` and `lon` parameters describe the point to look up.",
        tags = {"Photon Geocoding"},
        queryParams = {
            @OpenApiParam(name = "lat", description = "Latitude of the point to look up (WGS 84, -90 to 90).", required = true, type = Double.class),
            @OpenApiParam(name = "lon", description = "Longitude of the point to look up (WGS 84, -180 to 180).", required = true, type = Double.class),
            @OpenApiParam(name = "limit", description = "Maximum number of results to return. The server may return fewer.", type = Integer.class),
            @OpenApiParam(name = "radius", description = "Restrict results to within this radius (in kilometres) of the query point. Must be between 0 and 5000.", type = Double.class),
            @OpenApiParam(name = "distance_sort", description = "Sort results by distance from the query point. Default: `true`.", type = Boolean.class),
            @OpenApiParam(name = "lang", description = "Force a language bias for result labels — if unset, defaults to the HTTP `Accept-Language` header.", type = String.class),
            @OpenApiParam(name = "osm_tag", description = "Filter results by OSM tags. Can be repeated. Syntax: include by tag/value `key:value`, exclude `!key:value`, include by key `key`, include by value `:value`, exclude by key `!key`, exclude by value `:!value`. Example (5 nearest pharmacies): `osm_tag=amenity:pharmacy`.", type = String.class),
            @OpenApiParam(name = "layer", description = "Restrict results to one or more place layers (e.g. `house`, `street`, `city`, `country`). Can be repeated.", type = String.class),
            @OpenApiParam(name = "include", description = "Restrict results to a named category. Can be repeated.", type = String.class),
            @OpenApiParam(name = "exclude", description = "Exclude results belonging to a named category. Can be repeated.", type = String.class),
            @OpenApiParam(name = "query_string_filter", description = "Additional raw OpenSearch query string filter applied to the results.", type = String.class),
            @OpenApiParam(name = "dedupe", description = "Remove near-duplicate results from the response. Default: `true`.", type = Boolean.class),
            @OpenApiParam(name = "geometry", description = "Include the full place geometry in the response instead of just a representative point. Default: `false`.", type = Boolean.class),
            @OpenApiParam(name = "debug", description = "Include internal query debug information in the response. Default: `false`.", type = Boolean.class)
        },
        responses = {
            @OpenApiResponse(status = "200", description = "GeoJSON FeatureCollection with reverse geocoding results.",
                content = @OpenApiContent(from = PhotonFeatureCollection.class, type = "application/json")),
            @OpenApiResponse(status = "400", description = "Bad request — invalid or missing query parameters.")
        }
    )
    @Override
    public void handle(Context ctx) throws Exception {
        delegate.handle(ctx);
    }
}
