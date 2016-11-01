import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.requestMadeFor;
import static com.github.tomakehurst.wiremock.client.WireMock.requestMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LambdaRequestMatcherTest {

    @Rule
    public WireMockRule wm = new WireMockRule(wireMockConfig().dynamicPort());

    @Test
    public void lambdaStubMatcher() throws Exception {
        wm.stubFor(requestMatching(
                request -> MatchResult.of(request.getUrl().contains("magic"))
            ).willReturn(aResponse()));

        URL url = new URL("http://localhost:" + wm.port() + "/the-magic-path");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        assertThat(connection.getResponseCode(), is(200));
    }

    @Test
    public void lambdaVerification() {
        wm.verify(requestMadeFor(request ->
            MatchResult.of(!request.header("My-Header").values().isEmpty()))
        );
    }

}
