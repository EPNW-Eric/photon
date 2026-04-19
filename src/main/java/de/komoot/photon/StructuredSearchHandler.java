package de.komoot.photon;

import de.komoot.photon.openapi.PhotonFeatureCollection;
import de.komoot.photon.query.StructuredSearchRequest;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import org.jspecify.annotations.NullMarked;

/**
 * Handler for the /structured (structured forward geocoding) endpoint, documented for OpenAPI.
 */
@NullMarked
public class StructuredSearchHandler implements Handler {
    private final GenericSearchHandler<StructuredSearchRequest> delegate;

    public StructuredSearchHandler(GenericSearchHandler<StructuredSearchRequest> delegate) {
        this.delegate = delegate;
    }

    @OpenApi(
        path = "/structured",
        methods = {HttpMethod.GET},
        operationId = "structured",
        summary = "Structured geocoding",
        description = "Structured search works like forward geocoding but the query is split into individual address components. This often yields more targeted results when geocoding a known address. At least one address parameter must be provided.",
        tags = {"Photon Geocoding"},
        queryParams = {
            @OpenApiParam(name = "countrycode", description = "ISO 3166-1 alpha-2 country code to search in (e.g. `DE`).", type = String.class),
            @OpenApiParam(name = "state", description = "State or federal region name.", type = String.class),
            @OpenApiParam(name = "county", description = "County name.", type = String.class),
            @OpenApiParam(name = "city", description = "City name (e.g. `Düsseldorf`).", type = String.class),
            @OpenApiParam(name = "postcode", description = "Postal code (e.g. `40468`).", type = String.class),
            @OpenApiParam(name = "district", description = "City district or suburb name.", type = String.class),
            @OpenApiParam(name = "street", description = "Street name (e.g. `Klaus-Bungert-Straße`).", type = String.class),
            @OpenApiParam(name = "housenumber", description = "House number (e.g. `4`).", type = String.class),
            @OpenApiParam(name = "lang", description = "Force a language bias for result labels — if unset, defaults to the HTTP `Accept-Language` header.", type = String.class),
            @OpenApiParam(name = "limit", description = "Maximum number of results to return. The server may return fewer.", type = Integer.class),
            @OpenApiParam(name = "lat", description = "Latitude of the focus point. Use together with `lon` to bias results towards a location. See also `location_bias_scale` and `zoom`.", type = Double.class),
            @OpenApiParam(name = "lon", description = "Longitude of the focus point. Use together with `lat` to bias results towards a location. See also `location_bias_scale` and `zoom`.", type = Double.class),
            @OpenApiParam(name = "zoom", description = "`zoom` describes the radius around the focus point to prefer. It should roughly correspond to a map zoom level. Default is 16.", type = Integer.class),
            @OpenApiParam(name = "location_bias_scale", description = "Controls how much the general prominence of a result is factored in alongside proximity. Range 0.0 (prominence ignored) to 1.0 (prominence weighted equally with distance). Default is 0.2.", type = Double.class),
            @OpenApiParam(name = "bbox", description = "Restrict results to the given bounding box. Format: `minLon,minLat,maxLon,maxLat` (e.g. `9.5,51.5,11.5,53.5`).", type = String.class),
            @OpenApiParam(name = "osm_tag", description = "Filter results by OSM tags. Can be repeated. Syntax: include by tag/value `key:value`, exclude `!key:value`, include by key `key`, include by value `:value`, exclude by key `!key`, exclude by value `:!value`. Example: `osm_tag=tourism:museum`.", type = String.class),
            @OpenApiParam(name = "layer", description = "Restrict results to one or more place layers (e.g. `house`, `street`, `city`, `country`). Can be repeated.", type = String.class),
            @OpenApiParam(name = "include", description = "Restrict results to a named category. Can be repeated.", type = String.class),
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
