package agent;

/** One turn of the conversation. role is "user" or "assistant". */
public record Message(String role, String content) {}
