package com.example.springai.vision

import com.example.springai.service.LlmGateway
import com.example.springai.workflow.WorkflowProperties
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

/** Spock spec: data-driven validation tests + interaction testing with a Spock Mock. */
class VisionServiceSpec extends Specification {

    LlmGateway gateway = Mock()
    VisionService service = new VisionService(gateway, new WorkflowProperties("gemini", "m", 2, 2))

    @Unroll
    def "rejects unsupported content type #type"() {
        when:
        service.describe([1, 2, 3] as byte[], type, "p").block()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("unsupported image type")
        0 * gateway._

        where:
        type << ["application/pdf", "text/plain", ""]
    }

    def "rejects empty and oversized images"() {
        when:
        service.describe(bytes, "image/png", null).block()

        then:
        thrown(IllegalArgumentException)

        where:
        bytes << [new byte[0], new byte[(int) VisionService.MAX_BYTES + 1]]
    }

    def "fails fast when the provider has no key"() {
        given:
        gateway.isAvailable("gemini") >> false

        when:
        service.describe([1] as byte[], "image/png", null).block()

        then:
        thrown(IllegalStateException)
    }

    def "uses the default prompt and forwards image bytes to the gateway"() {
        given:
        def image = [9, 8, 7] as byte[]
        gateway.isAvailable("gemini") >> true

        when:
        def result = service.describe(image, "image/jpeg", " ").block()

        then:
        1 * gateway.describeImage("gemini", "m", "Describe this image in two sentences.", image, "image/jpeg") >>
                Mono.just("a cat")
        result == "a cat"
    }
}
