package cryptocatbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


fun main(args: Array<String>) {
    runApplication<MainApiApplication>(*args)
}

@SpringBootApplication
open class MainApiApplication {

    @Bean
    open fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }
}