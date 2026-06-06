package ee.kim.veebippod.dto;

public record SmartIdSessionDto(
        String sessionId,
        String sessionToken,
        String sessionSecret,
        String deviceLinkBase,
        String receivedAt
) {
}
