package ng.bloodpleasure.mapper

import ng.bloodpleasure.entity.*
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface BloodPleasureMapper {

    @Select("SELECT * FROM $TABLE_ACCOUNT_INFO WHERE $COLUMN_MOBILE=#{$COLUMN_MOBILE}")
    fun getAccountByMobile(@Param(COLUMN_MOBILE) mobile: String): AccountInfoEntity?

    @Select("SELECT * FROM $TABLE_ACCOUNT_HARDWARE_MAP WHERE $COLUMN_ACCOUNT_ID=#{$COLUMN_ACCOUNT_ID}")
    fun getHardwareIdsByAccountId(@Param(COLUMN_ACCOUNT_ID) accountId: Int): List<AccountHardwareMapEntity>

    @Select("SELECT * FROM $TABLE_DEVICE_VALUE WHERE $COLUMN_ACCOUNT_ID=#{$COLUMN_ACCOUNT_ID} ORDER BY $COLUMN_ID DESC LIMIT #{$PARAM_LIMIT}")
    fun getDeviceValuesByAccountId(@Param(COLUMN_ACCOUNT_ID) accountId: Int, @Param(PARAM_LIMIT) limit: Int): List<DeviceValueEntity>

    @Insert("INSERT INTO $TABLE_ACCOUNT_INFO($COLUMN_MOBILE) VALUES (#{$COLUMN_MOBILE})")
    @Options(useGeneratedKeys = true, keyProperty = "$COLUMN_ID.$COLUMN_ID")
    fun putAccountInfo(@Param(COLUMN_MOBILE) mobile: String, @Param(COLUMN_ID) id: GeneratedKeyEntity): Int

    @Insert(
        "INSERT INTO $TABLE_DEVICE_VALUE($COLUMN_ACCOUNT_ID, $COLUMN_HARDWARE_ID, $COLUMN_TYPE, $COLUMN_MODE, $COLUMN_VALUE) " +
                "SELECT $COLUMN_ACCOUNT_ID, #{$PARAM_OBJ.hardwareId}, #{$PARAM_OBJ.type}, #{$PARAM_OBJ.mode}, #{$PARAM_OBJ.value} " +
                "FROM $TABLE_ACCOUNT_HARDWARE_MAP " +
                "WHERE $COLUMN_HARDWARE_ID=#{$PARAM_OBJ.hardwareId} "
    )
    @Options(useGeneratedKeys = true, keyProperty = "$PARAM_OBJ.$COLUMN_ID")
    fun putDeviceValueByAccountId(@Param(PARAM_OBJ) entity: DeviceValueEntity): Int

    @Insert("INSERT INTO $TABLE_ACCOUNT_HARDWARE_MAP($COLUMN_ACCOUNT_ID, $COLUMN_HARDWARE_ID) VALUES (#{$COLUMN_ACCOUNT_ID}, #{$COLUMN_HARDWARE_ID})")
    @Options(useGeneratedKeys = true, keyProperty = "$COLUMN_ID.$COLUMN_ID")
    fun putAccountHardwareMap(
        @Param(COLUMN_ACCOUNT_ID) accountId: Int, @Param(COLUMN_HARDWARE_ID) hardwareId: String, @Param(
            COLUMN_ID
        ) id: GeneratedKeyEntity
    ): Int


}
