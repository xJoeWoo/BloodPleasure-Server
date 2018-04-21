package ng.bloodpleasure.entity

import ng.bloodpleasure.util.NoArg

@NoArg
data class AccountInfoEntity(
    override var id: Int = UNKNOWN_ID,
    val mobile: String = UNKNOWN_STRING
) : IdProperty