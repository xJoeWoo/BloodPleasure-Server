package ng.bloodpleasure

import ng.bloodpleasure.mapper.BloodPleasureMapper
import ng.bloodpleasure.router.BloodPleasureRouter
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.web.reactive.function.server.RouterFunctions

@SpringBootApplication
@Configuration
@ComponentScan("ng.bloodpleasure")
@MapperScan("ng.bloodpleasure.mapper")
class BloodpleasureApplication {

    @Bean
    fun httpHandler(bloodPleasureMapper: BloodPleasureMapper): HttpHandler =
        RouterFunctions.toHttpHandler(BloodPleasureRouter(bloodPleasureMapper).route())

}

fun main(args: Array<String>) {
    runApplication<BloodpleasureApplication>(*args)
}
