package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        Request request = new Request.Builder().url("https://dog.ceo/api/breed/" + breed + "/list").build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            JSONArray r = new JSONObject(response.body().string()).getJSONArray("message");
            return r.toList().stream().map(Object::toString).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BreedNotFoundException(breed);
        }
    }
}