package agent;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeTool implements Tool {
    public String name() { return "current_time"; }
    public String description() { return "Get current date/time (UTC). Input: {}"; }
    public String run(JsonNode input) {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
