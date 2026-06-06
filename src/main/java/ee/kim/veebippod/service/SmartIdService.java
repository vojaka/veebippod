package ee.kim.veebippod.service;

import ee.kim.veebippod.dto.SmartIdDto;
import ee.sk.smartid.*;
import ee.sk.smartid.rest.dao.DeviceLinkSessionResponse;
import ee.sk.smartid.common.devicelink.interactions.DeviceLinkInteraction;
import ee.sk.smartid.rest.dao.DeviceLinkAuthenticationSessionRequest;
import org.springframework.stereotype.Service;



import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

@Service
public class SmartIdService {
    private final SmartIdClient client = new SmartIdClient();
    private DeviceLinkAuthenticationSessionRequest sessionRequest ; // request used for starting the authentication or signing session. For example authentication session request is used.


    public String smartIdLogin(SmartIdDto smartIdRecord) {
        // Initialize SmartIdClient and set connection parameters.

        configureSmartIdClient(client);

        // For security reasons a new RP challenge must be created for each new authentication request
        RpChallenge rpChallenge = RpChallengeGenerator.generate();
        // Store generated rpChallenge only on backend side. Do not expose it to the client side.
        // Used for validating authentication sessions status OK response

        // Set up builder
        DeviceLinkAuthenticationSessionRequestBuilder builder = client
                .createDeviceLinkAuthentication()
                // to use anonymous authentication, do not set semantics identifier or document number
                .withRpChallenge(rpChallenge.toBase64EncodedValue())
                .withSignatureAlgorithm(SignatureAlgorithm.RSASSA_PSS)
                .withHashAlgorithm(HashAlgorithm.SHA3_512)
                .withInteractions(Collections.singletonList(
                        DeviceLinkInteraction.displayTextAndPin("Logging into veebipood")
                ));

        // Initiate authentication session
        DeviceLinkSessionResponse authenticationSessionResponse = builder.initAuthenticationSession();

        // Get authentication session request used for starting the authentication session and use it later to validate sessions status response
        sessionRequest = builder.getAuthenticationSessionRequest();

        // Use sessionID to start polling for session status
        String sessionId = authenticationSessionResponse.sessionID();

        // Following values are used for generating device link or QR-code
        String sessionToken = authenticationSessionResponse.sessionToken();
        // Store sessionSecret only on backend side. Do not expose it to the client side.
        String sessionSecret = authenticationSessionResponse.sessionSecret();
        URI deviceLinkBase = authenticationSessionResponse.deviceLinkBase();
        // Will be used to calculate elapsed time being used in QR-code
        Instant responseReceivedAt = authenticationSessionResponse.receivedAt();

        // Next steps:
        // - Generate QR-code or device link to be displayed to the user
        // - Start querying sessions status

       // return authenticationSessionResponse;
        return startSmartIdSession(authenticationSessionResponse);
    }

    private void configureSmartIdClient(SmartIdClient client) {
        // set relying party details
        client.setRelyingPartyUUID("00000000-0000-4000-8000-000000000000");
        client.setRelyingPartyName("DEMO");
        // set Smart-ID API host URL
        client.setHostUrl("https://sid.demo.sk.ee/smart-id-rp/v3/");
        // add trusted SSL certificates
        client.setTrustedCertificates("-----BEGIN CERTIFICATE-----\n" +
                "MIIGxjCCBa6gAwIBAgIQA4Y3b5+iF/PA/Jog/YdMiTANBgkqhkiG9w0BAQsFADBZ\n" +
                "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMTMwMQYDVQQDEypE\n" +
                "aWdpQ2VydCBHbG9iYWwgRzIgVExTIFJTQSBTSEEyNTYgMjAyMCBDQTEwHhcNMjUw\n" +
                "OTI5MDAwMDAwWhcNMjYxMDEwMjM1OTU5WjBVMQswCQYDVQQGEwJFRTEQMA4GA1UE\n" +
                "BxMHVGFsbGlubjEbMBkGA1UEChMSU0sgSUQgU29sdXRpb25zIEFTMRcwFQYDVQQD\n" +
                "Ew5zaWQuZGVtby5zay5lZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
                "AKAyy0yvjRCrATznThIwCu/wPCU5mV5UZIzNWl9KXx+gQiBp92SXfTOokkfiikBH\n" +
                "09HI+yVr3zI2U6FR8Tj21GiFE3bttmpCw8tJLmTe/P0Xah1D6vVkymbBt69N24ur\n" +
                "RqhW9in84WdkPc30vGJ+TdIj3jIePAbK3hHbpm+BfeyUhM48xXRgW+cBA//6R1C9\n" +
                "lUaF9Ycylf+g/P7FpmzHRk2HF3bPyWziBVOhIADtqMyVEJk20dl0SWGsCmAJuAhM\n" +
                "mOPc87zpXYzlAlY24XgsTyQdDnqmJn8ZukDahIt9ybKH/WPLkZfw6xBnsQKXdG0J\n" +
                "HBqBsgQdPDFsrsY45o4ek0kCAwEAAaOCA4wwggOIMB8GA1UdIwQYMBaAFHSFgMBm\n" +
                "x9833s+9KTeqAx2+7c0XMB0GA1UdDgQWBBSK7cmy40mto6zFVpcvnOyggb6YnzAZ\n" +
                "BgNVHREEEjAQgg5zaWQuZGVtby5zay5lZTA+BgNVHSAENzA1MDMGBmeBDAECAjAp\n" +
                "MCcGCCsGAQUFBwIBFhtodHRwOi8vd3d3LmRpZ2ljZXJ0LmNvbS9DUFMwDgYDVR0P\n" +
                "AQH/BAQDAgWgMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjCBnwYDVR0f\n" +
                "BIGXMIGUMEigRqBEhkJodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vRGlnaUNlcnRH\n" +
                "bG9iYWxHMlRMU1JTQVNIQTI1NjIwMjBDQTEtMS5jcmwwSKBGoESGQmh0dHA6Ly9j\n" +
                "cmw0LmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbEcyVExTUlNBU0hBMjU2MjAy\n" +
                "MENBMS0xLmNybDCBhwYIKwYBBQUHAQEEezB5MCQGCCsGAQUFBzABhhhodHRwOi8v\n" +
                "b2NzcC5kaWdpY2VydC5jb20wUQYIKwYBBQUHMAKGRWh0dHA6Ly9jYWNlcnRzLmRp\n" +
                "Z2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbEcyVExTUlNBU0hBMjU2MjAyMENBMS0x\n" +
                "LmNydDAMBgNVHRMBAf8EAjAAMIIBgAYKKwYBBAHWeQIEAgSCAXAEggFsAWoAdgDX\n" +
                "bX0Q0af1d8LH6V/XAL/5gskzWmXh0LMBcxfAyMVpdwAAAZmUn22IAAAEAwBHMEUC\n" +
                "IAanrRi7HrSbyOqc6s3QsP+S8ibPGe+g2pUuzEGb57EAAiEAi0496oVaL4EKdy1x\n" +
                "3g0gEivDSKVPKfn56YdyQ52GhbYAdwDCMX5XRRmjRe5/ON6ykEHrx8IhWiK/f9W1\n" +
                "rXaa2Q5SzQAAAZmUn20+AAAEAwBIMEYCIQCgHI7+yoKhNz7CZ9/5LezV38zyg/AD\n" +
                "2AwiQrrEfa9MSgIhAJ07CbJzN6TxII1Ow+NypN7aAlLw/p86gfafmgvKS+2BAHcA\n" +
                "lE5Dh/rswe+B8xkkJqgYZQHH0184AgE/cmd9VTcuGdgAAAGZlJ9tUwAABAMASDBG\n" +
                "AiEAuW7FVE3L1aNS83JHCFWaE0TgeaeOO7uYOw2hp4Hh1xsCIQC2iXVffo69rRnt\n" +
                "gnWvYZlXb/lIIawbcXXtXT0BPyFRbjANBgkqhkiG9w0BAQsFAAOCAQEAii5sJkW/\n" +
                "8qFlivqRf8L6HCxb0Q8wTeiFl/NGYuMkBL0RdRkLm14jGyCcFaQe6A3KLjDaLPa/\n" +
                "lSbBMRexH6r3oEzJeS8iuNMjEUKngyQh5PPOToO4Oi0rcHG2HjIMeOUAi7bHviu3\n" +
                "LOWMOmpoEtf1TRNQ7SBjOAj4qNbrVZCUoGdE2A9a/XOQZAyXaDwR89pEf898qMKY\n" +
                "mvht16vVX0g7FWkZ4X1ZQ/gqSjLyRwL/2B7mrgvsEeEU9nU0ZeM9Zsi/kKCJlmJF\n" +
                "G7nAniMUckeqRHX/RRGLQTCvE0RhnWcWbP54pi7XsyBDB3L9Uhw7szL9BE8Jtbuu\n" +
                "nnzTrMS+J2MQbQ==\n" +
                "-----END CERTIFICATE-----");
    }

    private String startSmartIdSession(DeviceLinkSessionResponse sessionResponse) {

// Calculate elapsed seconds since session response
        long elapsedSeconds = Duration.between(sessionResponse.receivedAt(), Instant.now()).getSeconds();
// Build final device link URI with authCode
        URI deviceLink = client.createDynamicContent()
                .withDeviceLinkBase(String.valueOf(sessionResponse.deviceLinkBase()))
                .withDeviceLinkType(DeviceLinkType.QR_CODE)
                .withSessionType(SessionType.AUTHENTICATION)
                .withSessionToken(sessionResponse.sessionToken())
                .withElapsedSeconds(elapsedSeconds)
                .withLang("eng")
                .withDigest("YWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWE=")
                .withInteractions(sessionRequest.interactions()) // interactions from the authentication or signing session request, should be empty when used with device link certificate choice session
                .buildDeviceLink(sessionResponse.sessionSecret());

        return  QrCodeGenerator.generateDataUri(deviceLink.toString());
    }
}

