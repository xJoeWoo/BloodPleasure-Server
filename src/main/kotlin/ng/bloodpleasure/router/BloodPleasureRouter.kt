package ng.bloodpleasure.router

import ng.bloodpleasure.entity.AccountInfoEntity
import ng.bloodpleasure.entity.DeviceValueEntity
import ng.bloodpleasure.entity.GeneratedKeyEntity
import ng.bloodpleasure.mapper.BloodPleasureMapper
import ng.bloodpleasure.util.takeOrDefault
import ng.bloodpleasure.util.takeOrDo
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.*

class BloodPleasureRouter(private val mapper: BloodPleasureMapper) {


    fun route(): RouterFunction<*> =
        router {
            "/account".nest {
                GET("/") {
                    it.queryParam("mobile").takeOptionalOrEmptyResponse {
                        mapper.getAccountByMobile(it)
                            .takeOrDefault { AccountInfoEntity() }
                            .response()
                    }
                }

                POST("/") {
                    it.formData().flatMap {
                        it.getFirst("mobile").takeOrEmptyResponse {
                            generatedKeyResponse {
                                mapper.putAccountInfo(it, this)

                            }
                        }
                    }
                }
            }

            "/hardware".nest {
                GET("/") {

                    val aId = it.queryParam("accountId").takeIf { it.isPresent }?.get()?.toIntOrNull()
                    val hId = it.queryParam("hardwareId").takeIf { it.isPresent }?.get()?.takeIf { it.isNotEmpty() }

                    if (aId != null) {

                        mapper.getHardwareIdsByAccountId(aId)
                            .let {
                                if (hId != null && !it.map { it.hardwareId }.contains(hId)) {
                                    mapper.putAccountHardwareMap(aId, hId, GeneratedKeyEntity())
                                    mapper.getHardwareIdsByAccountId(aId)
                                } else
                                    it
                            }.response()
                    } else {
                        emptyResponse()
                    }
                }

                POST("/") {
                    it.formData().flatMap {
                        val aId = it.getFirst("accountId")?.toIntOrNull()
                        val hId = it.getFirst("hardwareId")

                        if (aId != null && hId != null) {
                            generatedKeyResponse {
                                mapper.putAccountHardwareMap(aId, hId, this)
                            }
                        } else {
                            emptyResponse()
                        }
                    }
                }

                "/value".nest {

                    GET("/") {

                        val limit = it.queryParam("limit").takeIf { it.isPresent }?.get()?.toIntOrNull() ?: 100;

                        it.queryParam("accountId").takeOptionalOrEmptyResponse {
                            it.toIntOrNull().takeOrEmptyResponse {
                                mapper.getDeviceValuesByAccountId(it, limit).response()
                            }
                        }
                    }

                    POST("/") {
                        it.bodyToMono(deviceValueEntityRef)
                            .flatMap {
                                mapper.putDeviceValueByAccountId(it).response()
                            }
                    }
                }
            }
        }

    private val deviceValueEntityRef = object : ParameterizedTypeReference<DeviceValueEntity>() {}

    private inline fun <reified T : Any> T.response(): Mono<ServerResponse> =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(this.toMono())

    private inline fun <T> Optional<T>.takeOptionalOrEmptyResponse(
        failMsg: String = "",
        f: (T) -> Mono<ServerResponse>
    ): Mono<ServerResponse> =
        takeOrDo({ emptyResponse(failMsg) }, f)

    private inline fun <T> T?.takeOrEmptyResponse(
        failMsg: String = "",
        f: (T) -> Mono<ServerResponse>
    ): Mono<ServerResponse> =
        takeOrDo({ emptyResponse(failMsg) }, f)

    private fun generatedKeyResponse(f: GeneratedKeyEntity.() -> Unit) =
        GeneratedKeyEntity().apply {
            try {
                f(this)
            } catch (ignored: Throwable) {
            }
        }.response()

    private fun emptyResponse(msg: String = "") = ServerResponse.ok().body(msg.toMono())

}