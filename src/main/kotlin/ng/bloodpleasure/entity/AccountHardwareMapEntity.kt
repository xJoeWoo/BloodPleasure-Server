package ng.bloodpleasure.entity

import ng.bloodpleasure.util.NoArg

@NoArg
data class AccountHardwareMapEntity(
    override var id: Int = UNKNOWN_ID,
    val accountId: Int = UNKNOWN_ID,
    val hardwareId: String = UNKNOWN_STRING
) : IdProperty