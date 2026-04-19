package de.komoot.photon;

import de.komoot.photon.openapi.PhotonFeatureCollection;
import de.komoot.photon.query.SimpleSearchRequest;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import org.jspecify.annotations.NullMarked;

/**
 * Handler for the /api (forward geocoding) endpoint, documented for OpenAPI.
 */
@NullMarked
public class ApiSearchHandler implements Handler {
    private final GenericSearchHandler<SimpleSearchRequest> delegate;

    public ApiSearchHandler(GenericSearchHandler<SimpleSearchRequest> delegate) {
        this.delegate = delegate;
    }

    @OpenApi(
        path = "/api",
        methods = {HttpMethod.GET},
        operationId = "geocoding",
        summary = "\"Forward\" geocoding: address → coordinates",
        description = "Get coordinates for a given address or POI.",
        tags = {"Photon Geocoding"},
        queryParams = {
            @OpenApiParam(name = "q", description = "The term to search for. Mandatory unless filtering via `include`/`exclude` parameters is in place.", required = false),
            @OpenApiParam(name = "lang", description = "Force a language bias for result labels — if unset, defaults to the HTTP `Accept-Language` header.", type = String.class),
            @OpenApiParam(name = "limit", description = "Maximum number of results to return. The server may return fewer.", type = Integer.class),
            @OpenApiParam(name = "lat", description = "Latitude of the focus point. Use together with `lon` to bias results towards a location. See also `location_bias_scale` and `zoom`.", type = Double.class),
            @OpenApiParam(name = "lon", description = "Longitude of the focus point. Use together with `lat` to bias results towards a location. See also `location_bias_scale` and `zoom`.", type = Double.class),
            @OpenApiParam(name = "zoom", description = "`zoom` describes the radius around the focus point to prefer. It should roughly correspond to a map zoom level. Default is 16.", type = Integer.class),
            @OpenApiParam(name = "location_bias_scale", description = "Controls how much the general prominence of a result is factored in alongside proximity. Range 0.0 (prominence ignored) to 1.0 (prominence weighted equally with distance). Default is 0.2.", type = Double.class),
            @OpenApiParam(name = "bbox", description = "Restrict results to the given bounding box. Format: `minLon,minLat,maxLon,maxLat` (e.g. `6.5,50.5,9.5,52.5`).", type = String.class),
            @OpenApiParam(name = "countrycode", description = "Restrict results to one or more countries. Accepts a comma-separated list of ISO 3166-1 alpha-2 codes (e.g. `de,at`).", type = String.class),
            @OpenApiParam(name = "osm_tag", description = "Filter results by OSM tags. Can be repeated. Syntax: include by tag/value `key:value`, exclude `!key:value`, include by key `key`, include by value `:value`, exclude by key `!key`, exclude by value `:!value`. Example: `osm_tag=tourism:museum`.", type = String.class),
            @OpenApiParam(name = "layer", description = "Restrict results to one or more place layers (e.g. `house`, `street`, `city`, `country`). Can be repeated.", type = String.class),
            @OpenApiParam(name = "include", description = "Restrict results to a named category (e.g. `accommodation`). Can be repeated.", type = String.class),
            @OpenApiParam(name = "exclude", description = "Exclude results belonging to a named category. Can be repeated.", type = String.class),
            @OpenApiParam(name = "dedupe", description = "Remove near-duplicate results from the response. Default: `true`.", type = Boolean.class),
            @OpenApiParam(name = "geometry", description = "Include the full place geometry in the response instead of just a representative point. Default: `false`.", type = Boolean.class),
            @OpenApiParam(name = "suggest_addresses", description = "Bias result ranking towards address matches over POI matches. Default: `false`.", type = Boolean.class),
            @OpenApiParam(name = "debug", description = "Include internal query debug information in the response. Default: `false`.", type = Boolean.class)
        },
        responses = {
            @OpenApiResponse(status = "200", description = "GeoJSON FeatureCollection with geocoding results.",
                content = @OpenApiContent(from = PhotonFeatureCollection.class, type = "application/json")),
            @OpenApiResponse(status = "400", description = "Bad request — invalid or missing query parameters.")
        }
    )
    @Override
    public void handle(Context ctx) throws Exception {
        delegate.handle(ctx);
    }
}
