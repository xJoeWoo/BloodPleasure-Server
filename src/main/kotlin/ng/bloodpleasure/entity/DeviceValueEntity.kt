package ng.bloodpleasure.entity

import ng.bloodpleasure.util.NoArg

@NoArg
data class DeviceValueEntity(
    override var id: Int = UNKNOWN_ID,
    val accountId: String = UNKNOWN_STRING,
    val hardwareId: String,
    val type: Int,
    val mode: Int,
    val value: Int
) : IdProperty